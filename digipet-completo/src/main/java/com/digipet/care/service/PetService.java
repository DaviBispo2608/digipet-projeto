package com.digipet.care.service;

import com.digipet.care.domain.Dono;
import com.digipet.care.domain.Pet;
import com.digipet.care.exception.ResourceNotFoundException;
import com.digipet.care.repository.DonoRepository;
import com.digipet.care.repository.PetRepository;
import com.digipet.care.web.PetRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository repository;
    private final DonoRepository donoRepository;

    public PetService(PetRepository repository, DonoRepository donoRepository) {
        this.repository = repository;
        this.donoRepository = donoRepository;
    }

    public List<Pet> listarDoDono(Long clinicaId, Long donoId) {
        // Garante que o dono existe e pertence à clínica antes de listar os pets
        buscarDono(clinicaId, donoId);
        return repository.findByDonoIdAndDonoClinicaId(donoId, clinicaId);
    }

    public Pet buscarPorId(Long clinicaId, Long donoId, Long id) {
        return repository.findByIdAndDonoIdAndDonoClinicaId(id, donoId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado para este dono."));
    }

    @Transactional
    public Pet criar(Long clinicaId, Long donoId, PetRequest request) {
        Dono dono = buscarDono(clinicaId, donoId);
        Pet pet = new Pet();
        pet.setDono(dono);
        aplicar(pet, request);
        return repository.save(pet);
    }

    @Transactional
    public Pet atualizar(Long clinicaId, Long donoId, Long id, PetRequest request) {
        Pet pet = buscarPorId(clinicaId, donoId, id);
        aplicar(pet, request);
        return repository.save(pet);
    }

    @Transactional
    public void remover(Long clinicaId, Long donoId, Long id) {
        Pet pet = buscarPorId(clinicaId, donoId, id);
        repository.delete(pet);
    }

    private Dono buscarDono(Long clinicaId, Long donoId) {
        return donoRepository.findByIdAndClinicaId(donoId, clinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Dono não encontrado para esta clínica."));
    }

    private void aplicar(Pet pet, PetRequest request) {
        pet.setNome(request.nome());
        pet.setEspecie(request.especie());
        pet.setRaca(request.raca());
        pet.setPeso(request.peso());
        pet.setNascimento(request.nascimento());
    }
}
