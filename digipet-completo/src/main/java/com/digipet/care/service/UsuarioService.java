package com.digipet.care.service;

import com.digipet.care.domain.Clinica;
import com.digipet.care.domain.Usuario;
import com.digipet.care.exception.BusinessException;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.ClinicaRepository;
import com.digipet.care.repository.UsuarioRepository;
import com.digipet.care.web.UsuarioRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final ClinicaRepository clinicaRepository;

    public UsuarioService(UsuarioRepository repository, ClinicaRepository clinicaRepository) {
        this.repository = repository;
        this.clinicaRepository = clinicaRepository;
    }

    public List<Usuario> listarDaClinica(Long clinicaId) {
        return repository.findAllByClinicaId(clinicaId);
    }

    public Usuario buscarPorId(Long clinicaId, Long id) {
        return repository.findByIdAndClinicaId(id, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para esta clínica."));
    }

    @Transactional
    public Usuario criar(Long clinicaId, UsuarioRequest request) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Clínica não encontrada."));
        if (repository.existsByEmail(request.email())) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }
        Usuario usuario = new Usuario();
        usuario.setClinica(clinica);
        usuario.setNomeCompleto(request.nomeCompleto());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(hash(request.senha()));
        usuario.setPerfil(request.perfil());
        usuario.setCrmv(request.crmv());
        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long clinicaId, Long id, UsuarioRequest request) {
        Usuario usuario = buscarPorId(clinicaId, id);
        if (!usuario.getEmail().equals(request.email()) && repository.existsByEmail(request.email())) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }
        usuario.setNomeCompleto(request.nomeCompleto());
        usuario.setEmail(request.email());
        usuario.setSenhaHash(hash(request.senha()));
        usuario.setPerfil(request.perfil());
        usuario.setCrmv(request.crmv());
        return repository.save(usuario);
    }

    @Transactional
    public Usuario inativar(Long clinicaId, Long id) {
        Usuario usuario = buscarPorId(clinicaId, id);
        usuario.setAtivo(false);
        return repository.save(usuario);
    }

    @Transactional
    public Usuario reativar(Long clinicaId, Long id) {
        Usuario usuario = buscarPorId(clinicaId, id);
        usuario.setAtivo(true);
        return repository.save(usuario);
    }

    // NOTA: em produção o hash de senha deve usar BCrypt/Argon2 (spring-security-crypto).
    // Como a dependência de segurança ainda não foi incluída no projeto, usamos SHA-256
    // apenas para não persistir senha em texto puro neste MVP.
    private String hash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo de hash indisponível.", e);
        }
    }
}
