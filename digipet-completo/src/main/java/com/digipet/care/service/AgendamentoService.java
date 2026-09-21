package com.digipet.care.service;

import com.digipet.care.domain.Agendamento;
import com.digipet.care.repository.AgendamentoRepository;
import com.digipet.care.repository.EscalaRepository;
import com.digipet.care.repository.PetRepository;
import com.digipet.care.repository.ProfissionalServicoRepository;
import com.digipet.care.repository.ServicoRepository;
import com.digipet.care.repository.UsuarioRepository;
import com.digipet.care.enums.StatusAgendamento;
import com.digipet.care.exception.BusinessException;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.web.AgendamentoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final PetRepository petRepository;
    private final ServicoRepository servicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfissionalServicoRepository profissionalServicoRepository;
    private final EscalaRepository escalaRepository;

    public AgendamentoService(AgendamentoRepository repository, PetRepository petRepository,
            ServicoRepository servicoRepository, UsuarioRepository usuarioRepository,
            ProfissionalServicoRepository profissionalServicoRepository, EscalaRepository escalaRepository) {
        this.repository = repository;
        this.petRepository = petRepository;
        this.servicoRepository = servicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.profissionalServicoRepository = profissionalServicoRepository;
        this.escalaRepository = escalaRepository;
    }

    // Método seguro que lista agendamentos isolados por Clínica
    public List<Agendamento> listarAgendamentosDaClinica(Long clinicaIdUsuarioLogado) {
        return repository.findAllByClinicaId(clinicaIdUsuarioLogado);
    }

    // Filtra, adicionalmente, por profissional dentro da mesma clínica
    public List<Agendamento> listarAgendamentosDoProfissional(Long clinicaId, Long profissionalId) {
        return repository.findByClinicaIdAndProfissionalId(clinicaId, profissionalId);
    }

    public Agendamento buscarPorId(Long clinicaId, Long id) {
        return repository.findByIdAndClinicaId(id, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado para esta clínica."));
    }

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequest request, Long clinicaId) {
        var pet = petRepository.findById(request.petId())
                .filter(value -> value.getDono().getClinica().getId().equals(clinicaId))
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado para esta clínica."));
        var servico = servicoRepository.findById(request.servicoId())
                .filter(value -> value.getClinicaId().equals(clinicaId) && value.isAtivo())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço ativo não encontrado para esta clínica."));
        var profissional = usuarioRepository.findById(request.profissionalId())
                .filter(value -> value.getClinica() != null && value.getClinica().getId().equals(clinicaId) && value.isAtivo())
                .orElseThrow(() -> new ResourceNotFoundException("Profissional não encontrado para esta clínica."));
        profissionalServicoRepository.findByUsuarioIdAndServicoId(profissional.getId(), servico.getId())
                .filter(value -> value.isHabilitado())
                .orElseThrow(() -> new BusinessException("O profissional não está habilitado para este serviço."));

        LocalDateTime inicio = request.dataHora();
        LocalDateTime fim = inicio.plusMinutes(servico.getDuracaoMinutos());
        boolean estaNaEscala = escalaRepository.findByProfissionalIdAndDiaSemanaAndAtivoTrue(
                profissional.getId(), inicio.getDayOfWeek()).stream()
                .anyMatch(escala -> !inicio.toLocalTime().isBefore(escala.getHoraInicio())
                        && !fim.toLocalTime().isAfter(escala.getHoraFim()));
        if (!estaNaEscala) throw new BusinessException("O horário está fora da escala do profissional.");
        if (repository.existsByClinicaIdAndProfissionalIdAndDataHoraLessThanAndDataHoraFimGreaterThan(
                clinicaId, profissional.getId(), fim, inicio)) {
            throw new BusinessException("O profissional já possui um agendamento neste horário.");
        }
        Agendamento agendamento = new Agendamento();
        agendamento.setClinicaId(clinicaId);
        agendamento.setPet(pet);
        agendamento.setServico(servico);
        agendamento.setProfissional(profissional);
        agendamento.setDataHora(inicio);
        agendamento.setDataHoraFim(fim);
        agendamento.setObservacoes(request.observacoes());
        return repository.save(agendamento);
    }

    @Transactional
    public Agendamento confirmar(Long clinicaId, Long id) {
        return transicionar(clinicaId, id, StatusAgendamento.PENDENTE, StatusAgendamento.CONFIRMADO);
    }

    @Transactional
    public Agendamento concluir(Long clinicaId, Long id) {
        return transicionar(clinicaId, id, StatusAgendamento.CONFIRMADO, StatusAgendamento.CONCLUIDO);
    }

    @Transactional
    public Agendamento cancelar(Long clinicaId, Long id) {
        Agendamento agendamento = buscarPorId(clinicaId, id);
        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO
                || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BusinessException(
                    "Não é possível cancelar um agendamento com status " + agendamento.getStatus() + ".");
        }
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return repository.save(agendamento);
    }

    private Agendamento transicionar(Long clinicaId, Long id, StatusAgendamento statusEsperado,
            StatusAgendamento novoStatus) {
        Agendamento agendamento = buscarPorId(clinicaId, id);
        if (agendamento.getStatus() != statusEsperado) {
            throw new BusinessException("Só é possível mudar para " + novoStatus
                    + " a partir do status " + statusEsperado + ". Status atual: " + agendamento.getStatus() + ".");
        }
        agendamento.setStatus(novoStatus);
        return repository.save(agendamento);
    }
}
