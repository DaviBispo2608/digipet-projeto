package com.digipet.care.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "clinica")
@Getter @Setter @NoArgsConstructor
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(unique = true, nullable = false)
    private String cnpj;

    private String endereco;
    private String telefone;
    private String email;

    // Relacionamento 1:N (Uma clínica tem vários usuários)
    @OneToMany(mappedBy = "clinica")
    private List<Usuario> usuarios;
}
