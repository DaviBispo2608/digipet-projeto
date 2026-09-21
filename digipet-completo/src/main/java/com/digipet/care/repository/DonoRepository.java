package com.digipet.care.repository;

import com.digipet.care.domain.Dono;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DonoRepository extends JpaRepository<Dono, Long> {
    Optional<Dono> findByEmail(String email);

    List<Dono> findAllByClinicaId(Long clinicaId);

    Optional<Dono> findByIdAndClinicaId(Long id, Long clinicaId);

    boolean existsByClinicaIdAndEmail(Long clinicaId, String email);
}