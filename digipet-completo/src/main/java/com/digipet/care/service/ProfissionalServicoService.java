package com.digipet.care.service;

import com.digipet.care.domain.ProfissionalServico;
import com.digipet.care.domain.Servico;
import com.digipet.care.domain.Usuario;
import com.digipet.care.exception.BusinessException;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.ProfissionalServicoRepository;
import com.digipet.care.repository.ServicoRepository;
import com.digipet.care.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfissionalServicoService {

    private final ProfissionalServicoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ServicoRepository servicoRepository;

    public ProfissionalServicoService(ProfissionalServicoRepository repository, UsuarioRepository usuarioRepository,
            ServicoRepository servicoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.servicoRepository = servicoRepository;
    }

    public List<ProfissionalServico> listarDoProfissional(Long clinicaId, Long profissionalId) {
        buscarProfissional(clinicaId, profissionalId);
        return repository.findByUsuarioId(profissionalId);
    }

    @Transactional
    public ProfissionalServico habilitar(Long clinicaId, Long profissionalId, Long servicoId) {
        Usuario profissional = buscarProfissional(clinicaId, profissionalId);
        Servico servico = servicoRepository.findByIdAndClinicaId(servicoId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado para esta clínica."));

        return repository.findByUsuarioIdAndServicoId(profissionalId, servicoId)
                .map(existente -> {
                    existente.setHabilitado(true);
                    return repository.save(existente);
                })
                .orElseGet(() -> {
                    ProfissionalServico novo = new ProfissionalServico();
                    novo.setUsuario(profissional);
                    novo.setServico(servico);
                    novo.setHabilitado(true);
                    return repository.save(novo);
                });
    }

    @Transactional
    public void desabilitar(Long clinicaId, Long profissionalId, Long servicoId) {
        buscarProfissional(clinicaId, profissionalId);
        ProfissionalServico vinculo = repository.findByUsuarioIdAndServicoId(profissionalId, servicoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Este profissional não está vinculado a este serviço."));
        vinculo.setHabilitado(false);
        repository.save(vinculo);
    }

    private Usuario buscarProfissional(Long clinicaId, Long profissionalId) {
        Usuario usuario = usuarioRepository.findByIdAndClinicaId(profissionalId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado para esta clínica."));
        if (!usuario.isAtivo()) {
            throw new BusinessException("O profissional está inativo.");
        }
        return usuario;
    }
}
