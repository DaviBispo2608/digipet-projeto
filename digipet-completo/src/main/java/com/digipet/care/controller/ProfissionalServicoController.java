package com.digipet.care.controller;

import com.digipet.care.service.ProfissionalServicoService;
import com.digipet.care.web.ProfissionalServicoRequest;
import com.digipet.care.web.ProfissionalServicoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/profissionais/{profissionalId}/servicos")
public class ProfissionalServicoController {

    private final ProfissionalServicoService service;

    public ProfissionalServicoController(ProfissionalServicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalServicoResponse>> listar(@PathVariable Long clinicaId,
            @PathVariable Long profissionalId) {
        var vinculos = service.listarDoProfissional(clinicaId, profissionalId).stream()
                .map(ProfissionalServicoResponse::from).toList();
        return ResponseEntity.ok(vinculos);
    }

    @PostMapping
    public ResponseEntity<ProfissionalServicoResponse> habilitar(@PathVariable Long clinicaId,
            @PathVariable Long profissionalId, @Valid @RequestBody ProfissionalServicoRequest request) {
        var vinculo = service.habilitar(clinicaId, profissionalId, request.servicoId());
        return ResponseEntity.status(201).body(ProfissionalServicoResponse.from(vinculo));
    }

    @DeleteMapping("/{servicoId}")
    public ResponseEntity<Void> desabilitar(@PathVariable Long clinicaId, @PathVariable Long profissionalId,
            @PathVariable Long servicoId) {
        service.desabilitar(clinicaId, profissionalId, servicoId);
        return ResponseEntity.noContent().build();
    }
}
