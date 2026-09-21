package com.digipet.care.service;

import com.digipet.care.domain.Escala;
import com.digipet.care.domain.Usuario;
import com.digipet.care.exception.BusinessException;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.EscalaRepository;
import com.digipet.care.repository.UsuarioRepository;
import com.digipet.care.web.EscalaRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EscalaService {

    private final EscalaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public EscalaService(EscalaRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Escala> listarDoProfissional(Long clinicaId, Long profissionalId) {
        buscarProfissional(clinicaId, profissionalId);
        return repository.findByProfissionalIdAndProfissionalClinicaId(profissionalId, clinicaId);
    }

    public Escala buscarPorId(Long clinicaId, Long profissionalId, Long id) {
        return repository.findByIdAndProfissionalIdAndProfissionalClinicaId(id, profissionalId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Escala não encontrada para este profissional."));
    }

    @Transactional
    public Escala criar(Long clinicaId, Long profissionalId, EscalaRequest request) {
        Usuario profissional = buscarProfissional(clinicaId, profissionalId);
        validarHorario(request);
        validarSobreposicao(profissionalId, request, null);
        Escala escala = new Escala();
        escala.setProfissional(profissional);
        aplicar(escala, request);
        return repository.save(escala);
    }

    @Transactional
    public Escala atualizar(Long clinicaId, Long profissionalId, Long id, EscalaRequest request) {
        Escala escala = buscarPorId(clinicaId, profissionalId, id);
        validarHorario(request);
        validarSobreposicao(profissionalId, request, id);
        aplicar(escala, request);
        return repository.save(escala);
    }

    @Transactional
    public Escala inativar(Long clinicaId, Long profissionalId, Long id) {
        Escala escala = buscarPorId(clinicaId, profissionalId, id);
        escala.setAtivo(false);
        return repository.save(escala);
    }

    @Transactional
    public Escala reativar(Long clinicaId, Long profissionalId, Long id) {
        Escala escala = buscarPorId(clinicaId, profissionalId, id);
        escala.setAtivo(true);
        return repository.save(escala);
    }

    private Usuario buscarProfissional(Long clinicaId, Long profissionalId) {
        return usuarioRepository.findByIdAndClinicaId(profissionalId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado para esta clínica."));
    }

    private void validarHorario(EscalaRequest request) {
        if (!request.horaInicio().isBefore(request.horaFim())) {
            throw new BusinessException("O horário de início deve ser anterior ao horário de fim.");
        }
    }

    // Impede que o mesmo profissional tenha duas escalas ativas sobrepostas no mesmo dia da semana
    private void validarSobreposicao(Long profissionalId, EscalaRequest request, Long idEscalaAtual) {
        boolean sobrepoe = repository.findByProfissionalIdAndDiaSemanaAndAtivoTrue(profissionalId, request.diaSemana())
                .stream()
                .filter(escala -> !escala.getId().equals(idEscalaAtual))
                .anyMatch(escala -> request.horaInicio().isBefore(escala.getHoraFim())
                        && escala.getHoraInicio().isBefore(request.horaFim()));
        if (sobrepoe) {
            throw new BusinessException("Já existe uma escala ativa que se sobrepõe a este horário.");
        }
    }

    private void aplicar(Escala escala, EscalaRequest request) {
        escala.setDiaSemana(request.diaSemana());
        escala.setHoraInicio(request.horaInicio());
        escala.setHoraFim(request.horaFim());
    }
}
