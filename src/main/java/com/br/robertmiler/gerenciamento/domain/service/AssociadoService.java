package com.br.robertmiler.gerenciamento.domain.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EquipeRepository;

@Service
public class AssociadoService {

	@Autowired
	private AssociadoRepository associadoRepository;

	@Autowired
	private AssociadoEnderecoResidencialRepository enderecoResidencialRepository;

	@Autowired
	private AssociadoMapper associadoMapper;

	@Autowired
	private AssociadoEnderecoResidencialMapper associadoEnderecoResidencialMapper;

	@Autowired
	private EquipeService equipeService;

	@Autowired
	private ClusterService clusterService;

	@Autowired
	private AtuacaoEspecificaService atuacaoEspecificaService;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private PaginacaoMapper paginacaoMapper;

	@Transactional
    public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto request) {

        if (associadoRepository.findByCpf(request.getCpf()).isPresent()) {
            throw new JaCadastradoException("CPF já cadastrado para outro associado.");
        }

        if (associadoRepository.findByEmailPrincipal(request.getEmailPrincipal()).isPresent()) {
            throw new JaCadastradoException("Email já cadastrado para outro associado.");
        }

        // 1. Mapeia e salva o Associado PRIMEIRO (para o banco gerar o ID dele)
        var associado = associadoMapper.toEntity(request);
        
        // (Nota: Se EquipeAtual for obrigatório no banco, você precisa buscar a equipe 
        // e setar no associado aqui antes de salvar, igual fez no editarAssociado)
        
		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());

		equipeRepository.save(equipeAtual);
		
        associado = associadoRepository.save(associado);

        // 2. Mapeia o Endereço usando os dados que vieram no request
        var endereco = associadoEnderecoResidencialMapper.toEntity(null);
        
        // 3. FAZ O LINK: Associa a entidade 'Associado' (agora salva e com ID) ao 'Endereço'
        endereco.setAssociado(associado);
        
        // 4. Salva o endereço no banco! (Se não fizer isso, ele é descartado da memória)
        enderecoResidencialRepository.save(endereco);

        // 5. Retorna o DTO
        return associadoMapper.toResponse(associado);
    }

	@Transactional
	public AssociadoResponseDto editarAssociado(Long idAssociado, AssociadoRequestDto request) {

		var associado = buscarAssociadoEntity(idAssociado);


		//Ve se o email existe
		var emailExistente = associadoRepository.findByEmailPrincipal(request.getEmailPrincipal());
		if (emailExistente.isPresent() && !emailExistente.get().getIdAssociado().equals(idAssociado)) {
			throw new JaCadastradoException("E-mail já cadastrado para outro associado.");
		}
		//equipe Atual
		var equipeAtual = equipeService.buscarEquipeEntity(request.getIdEquipe());
		//area de atuação
		var cluster = clusterService.buscarClusterEntity(request.getIdCluster());
		//especialização
		var atuacaoEspecifica = atuacaoEspecificaService.buscarAtuacaoEspecificaEntity(request.getIdAtuacaoEspecifica());
		//cadastro comum
		associado.setNomeCompleto(request.getNomeCompleto());
		associado.setEmailPrincipal(request.getEmailPrincipal());
		associado.setTelefonePrincipal(request.getTelefonePrincipal());
		associado.setDataNascimento(request.getDataNascimento());
		associado.setDataIngresso(request.getDataIngresso());
		associado.setDataVencimento(request.getDataVencimento());
		associado.setTipoOrigemEquipe(request.getTipoOrigemEquipe());
		associado.setStatusAssociado(request.getStatusAssociado());
		associado.setEquipeAtual(equipeAtual);
		associado.setCluster(cluster);

		//No futuro teremos que fazer um calculo doido
		associado.setAtuacaoEspecifica(atuacaoEspecifica);
		associado.setAtualizadoEm(LocalDateTime.now());

		associadoRepository.save(associado);

		return associadoMapper.toResponse(associado);
	}

	@Transactional(readOnly = true)
	public PaginacaoResponseDto<AssociadoResponseDto> buscarTodosAssociados(Integer number, Integer size) {
		var pageable = PageRequest.of(number, size);
		var page = associadoRepository.findAll(pageable).map(associadoMapper::toResponse);
		return paginacaoMapper.montarDtoResposta(page);
	}

	public AssociadoResponseDto buscarAssociadoPorId(Long idAssociado) {
		var associadoFound = buscarAssociadoEntity(idAssociado);
		return associadoMapper.toResponse(associadoFound);
	}

	public Associado buscarAssociadoEntity(Long idAssociado) {
		return associadoRepository.findById(idAssociado)
				.orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));
	}


}
