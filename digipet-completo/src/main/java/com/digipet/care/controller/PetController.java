package com.digipet.care.controller;

import com.digipet.care.service.PetService;
import com.digipet.care.web.PetRequest;
import com.digipet.care.web.PetResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/donos/{donoId}/pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> listar(@PathVariable Long clinicaId, @PathVariable Long donoId) {
        var pets = service.listarDoDono(clinicaId, donoId).stream().map(PetResponse::from).toList();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> buscar(@PathVariable Long clinicaId, @PathVariable Long donoId,
            @PathVariable Long id) {
        return ResponseEntity.ok(PetResponse.from(service.buscarPorId(clinicaId, donoId, id)));
    }

    @PostMapping
    public ResponseEntity<PetResponse> criar(@PathVariable Long clinicaId, @PathVariable Long donoId,
            @Valid @RequestBody PetRequest request) {
        var pet = service.criar(clinicaId, donoId, request);
        return ResponseEntity.created(
                        URI.create("/api/v1/clinicas/" + clinicaId + "/donos/" + donoId + "/pets/" + pet.getId()))
                .body(PetResponse.from(pet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> atualizar(@PathVariable Long clinicaId, @PathVariable Long donoId,
            @PathVariable Long id, @Valid @RequestBody PetRequest request) {
        return ResponseEntity.ok(PetResponse.from(service.atualizar(clinicaId, donoId, id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long clinicaId, @PathVariable Long donoId,
            @PathVariable Long id) {
        service.remover(clinicaId, donoId, id);
        return ResponseEntity.noContent().build();
    }
}
