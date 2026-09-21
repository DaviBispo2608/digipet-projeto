package com.digipet.care.controller;

import com.digipet.care.service.EscalaService;
import com.digipet.care.web.EscalaRequest;
import com.digipet.care.web.EscalaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/profissionais/{profissionalId}/escalas")
public class EscalaController {

    private final EscalaService service;

    public EscalaController(EscalaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EscalaResponse>> listar(@PathVariable Long clinicaId,
            @PathVariable Long profissionalId) {
        var escalas = service.listarDoProfissional(clinicaId, profissionalId).stream()
                .map(EscalaResponse::from).toList();
        return ResponseEntity.ok(escalas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscalaResponse> buscar(@PathVariable Long clinicaId, @PathVariable Long profissionalId,
            @PathVariable Long id) {
        return ResponseEntity.ok(EscalaResponse.from(service.buscarPorId(clinicaId, profissionalId, id)));
    }

    @PostMapping
    public ResponseEntity<EscalaResponse> criar(@PathVariable Long clinicaId, @PathVariable Long profissionalId,
            @Valid @RequestBody EscalaRequest request) {
        var escala = service.criar(clinicaId, profissionalId, request);
        return ResponseEntity.created(URI.create("/api/v1/clinicas/" + clinicaId + "/profissionais/"
                        + profissionalId + "/escalas/" + escala.getId()))
                .body(EscalaResponse.from(escala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscalaResponse> atualizar(@PathVariable Long clinicaId, @PathVariable Long profissionalId,
            @PathVariable Long id, @Valid @RequestBody EscalaRequest request) {
        return ResponseEntity.ok(EscalaResponse.from(service.atualizar(clinicaId, profissionalId, id, request)));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<EscalaResponse> inativar(@PathVariable Long clinicaId, @PathVariable Long profissionalId,
            @PathVariable Long id) {
        return ResponseEntity.ok(EscalaResponse.from(service.inativar(clinicaId, profissionalId, id)));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<EscalaResponse> reativar(@PathVariable Long clinicaId, @PathVariable Long profissionalId,
            @PathVariable Long id) {
        return ResponseEntity.ok(EscalaResponse.from(service.reativar(clinicaId, profissionalId, id)));
    }
}
