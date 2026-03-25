package com.br.robertmiler.gerenciamento.domain.mappers;

import org.springframework.stereotype.Component;

import com.br.robertmiler.gerenciamento.domain.dtos.request.PerfilAssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PerfilAssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.PerfilAssociado;

@Component
public class PerfilAssociadoMapper {

    /**
     * Converte o request em entidade. Apenas campos editáveis são mapeados.
     * A FK do associado é resolvida no service.
     */
    public PerfilAssociado toEntity(PerfilAssociadoRequestDto request) {
        PerfilAssociado perfil = new PerfilAssociado();
        aplicarCampos(perfil, request);
        return perfil;
    }

    /**
     * Atualiza os campos editáveis de uma entidade já existente.
     */
    public void atualizarEntidade(PerfilAssociado perfil, PerfilAssociadoRequestDto request) {
        aplicarCampos(perfil, request);
    }

    /**
     * Monta o DTO de resposta com os campos editáveis + campos automáticos.
     *
     * @param perfil        entidade do perfil
     * @param nomeCargoAtual nome do cargo ativo atual (resolvido pelo service via AssociadoCargoLiderancaRepository)
     */
    public PerfilAssociadoResponseDto toResponse(PerfilAssociado perfil, String nomeCargoAtual) {
        PerfilAssociadoResponseDto response = new PerfilAssociadoResponseDto();

        // Identificadores
        response.setIdPerfil(perfil.getIdPerfil());
        response.setIdAssociado(perfil.getAssociado().getIdAssociado());

        // Campos editáveis
        response.setFotoProfissional(perfil.getFotoProfissional());
        response.setNomeProfissional(perfil.getNomeProfissional());
        response.setNomeEmpresa(perfil.getNomeEmpresa());
        response.setLogomarcaEmpresa(perfil.getLogomarcaEmpresa());
        response.setTelefonePrincipal(perfil.getTelefonePrincipal());
        response.setTelefoneSecundario(perfil.getTelefoneSecundario());
        response.setEmail(perfil.getEmail());
        response.setSite(perfil.getSite());
        response.setLinkedIn(perfil.getLinkedIn());
        response.setInstagram(perfil.getInstagram());
        response.setYouTube(perfil.getYouTube());
        response.setOutraRedeSocial(perfil.getOutraRedeSocial());
        response.setOQueFaco(perfil.getOQueFaco());
        response.setPublicoIdeal(perfil.getPublicoIdeal());
        response.setPrincipalProblemaResolvo(perfil.getPrincipalProblemaResolvo());
        response.setConexoesEstrategicas(perfil.getConexoesEstrategicas());
        response.setInteressesPessoais(perfil.getInteressesPessoais());

        // Campos automáticos da Fase 1 (acessados via lazy dentro da transação)
        var associado = perfil.getAssociado();
        response.setNomeCluster(
                associado.getCluster() != null ? associado.getCluster().getNome() : null);
        response.setNomeAtuacaoEspecifica(
                associado.getAtuacaoEspecifica() != null ? associado.getAtuacaoEspecifica().getNome() : null);
        response.setNomeEquipe(
                associado.getEquipeAtual() != null ? associado.getEquipeAtual().getNomeEquipe() : null);
        response.setStatusAssociado(associado.getStatusAssociado());
        response.setDataIngresso(associado.getDataIngresso());
        response.setDataVencimento(associado.getDataVencimento());

        // Cargo ativo resolvido pelo service
        response.setNomeCargoAtual(nomeCargoAtual);

        // Flag e metadados
        response.setPerfilCompleto(perfil.getPerfilCompleto());
        response.setCriadoEm(perfil.getCriadoEm());
        response.setAtualizadoEm(perfil.getAtualizadoEm());

        return response;
    }

    // ── Privado ───────────────────────────────────────────────────────────────

    private void aplicarCampos(PerfilAssociado perfil, PerfilAssociadoRequestDto request) {
        perfil.setFotoProfissional(request.getFotoProfissional());
        perfil.setNomeProfissional(request.getNomeProfissional());
        perfil.setNomeEmpresa(request.getNomeEmpresa());
        perfil.setLogomarcaEmpresa(request.getLogomarcaEmpresa());
        perfil.setTelefonePrincipal(request.getTelefonePrincipal());
        perfil.setTelefoneSecundario(request.getTelefoneSecundario());
        perfil.setEmail(request.getEmail());
        perfil.setSite(request.getSite());
        perfil.setLinkedIn(request.getLinkedIn());
        perfil.setInstagram(request.getInstagram());
        perfil.setYouTube(request.getYouTube());
        perfil.setOutraRedeSocial(request.getOutraRedeSocial());
        perfil.setOQueFaco(request.getOQueFaco());
        perfil.setPublicoIdeal(request.getPublicoIdeal());
        perfil.setPrincipalProblemaResolvo(request.getPrincipalProblemaResolvo());
        perfil.setConexoesEstrategicas(request.getConexoesEstrategicas());
        perfil.setInteressesPessoais(request.getInteressesPessoais());
    }

}
