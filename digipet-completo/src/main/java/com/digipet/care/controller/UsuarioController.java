package com.digipet.care.controller;

import com.digipet.care.service.UsuarioService;
import com.digipet.care.web.UsuarioRequest;
import com.digipet.care.web.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar(@PathVariable Long clinicaId) {
        var usuarios = service.listarDaClinica(clinicaId).stream().map(UsuarioResponse::from).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(service.buscarPorId(clinicaId, id)));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@PathVariable Long clinicaId,
            @Valid @RequestBody UsuarioRequest request) {
        var usuario = service.criar(clinicaId, request);
        return ResponseEntity.created(
                        URI.create("/api/v1/clinicas/" + clinicaId + "/usuarios/" + usuario.getId()))
                .body(UsuarioResponse.from(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long clinicaId, @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(UsuarioResponse.from(service.atualizar(clinicaId, id, request)));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<UsuarioResponse> inativar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(service.inativar(clinicaId, id)));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<UsuarioResponse> reativar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(service.reativar(clinicaId, id)));
    }
}
