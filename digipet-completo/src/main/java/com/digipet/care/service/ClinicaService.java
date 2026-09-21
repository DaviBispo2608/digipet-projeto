package com.digipet.care.service;

import com.digipet.care.domain.Clinica;
import com.digipet.care.exception.BusinessException;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.ClinicaRepository;
import com.digipet.care.web.ClinicaRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClinicaService {

    private final ClinicaRepository repository;

    public ClinicaService(ClinicaRepository repository) {
        this.repository = repository;
    }

    public List<Clinica> listar() {
        return repository.findAll();
    }

    public Clinica buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada."));
    }

    @Transactional
    public Clinica criar(ClinicaRequest request) {
        if (repository.existsByCnpj(request.cnpj())) {
            throw new BusinessException("Já existe uma clínica cadastrada com este CNPJ.");
        }
        Clinica clinica = new Clinica();
        aplicar(clinica, request);
        return repository.save(clinica);
    }

    @Transactional
    public Clinica atualizar(Long id, ClinicaRequest request) {
        Clinica clinica = buscarPorId(id);
        if (!clinica.getCnpj().equals(request.cnpj()) && repository.existsByCnpj(request.cnpj())) {
            throw new BusinessException("Já existe uma clínica cadastrada com este CNPJ.");
        }
        aplicar(clinica, request);
        return repository.save(clinica);
    }

    private void aplicar(Clinica clinica, ClinicaRequest request) {
        clinica.setRazaoSocial(request.razaoSocial());
        clinica.setNomeFantasia(request.nomeFantasia());
        clinica.setCnpj(request.cnpj());
        clinica.setEndereco(request.endereco());
        clinica.setTelefone(request.telefone());
        clinica.setEmail(request.email());
    }
}
