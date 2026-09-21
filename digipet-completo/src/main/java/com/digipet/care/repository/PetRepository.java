package com.digipet.care.repository;

import com.digipet.care.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByDonoId(Long donoId);
    boolean existsByIdAndDonoClinicaId(Long id, Long clinicaId);

    // Escopo duplo: o pet precisa pertencer ao dono E o dono precisa pertencer à clínica logada
    List<Pet> findByDonoIdAndDonoClinicaId(Long donoId, Long clinicaId);
    Optional<Pet> findByIdAndDonoIdAndDonoClinicaId(Long id, Long donoId, Long clinicaId);
}
