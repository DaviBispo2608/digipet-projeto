package com.digipet.care.repository;

import com.digipet.care.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findAllByClinicaId(Long clinicaId);

    Optional<Usuario> findByIdAndClinicaId(Long id, Long clinicaId);
}
