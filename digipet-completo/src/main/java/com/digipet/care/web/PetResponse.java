package com.digipet.care.web;

import com.digipet.care.domain.Pet;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PetResponse(
        Long id,
        Long donoId,
        String nome,
        String especie,
        String raca,
        BigDecimal peso,
        LocalDate nascimento) {

    public static PetResponse from(Pet pet) {
        return new PetResponse(pet.getId(), pet.getDono().getId(), pet.getNome(), pet.getEspecie(),
                pet.getRaca(), pet.getPeso(), pet.getNascimento());
    }
}
