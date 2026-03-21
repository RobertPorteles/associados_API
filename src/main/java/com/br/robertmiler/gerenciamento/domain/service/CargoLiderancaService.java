package com.br.robertmiler.gerenciamento.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.robertmiler.gerenciamento.domain.dtos.request.CargoLiderancaRequestDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.CargoLiderancaResponseDto;
import com.br.robertmiler.gerenciamento.domain.dtos.response.PaginacaoResponseDto;
import com.br.robertmiler.gerenciamento.domain.entities.CargoLideranca;
import com.br.robertmiler.gerenciamento.domain.exceptions.NaoEncontradoException;
import com.br.robertmiler.gerenciamento.domain.exceptions.RegraNegocioException;
import com.br.robertmiler.gerenciamento.domain.helpers.FormataString;
import com.br.robertmiler.gerenciamento.domain.mappers.CargoLiderancaMapper;
import com.br.robertmiler.gerenciamento.domain.mappers.PaginacaoMapper;
import com.br.robertmiler.gerenciamento.infrastructure.repositories.CargoLiderancaRepository;

@Service
public class CargoLiderancaService {

    @Autowired
    private CargoLiderancaRepository cargoLiderancaRepository;

    @Autowired
    private CargoLiderancaMapper cargoLiderancaMapper;

    @Autowired
    private PaginacaoMapper paginacaoMapper;

    @Transactional
    public CargoLiderancaResponseDto cadastrarCargo(CargoLiderancaRequestDto request) {

        cargoLiderancaRepository.findByNomeCargo(FormataString.primeiraLetraMaiuscula(request.getNomeCargo()))
                .ifPresent(c -> {
                    throw new RegraNegocioException("Já existe um cargo com este nome.");
                });

        var cargo = cargoLiderancaMapper.toEntity(request);
        cargoLiderancaRepository.save(cargo);

        return cargoLiderancaMapper.toResponse(cargo);
    }

    @Transactional
    public CargoLiderancaResponseDto editarCargo(Long idCargoLideranca, CargoLiderancaRequestDto request) {

        var cargo = buscarCargoEntity(idCargoLideranca);

        var nomeNormalizado = FormataString.primeiraLetraMaiuscula(request.getNomeCargo());

        var cargoComMesmoNome = cargoLiderancaRepository.findByNomeCargo(nomeNormalizado);
        if (cargoComMesmoNome.isPresent() && !cargoComMesmoNome.get().getIdCargoLideranca().equals(idCargoLideranca)) {
            throw new RegraNegocioException("Já existe um cargo com este nome.");
        }

        cargo.setNomeCargo(nomeNormalizado);
        cargo.setClassificacaoFinanceira(request.getClassificacaoFinanceira());
        if (request.getAtivo() != null) {
            cargo.setAtivo(request.getAtivo());
        }

        cargoLiderancaRepository.save(cargo);

        return cargoLiderancaMapper.toResponse(cargo);
    }

    @Transactional(readOnly = true)
    public CargoLiderancaResponseDto buscarCargoPorId(Long idCargoLideranca) {
        var cargo = buscarCargoEntity(idCargoLideranca);
        return cargoLiderancaMapper.toResponse(cargo);
    }

    @Transactional(readOnly = true)
    public CargoLideranca buscarCargoEntity(Long idCargoLideranca) {
        return cargoLiderancaRepository.findById(idCargoLideranca)
                .orElseThrow(() -> new NaoEncontradoException("Cargo de liderança não encontrado."));
    }

    @Transactional(readOnly = true)
    public PaginacaoResponseDto<CargoLiderancaResponseDto> buscarTodosCargos(Integer number, Integer size) {
        var pageable = PageRequest.of(number, size, Sort.by("nomeCargo").ascending());
        var page = cargoLiderancaRepository.findAll(pageable).map(cargoLiderancaMapper::toResponse);
        return paginacaoMapper.montarDtoResposta(page);
    }

}
