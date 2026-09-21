package com.digipet.care.service;

import com.digipet.care.domain.Clinica;
import com.digipet.care.domain.Dono;
import com.digipet.care.exception.BusinessException;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.ClinicaRepository;
import com.digipet.care.repository.DonoRepository;
import com.digipet.care.web.DonoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DonoService {

    private final DonoRepository repository;
    private final ClinicaRepository clinicaRepository;

    public DonoService(DonoRepository repository, ClinicaRepository clinicaRepository) {
        this.repository = repository;
        this.clinicaRepository = clinicaRepository;
    }

    public List<Dono> listarDaClinica(Long clinicaId) {
        return repository.findAllByClinicaId(clinicaId);
    }

    public Dono buscarPorId(Long clinicaId, Long id) {
        return repository.findByIdAndClinicaId(id, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Dono não encontrado para esta clínica."));
    }

    @Transactional
    public Dono criar(Long clinicaId, DonoRequest request) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada."));
        if (repository.existsByClinicaIdAndEmail(clinicaId, request.email())) {
            throw new BusinessException("Já existe um dono cadastrado com este e-mail nesta clínica.");
        }
        Dono dono = new Dono();
        dono.setClinica(clinica);
        aplicar(dono, request);
        return repository.save(dono);
    }

    @Transactional
    public Dono atualizar(Long clinicaId, Long id, DonoRequest request) {
        Dono dono = buscarPorId(clinicaId, id);
        if (!dono.getEmail().equals(request.email())
                && repository.existsByClinicaIdAndEmail(clinicaId, request.email())) {
            throw new BusinessException("Já existe um dono cadastrado com este e-mail nesta clínica.");
        }
        aplicar(dono, request);
        return repository.save(dono);
    }

    @Transactional
    public void remover(Long clinicaId, Long id) {
        Dono dono = buscarPorId(clinicaId, id);
        repository.delete(dono);
    }

    private void aplicar(Dono dono, DonoRequest request) {
        dono.setNome(request.nome());
        dono.setEmail(request.email());
        dono.setTelefone(request.telefone());
        dono.setEndereco(request.endereco());
    }
}
