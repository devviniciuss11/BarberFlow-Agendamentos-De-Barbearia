ALTER TABLE agendamento
    ADD COLUMN barbeiro_id BIGINT;

ALTER TABLE agendamento
    ADD COLUMN cancelado BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE agendamento
SET cancelado = TRUE
WHERE status = 'CANCELADO';

ALTER TABLE agendamento
DROP CONSTRAINT chk_status_agendamento;

ALTER TABLE agendamento
DROP COLUMN IF EXISTS servico;

ALTER TABLE agendamento
ALTER COLUMN status TYPE BOOLEAN
        USING CASE
                  WHEN status = 'CONCLUIDO' THEN TRUE
                  ELSE FALSE
END;

ALTER TABLE agendamento
    ADD CONSTRAINT fk_agendamento_barbeiro
        FOREIGN KEY (barbeiro_id)
            REFERENCES barbeiro(id)
            ON DELETE CASCADE;

ALTER TABLE agendamento
    ALTER COLUMN barbeiro_id SET NOT NULL;
