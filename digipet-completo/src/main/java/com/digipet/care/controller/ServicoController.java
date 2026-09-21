package com.digipet.care.controller;

import com.digipet.care.service.ServicoService;
import com.digipet.care.web.ServicoRequest;
import com.digipet.care.web.ServicoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listar(@PathVariable Long clinicaId,
            @RequestParam(defaultValue = "false") boolean apenasAtivos) {
        var servicos = service.listarDaClinica(clinicaId, apenasAtivos).stream().map(ServicoResponse::from).toList();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(ServicoResponse.from(service.buscarPorId(clinicaId, id)));
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> criar(@PathVariable Long clinicaId,
            @Valid @RequestBody ServicoRequest request) {
        var servico = service.criar(clinicaId, request);
        return ResponseEntity.created(
                        URI.create("/api/v1/clinicas/" + clinicaId + "/servicos/" + servico.getId()))
                .body(ServicoResponse.from(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> atualizar(@PathVariable Long clinicaId, @PathVariable Long id,
            @Valid @RequestBody ServicoRequest request) {
        return ResponseEntity.ok(ServicoResponse.from(service.atualizar(clinicaId, id, request)));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<ServicoResponse> inativar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(ServicoResponse.from(service.inativar(clinicaId, id)));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<ServicoResponse> reativar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(ServicoResponse.from(service.reativar(clinicaId, id)));
    }
}
