package com.digipet.care.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import com.digipet.care.enums.TipoServico;

@Entity
@Table(name = "servico")
@Getter @Setter @NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Long clinicaId;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoServico tipo;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "preco_base")
    private BigDecimal precoBase;

    private boolean ativo = true;
}
