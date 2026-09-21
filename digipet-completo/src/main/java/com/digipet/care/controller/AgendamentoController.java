package com.digipet.care.controller;

import com.digipet.care.service.AgendamentoService;
import com.digipet.care.web.AgendamentoRequest;
import com.digipet.care.web.AgendamentoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clinicas/{clinicaId}/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listar(@PathVariable Long clinicaId,
            @RequestParam(required = false) Long profissionalId) {
        var agendamentos = profissionalId != null
                ? service.listarAgendamentosDoProfissional(clinicaId, profissionalId)
                : service.listarAgendamentosDaClinica(clinicaId);
        return ResponseEntity.ok(agendamentos.stream().map(AgendamentoResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(service.buscarPorId(clinicaId, id)));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@PathVariable Long clinicaId,
            @Valid @RequestBody AgendamentoRequest agendamento) {
        var novo = service.criarAgendamento(agendamento, clinicaId);
        return ResponseEntity.created(URI.create("/api/v1/clinicas/" + clinicaId + "/agendamentos/" + novo.getId()))
                .body(AgendamentoResponse.from(novo));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(service.confirmar(clinicaId, id)));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(service.concluir(clinicaId, id)));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long clinicaId, @PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponse.from(service.cancelar(clinicaId, id)));
    }
}
