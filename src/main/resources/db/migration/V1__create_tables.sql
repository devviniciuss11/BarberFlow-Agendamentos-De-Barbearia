CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(13) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    agendamentopoints INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE barbeiro (
    id BIGSERIAL PRIMARY KEY,
    especialidade VARCHAR(100) NOT NULL,
    telefone VARCHAR(15) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE agendamento (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    barbeiro_id BIGINT NOT NULL,
    data DATE NOT NULL,
    horario TIME NOT NULL,
    status BOOLEAN NOT NULL DEFAULT FALSE,
    cancelado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_agendamento_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_agendamento_barbeiro
        FOREIGN KEY (barbeiro_id)
        REFERENCES barbeiro(id)
        ON DELETE CASCADE
);

CREATE TABLE horarios_disponiveis (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    hora TIME NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    barbeiro_id BIGINT NOT NULL,
    CONSTRAINT fk_horario_barbeiro
        FOREIGN KEY (barbeiro_id)
        REFERENCES barbeiro(id),
    CONSTRAINT uk_horario_barbeiro_data_hora
        UNIQUE (barbeiro_id, data, hora)
);
