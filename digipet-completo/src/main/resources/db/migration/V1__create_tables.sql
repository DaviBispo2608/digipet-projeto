CREATE TABLE clinica (
    id BIGSERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    nome_fantasia VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    endereco VARCHAR(255), telefone VARCHAR(20), email VARCHAR(150)
);

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    clinica_id BIGINT REFERENCES clinica(id),
    nome_completo VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(30) NOT NULL,
    crmv VARCHAR(30), ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE dono (
    id BIGSERIAL PRIMARY KEY,
    clinica_id BIGINT NOT NULL REFERENCES clinica(id),
    nome VARCHAR(150) NOT NULL, email VARCHAR(150) NOT NULL,
    telefone VARCHAR(20), endereco VARCHAR(255),
    CONSTRAINT uk_dono_clinica_email UNIQUE (clinica_id, email)
);

CREATE TABLE pet (
    id BIGSERIAL PRIMARY KEY,
    dono_id BIGINT NOT NULL REFERENCES dono(id),
    nome VARCHAR(150) NOT NULL, especie VARCHAR(80), raca VARCHAR(100),
    peso NUMERIC(7,2), nascimento DATE
);

CREATE TABLE servico (
    id BIGSERIAL PRIMARY KEY,
    clinica_id BIGINT NOT NULL REFERENCES clinica(id),
    nome VARCHAR(150) NOT NULL, descricao TEXT, tipo VARCHAR(50) NOT NULL,
    duracao_minutos INTEGER NOT NULL CHECK (duracao_minutos > 0),
    preco_base NUMERIC(10,2), ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE escala (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    dia_semana VARCHAR(15) NOT NULL, hora_inicio TIME NOT NULL, hora_fim TIME NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_escala_horario CHECK (hora_inicio < hora_fim)
);

CREATE TABLE prof_serv (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    servico_id BIGINT NOT NULL REFERENCES servico(id),
    habilitado BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_prof_serv UNIQUE (usuario_id, servico_id)
);

CREATE TABLE agendamento (
    id BIGSERIAL PRIMARY KEY,
    clinica_id BIGINT NOT NULL REFERENCES clinica(id),
    pet_id BIGINT NOT NULL REFERENCES pet(id),
    servico_id BIGINT NOT NULL REFERENCES servico(id),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    data_hora TIMESTAMP NOT NULL, data_hora_fim TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL, observacoes VARCHAR(1000),
    CONSTRAINT ck_agendamento_periodo CHECK (data_hora < data_hora_fim)
);

CREATE INDEX idx_agendamento_clinica_profissional_horario
    ON agendamento (clinica_id, usuario_id, data_hora);
