package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.EmpresaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.EmpresaResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.Empresa;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.AssociadoRepository;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.EmpresaRepository;

@Service
public class EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private AssociadoRepository associadoRepository;

	@Transactional
	public EmpresaResponseDto cadastrarEmpresa(EmpresaRequestDto request) {

		var associadoFound = associadoRepository.findById(request.getIdAssociado())
				.orElseThrow(() -> new NaoEncontradoException("Associado não encontrado."));

		Empresa novaEmpresa = new Empresa();
		novaEmpresa.setRazaoSocial(request.getRazaoSocial());
		novaEmpresa.setCnpj(request.getCnpj());
		novaEmpresa.setNomeFantasia(request.getNomeFantasia());
		novaEmpresa.setCargo(request.getCargo());
		novaEmpresa.setAssociado(associadoFound);

		empresaRepository.save(novaEmpresa);

		EmpresaResponseDto response = new EmpresaResponseDto();
		response.setIdEmpresa(novaEmpresa.getIdEmpresa());
		response.setRazaoSocial(novaEmpresa.getRazaoSocial());
		response.setCnpj(novaEmpresa.getCnpj());
		response.setNomeFantasia(novaEmpresa.getNomeFantasia());
		response.setCargo(novaEmpresa.getCargo());
		response.setNomeAssociado(novaEmpresa.getAssociado().getNomeCompleto());

		return response;
	}

	public Empresa buscarEmpresaEntity(Long idEmpresa) {
		return empresaRepository.findById(idEmpresa)
				.orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
	}

}
