package com.digipet.care.service;

import com.digipet.care.domain.Servico;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.ClinicaRepository;
import com.digipet.care.repository.ServicoRepository;
import com.digipet.care.web.ServicoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repository;
    private final ClinicaRepository clinicaRepository;

    public ServicoService(ServicoRepository repository, ClinicaRepository clinicaRepository) {
        this.repository = repository;
        this.clinicaRepository = clinicaRepository;
    }

    public List<Servico> listarDaClinica(Long clinicaId, boolean apenasAtivos) {
        return apenasAtivos
                ? repository.findAllByClinicaIdAndAtivoTrue(clinicaId)
                : repository.findAllByClinicaId(clinicaId);
    }

    public Servico buscarPorId(Long clinicaId, Long id) {
        return repository.findByIdAndClinicaId(id, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado para esta clínica."));
    }

    @Transactional
    public Servico criar(Long clinicaId, ServicoRequest request) {
        if (!clinicaRepository.existsById(clinicaId)) {
            throw new ResourceNotFoundException("Clínica não encontrada.");
        }
        Servico servico = new Servico();
        servico.setClinicaId(clinicaId);
        aplicar(servico, request);
        return repository.save(servico);
    }

    @Transactional
    public Servico atualizar(Long clinicaId, Long id, ServicoRequest request) {
        Servico servico = buscarPorId(clinicaId, id);
        aplicar(servico, request);
        return repository.save(servico);
    }

    @Transactional
    public Servico inativar(Long clinicaId, Long id) {
        Servico servico = buscarPorId(clinicaId, id);
        servico.setAtivo(false);
        return repository.save(servico);
    }

    @Transactional
    public Servico reativar(Long clinicaId, Long id) {
        Servico servico = buscarPorId(clinicaId, id);
        servico.setAtivo(true);
        return repository.save(servico);
    }

    private void aplicar(Servico servico, ServicoRequest request) {
        servico.setNome(request.nome());
        servico.setTipo(request.tipo());
        servico.setDuracaoMinutos(request.duracaoMinutos());
        servico.setDescricao(request.descricao());
        servico.setPrecoBase(request.precoBase());
    }
}
