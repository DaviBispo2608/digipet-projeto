package com.digipet.care.controller;

import com.digipet.care.service.DonoService;
import com.digipet.care.web.DonoRequest;
import com.digipet.care.web.DonoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/donos")
public class DonoController {

    private final DonoService service;

    public DonoController(DonoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DonoResponse>> listar(@PathVariable Long clinicaId) {
        var donos = service.listarDaClinica(clinicaId).stream().map(DonoResponse::from).toList();
        return ResponseEntity.ok(donos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonoResponse> buscar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(DonoResponse.from(service.buscarPorId(clinicaId, id)));
    }

    @PostMapping
    public ResponseEntity<DonoResponse> criar(@PathVariable Long clinicaId, @Valid @RequestBody DonoRequest request) {
        var dono = service.criar(clinicaId, request);
        return ResponseEntity.created(URI.create("/api/v1/clinicas/" + clinicaId + "/donos/" + dono.getId()))
                .body(DonoResponse.from(dono));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonoResponse> atualizar(@PathVariable Long clinicaId, @PathVariable Long id,
            @Valid @RequestBody DonoRequest request) {
        return ResponseEntity.ok(DonoResponse.from(service.atualizar(clinicaId, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long clinicaId, @PathVariable Long id) {
        service.remover(clinicaId, id);
        return ResponseEntity.noContent().build();
    }
}
