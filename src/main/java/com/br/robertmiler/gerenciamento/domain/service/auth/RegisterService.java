package com.br.robertmiler.gerenciamento.domain.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.RegisterRequest;
import com.br.robertmiler.gerenciamento.domain.dtos.response.RegisterResponse;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.entities.Usuario;
import com.br.robertmiler.gerenciamento.domain.enums.UserRule;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.UsuarioMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.UsuarioRepository;

@Service
public class RegisterService {

     @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private AssociadoMapper associadoMapper;

    /*
     * @Transactional garante atomicidade no fluxo de registro de associado,
     * que envolve dois saves sequenciais: Associado e depois Usuario.
     * Sem isso, se o save do Usuario falhar, o Associado fica persistido sem vínculo de acesso.
     */
    @Transactional
    public RegisterResponse registrar(RegisterRequest request) {
        if (request.getRole() == UserRule.ROLE_ADM) {
            return registrarAdm(request);
        }
        return registrarAssociado(request);
    }

    // ============================
    // FLUXO ADM
    // ============================
    private RegisterResponse registrarAdm(RegisterRequest request) {
        validarEmailAdm(request.getEmail());

        // ✅ mapper monta a entidade
        Usuario admSalvo = usuarioRepository.save(
                usuarioMapper.toUsuarioAdm(request)
        );

        // ✅ mapper monta a resposta
        return usuarioMapper.toResponseAdm(admSalvo, request.getNomeCompleto());
    }

    // ============================
    // FLUXO ASSOCIADO / REDE
    // ============================
    private RegisterResponse registrarAssociado(RegisterRequest request) {
        validarCpfInformado(request.getCpf());
        validarEmailAssociado(request.getEmail());
        validarCpf(request.getCpf());

        // ✅ mapper monta o Associado
        Associado associadoSalvo = associadoRepository.save(
                associadoMapper.toEntityFromRegister(request)
        );

        // ✅ mapper monta o Usuario
        Usuario usuarioSalvo = usuarioRepository.save(
                usuarioMapper.toUsuarioAssociado(request, associadoSalvo)
        );

        // ✅ mapper monta a resposta
        return usuarioMapper.toResponseAssociado(usuarioSalvo, associadoSalvo);
    }

    // ============================
    // VALIDAÇÕES
    // ============================

    /*
     * As validações abaixo usam JaCadastradoException (HTTP 409) em vez de IllegalArgumentException.
     * IllegalArgumentException não é capturada pelo GlobalExceptionHandler,
     * fazendo o cliente receber 500 em casos que são erros de negócio previsíveis.
     */
    private void validarEmailAdm(String email) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new JaCadastradoException("E-mail já cadastrado: " + email);
        }
    }

    private void validarEmailAssociado(String email) {
        if (associadoRepository.findByEmailPrincipal(email).isPresent()) {
            throw new JaCadastradoException("E-mail já cadastrado: " + email);
        }
    }

    private void validarCpfInformado(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new JaCadastradoException("CPF é obrigatório para Associado");
        }
    }

    private void validarCpf(String cpf) {
        if (associadoRepository.findByCpf(cpf).isPresent()) {
            throw new JaCadastradoException("CPF já cadastrado: " + cpf);
        }
    }
}
