package com.digipet.care.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pet")
@Getter @Setter @NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Conexão obrigatória com o Dono do Pet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono_id", nullable = false)
    private Dono dono;

    @Column(nullable = false)
    private String nome;

    private String especie;
    private String raca;
    @Column(precision = 7, scale = 2)
    private java.math.BigDecimal peso;
    private LocalDate nascimento;
}
