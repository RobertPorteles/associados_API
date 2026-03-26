package com.br.robertmiler.gerenciamento.applications.controllers;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.RenovacaoAnuidadeRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.enums.Perfil;
import com.br.robertmiler.gerenciamento.domain.service.AssociadoService;
import com.br.robertmiler.gerenciamento.domain.service.VisibilidadeFiltroService;
import com.br.robertmiler.gerenciamento.infrastructure.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/associados")
public class AssociadosController {

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private VisibilidadeFiltroService visibilidadeFiltroService;

    // ── Endpoints legados (por ID) ────────────────────────────────────────────────

    @PreAuthorize("hasRole('ADM')")
    @PostMapping
    public ResponseEntity<AssociadoResponseDto> postCadastrarAssociado(
            @Valid @RequestBody AssociadoRequestDto request) {
        var response = associadoService.cadastrarAssociado(request);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasRole('ADM')")
    @PutMapping("/{idAssociado}")
    public ResponseEntity<AssociadoResponseDto> putEditarAssociado(
            @PathVariable Long idAssociado,
            @Valid @RequestBody AssociadoRequestDto request) {
        var response = associadoService.editarAssociado(idAssociado, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResponseDto<AssociadoResponseDto>> getTodosAssociados(
            @RequestParam(defaultValue = "0") Integer number,
            @RequestParam(defaultValue = "10") Integer size) {
        var response = associadoService.buscarTodosAssociados(number, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idAssociado}")
    public ResponseEntity<AssociadoResponseDto> getAssociadoPorId(@PathVariable Long idAssociado) {
        var response = associadoService.buscarAssociadoPorId(idAssociado);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADM')")
    @PatchMapping("/{idAssociado}/confirmar-cadastro")
    public ResponseEntity<AssociadoResponseDto> patchConfirmarCadastro(@PathVariable Long idAssociado) {
        var response = associadoService.confirmarCadastro(idAssociado);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADM')")
    @PatchMapping("/{idAssociado}/renovar-anuidade")
    public ResponseEntity<AssociadoResponseDto> patchRenovarAnuidade(
            @PathVariable Long idAssociado,
            @Valid @RequestBody RenovacaoAnuidadeRequestDto request) {
        var response = associadoService.renovarAnuidade(idAssociado, request);
        return ResponseEntity.ok(response);
    }

    // ── Endpoints de visibilidade por CPF ────────────────────────────────────────

    /**
     * GET /associados/{cpf}/perfil
     *
     * Retorna o JSON do associado filtrado pelo perfil do solicitante.
     * Cada campo só aparece se o perfil tiver permissão de leitura (conforme @Visibilidade).
     * Campos condicionais respeitam o boolean companion do associado.
     */
    @GetMapping("/{cpf}/perfil")
    public ResponseEntity<Map<String, Object>> getAssociadoFiltradoPorPerfil(
            @PathVariable String cpf,
            Authentication authentication) {

        // Resolve perfil e CPF do solicitante a partir dos details do token
        UsuarioAutenticado solicitante = resolverSolicitante(authentication);

        // Constrói o view DTO completo com todos os campos
        var viewDto = associadoService.buildViewDto(cpf);

        // Filtra os campos de acordo com o perfil — nenhum JsonIgnore/JsonView envolvido
        Map<String, Object> resultado = visibilidadeFiltroService.filtrar(
                viewDto, solicitante.getPerfil(), solicitante.getCpf());

        return ResponseEntity.ok(resultado);
    }

    /**
     * PUT /associados/{cpf}/campo/{nomeCampo}
     *
     * Edita um campo individual de Associado com validação de permissão de edição.
     * Lança 403 se o perfil não tiver permissão de edição para o campo.
     * Campos auditáveis são registrados automaticamente pelo AuditoriaAspect.
     *
     * Body esperado: { "valorAnterior": "...", "valorNovo": "..." }
     */
    @PutMapping("/{cpf}/campo/{nomeCampo}")
    public ResponseEntity<AssociadoResponseDto> putEditarCampo(
            @PathVariable String cpf,
            @PathVariable String nomeCampo,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        UsuarioAutenticado solicitante = resolverSolicitante(authentication);
        String valorAnterior = body.getOrDefault("valorAnterior", null);
        String valorNovo     = body.getOrDefault("valorNovo", null);

        var response = associadoService.editarCampo(cpf, nomeCampo, valorAnterior, valorNovo,
                solicitante.getPerfil());

        return ResponseEntity.ok(response);
    }

    /**
     * POST /associados/{cpf}/permissao/{campo}
     *
     * Alterna (toggle) o boolean companion de um campo condicional.
     * Campos suportados: "aniversarioDiaMes", "enderecoComercial".
     * ADM_CC pode alternar qualquer campo; associado só pode alternar o próprio.
     */
    @PostMapping("/{cpf}/permissao/{campo}")
    public ResponseEntity<Void> postTogglePermissao(
            @PathVariable String cpf,
            @PathVariable String campo,
            Authentication authentication) {

        UsuarioAutenticado solicitante = resolverSolicitante(authentication);

        // Associado só pode alterar a própria visibilidade
        if (solicitante.getPerfil() == Perfil.ASSOCIADO
                && !cpf.equals(solicitante.getCpf())) {
            return ResponseEntity.status(403).build();
        }

        associadoService.togglePermissao(cpf, campo);
        return ResponseEntity.noContent().build();
    }

    // ── Helper ───────────────────────────────────────────────────────────────────

    /**
     * Extrai UsuarioAutenticado dos details da autenticação.
     * Fallback seguro: ASSOCIADO sem CPF, caso o token não tenha o objeto details.
     */
    private UsuarioAutenticado resolverSolicitante(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof UsuarioAutenticado ua) {
            return ua;
        }
        // Fallback para tokens sem details (testes ou requests antigas)
        return new UsuarioAutenticado(null, Perfil.ASSOCIADO);
    }
}
