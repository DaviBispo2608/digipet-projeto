package com.digipet.care.repository;

import com.digipet.care.domain.ProfissionalServico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProfissionalServicoRepository extends JpaRepository<ProfissionalServico, Long> {
    List<ProfissionalServico> findByUsuarioIdAndHabilitadoTrue(Long usuarioId);
    List<ProfissionalServico> findByServicoIdAndHabilitadoTrue(Long servicoId);
    Optional<ProfissionalServico> findByUsuarioIdAndServicoId(Long usuarioId, Long servicoId);
    List<ProfissionalServico> findByUsuarioId(Long usuarioId);
}