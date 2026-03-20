package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoEnderecoResidencialRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.request.AssociadoRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.AssociadoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Associado;
import com.br.robertmiler.gerenciamento.domain.exceptions.JaCadastradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoEnderecoResidencialMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.AssociadoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoEnderecoResidencialRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;

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

	@Transactional
	public AssociadoResponseDto cadastrarAssociado(AssociadoRequestDto request, AssociadoEnderecoResidencialRequestDto requestEndereco) {

		if (associadoRepository.findByCpf(request.getCpf()).isPresent()) {
			throw new JaCadastradoException("CPF já cadastrado para outro associado.");
		}

		var associado = associadoMapper.toEntity(request);

		associadoRepository.save(associado);

		var endereco = associadoEnderecoResidencialMapper.toEntity(requestEndereco);
		enderecoResidencialRepository.save(endereco);
		
		var associadoResponse = associadoMapper.toResponse(associado);
		
		return associadoResponse;

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
