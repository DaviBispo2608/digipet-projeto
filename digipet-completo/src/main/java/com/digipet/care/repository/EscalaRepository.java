package com.digipet.care.repository;

import com.digipet.care.domain.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface EscalaRepository extends JpaRepository<Escala, Long> {
    List<Escala> findByProfissionalId(Long profissionalId);
    List<Escala> findByProfissionalIdAndDiaSemanaAndAtivoTrue(Long profissionalId, DayOfWeek diaSemana);

    // Escopo por clínica (via o dono da escala, o profissional)
    List<Escala> findByProfissionalIdAndProfissionalClinicaId(Long profissionalId, Long clinicaId);
    Optional<Escala> findByIdAndProfissionalIdAndProfissionalClinicaId(Long id, Long profissionalId, Long clinicaId);
}