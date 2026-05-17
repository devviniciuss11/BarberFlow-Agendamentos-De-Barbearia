DO $$
BEGIN
    IF to_regclass('public.horarios_disponiveis') IS NULL THEN
        IF to_regclass('public.horariosdisponiveis') IS NOT NULL THEN
            EXECUTE 'ALTER TABLE public.horariosdisponiveis RENAME TO horarios_disponiveis';
        ELSIF to_regclass('public."horariosDisponiveis"') IS NOT NULL THEN
            EXECUTE 'ALTER TABLE public."horariosDisponiveis" RENAME TO horarios_disponiveis';
        ELSE
            CREATE TABLE public.horarios_disponiveis (
                id BIGSERIAL PRIMARY KEY,
                data DATE NOT NULL,
                hora TIME NOT NULL,
                disponivel BOOLEAN NOT NULL DEFAULT TRUE,
                barbeiro_id BIGINT NOT NULL
            );
        END IF;
    END IF;
END $$;

ALTER TABLE horarios_disponiveis
    ADD COLUMN IF NOT EXISTS data DATE;

ALTER TABLE horarios_disponiveis
    ADD COLUMN IF NOT EXISTS hora TIME;

ALTER TABLE horarios_disponiveis
    ADD COLUMN IF NOT EXISTS disponivel BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE horarios_disponiveis
    ADD COLUMN IF NOT EXISTS barbeiro_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_horario_barbeiro_data_hora'
          AND conrelid = 'horarios_disponiveis'::regclass
    ) THEN
        ALTER TABLE horarios_disponiveis
            ADD CONSTRAINT uk_horario_barbeiro_data_hora
                UNIQUE (barbeiro_id, data, hora);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_horario_barbeiro'
          AND conrelid = 'horarios_disponiveis'::regclass
    ) THEN
        ALTER TABLE horarios_disponiveis
            ADD CONSTRAINT fk_horario_barbeiro
                FOREIGN KEY (barbeiro_id)
                REFERENCES barbeiro(id);
    END IF;
END $$;

ALTER TABLE agendamento
    ADD COLUMN IF NOT EXISTS barbeiro_id BIGINT;

ALTER TABLE agendamento
    ADD COLUMN IF NOT EXISTS cancelado BOOLEAN NOT NULL DEFAULT FALSE;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'agendamento'
          AND column_name = 'status'
          AND data_type <> 'boolean'
    ) THEN
        UPDATE agendamento
        SET cancelado = TRUE
        WHERE status::text = 'CANCELADO';

        ALTER TABLE agendamento
            DROP CONSTRAINT IF EXISTS chk_status_agendamento;

        ALTER TABLE agendamento
            DROP COLUMN IF EXISTS servico;

        ALTER TABLE agendamento
            ALTER COLUMN status TYPE BOOLEAN
            USING CASE
                    WHEN status::text = 'CONCLUIDO' THEN TRUE
                    ELSE FALSE
                  END;
    ELSE
        ALTER TABLE agendamento
            DROP COLUMN IF EXISTS servico;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_agendamento_barbeiro'
          AND conrelid = 'agendamento'::regclass
    ) THEN
        ALTER TABLE agendamento
            ADD CONSTRAINT fk_agendamento_barbeiro
                FOREIGN KEY (barbeiro_id)
                REFERENCES barbeiro(id)
                ON DELETE CASCADE;
    END IF;
END $$;

UPDATE agendamento
SET cancelado = FALSE
WHERE cancelado IS NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM agendamento WHERE barbeiro_id IS NULL
    ) THEN
        ALTER TABLE agendamento
            ALTER COLUMN barbeiro_id SET NOT NULL;
    END IF;
END $$;

ALTER TABLE agendamento
    ALTER COLUMN cancelado SET NOT NULL;
