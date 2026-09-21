package com.digipet.care.repository;

import com.digipet.care.domain.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findAllByClinicaId(Long clinicaId);
    List<Servico> findAllByClinicaIdAndAtivoTrue(Long clinicaId);
    Optional<Servico> findByIdAndClinicaId(Long id, Long clinicaId);
}