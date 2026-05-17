CREATE TABLE horarios_disponiveis (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    hora TIME NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    barbeiro_id BIGINT NOT NULL,
    CONSTRAINT fk_horario_barbeiro FOREIGN KEY (barbeiro_id) REFERENCES barbeiro(id),
    CONSTRAINT uk_horario_barbeiro_data_hora UNIQUE (barbeiro_id, data, hora)
);