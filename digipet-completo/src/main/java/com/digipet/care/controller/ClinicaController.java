package com.digipet.care.controller;

import com.digipet.care.service.ClinicaService;
import com.digipet.care.web.ClinicaRequest;
import com.digipet.care.web.ClinicaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas")
public class ClinicaController {

    private final ClinicaService service;

    public ClinicaController(ClinicaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClinicaResponse>> listar() {
        List<ClinicaResponse> clinicas = service.listar().stream().map(ClinicaResponse::from).toList();
        return ResponseEntity.ok(clinicas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ClinicaResponse.from(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ClinicaResponse> criar(@Valid @RequestBody ClinicaRequest request) {
        var clinica = service.criar(request);
        return ResponseEntity.created(URI.create("/api/v1/clinicas/" + clinica.getId()))
                .body(ClinicaResponse.from(clinica));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClinicaRequest request) {
        return ResponseEntity.ok(ClinicaResponse.from(service.atualizar(id, request)));
    }
}
