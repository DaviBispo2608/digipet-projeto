package com.digipet.care.repository;

import com.digipet.care.domain.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
    boolean existsByCnpj(String cnpj);
}
