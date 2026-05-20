const API_BASE = window.location.protocol.startsWith("http")
    ? window.location.origin
    : "http://localhost:8080";

const feedbackEl = document.getElementById("api-feedback");
const rankingEl = document.getElementById("ranking-lista");
const clientesListEl = document.getElementById("lista-clientes-output");
const clientesBuscaEl = document.getElementById("busca-clientes-output");
const clienteIdEl = document.getElementById("cliente-id-output");
const barbeirosListEl = document.getElementById("lista-barbeiros-output");
const barbeiroIdEl = document.getElementById("barbeiro-id-output");
const horariosListEl = document.getElementById("lista-horarios-output");
const agendamentosListEl = document.getElementById("lista-agendamentos-output");
const clientesListMetaEl = document.getElementById("lista-clientes-meta");
const clientesBuscaMetaEl = document.getElementById("busca-clientes-meta");
const clienteIdMetaEl = document.getElementById("cliente-id-meta");
const barbeirosListMetaEl = document.getElementById("lista-barbeiros-meta");
const barbeiroIdMetaEl = document.getElementById("barbeiro-id-meta");
const horariosListMetaEl = document.getElementById("lista-horarios-meta");
const agendamentosListMetaEl = document.getElementById("lista-agendamentos-meta");

function showFeedback(message, type = "success") {
    if (!feedbackEl) {
        return;
    }

    feedbackEl.textContent = message;
    feedbackEl.className = `api-feedback ${type}`;
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
}

function renderMeta(metaEl, count, label) {
    if (!metaEl) {
        return;
    }

    const plural = count === 1 ? label : `${label}s`;
    metaEl.innerHTML = `<span class="result-count">${count}</span> ${escapeHtml(plural)}`;
}

function quickFillButton(kind, payload, label) {
    const attrs = Object.entries(payload)
        .map(([key, value]) => `data-${key}="${escapeHtml(value)}"`)
        .join(" ");

    return `<button type="button" class="quick-fill-btn" data-fill-kind="${escapeHtml(kind)}" ${attrs}>${escapeHtml(label)}</button>`;
}

function statusChip(text, active) {
    const className = active ? "status-chip success" : "status-chip neutral";
    return `<span class="${className}">${escapeHtml(text)}</span>`;
}

function renderList(container, items, mapper, emptyMessage, metaEl, metaLabel = "registro") {
    if (!container) {
        return;
    }

    if (!Array.isArray(items) || items.length === 0) {
        renderMeta(metaEl, 0, metaLabel);
        container.innerHTML = `<p class="list-empty">${escapeHtml(emptyMessage)}</p>`;
        return;
    }

    renderMeta(metaEl, items.length, metaLabel);

    const htmlItems = items
        .map((item, index) => `<li>${mapper(item, index)}</li>`)
        .join("");

    container.innerHTML = `<ul class="api-list">${htmlItems}</ul>`;
}

function formatCliente(cliente) {
    const actions = `
        <div class="quick-actions">
            ${quickFillButton("clienteAtualizar", {
        id: cliente.id ?? "",
        nome: cliente.nome ?? "",
        telefone: cliente.telefone ?? ""
    }, "Preencher Atualizar")}
            ${quickFillButton("clienteRemover", { id: cliente.id ?? "" }, "Preencher Remover")}
        </div>
    `;

    return `
        <div class="entity-card">
            <div class="entity-title">${escapeHtml(`#${cliente.id ?? "-"} - ${cliente.nome ?? "Sem nome"}`)}</div>
            <div class="entity-grid">
                <span><b>Telefone:</b> ${escapeHtml(cliente.telefone ?? "-")}</span>
                <span><b>Pontos:</b> ${escapeHtml(cliente.agendamentoPoints ?? 0)}</span>
            </div>
            ${actions}
        </div>
    `;
}

function formatBarbeiro(barbeiro) {
    const actions = `
        <div class="quick-actions">
            ${quickFillButton("barbeiroAtualizar", {
        id: barbeiro.id ?? "",
        nome: barbeiro.nome ?? "",
        especialidade: barbeiro.especialidade ?? "",
        telefone: barbeiro.telefone ?? "",
        cpf: barbeiro.cpf ?? ""
    }, "Preencher Atualizar")}
            ${quickFillButton("barbeiroRemover", { id: barbeiro.id ?? "" }, "Preencher Remover")}
        </div>
    `;

    return `
        <div class="entity-card">
            <div class="entity-title">${escapeHtml(`#${barbeiro.id ?? "-"} - ${barbeiro.nome ?? "Sem nome"}`)}</div>
            <div class="entity-grid">
                <span><b>Especialidade:</b> ${escapeHtml(barbeiro.especialidade ?? "-")}</span>
                <span><b>Telefone:</b> ${escapeHtml(barbeiro.telefone ?? "-")}</span>
                <span><b>CPF:</b> ${escapeHtml(barbeiro.cpf ?? "-")}</span>
            </div>
            ${actions}
        </div>
    `;
}

function formatHorario(horario) {
    return `
        <div class="entity-card">
            <div class="entity-title">${escapeHtml(`#${horario.id ?? "-"}`)} - ${escapeHtml(horario.barbeiro ?? "-")}</div>
            <div class="entity-grid">
                <span><b>Data:</b> ${escapeHtml(horario.data ?? "-")}</span>
                <span><b>Hora:</b> ${escapeHtml(horario.hora ?? "-")}</span>
                <span>${statusChip(horario.disponivel ? "Disponivel" : "Indisponivel", !!horario.disponivel)}</span>
            </div>
        </div>
    `;
}

function formatAgendamento(agendamento) {
    const actions = `
        <div class="quick-actions">
            ${quickFillButton("agendamentoConcluir", { id: agendamento.id ?? "" }, "Usar em Concluir")}
            ${quickFillButton("agendamentoCancelarCliente", { id: agendamento.id ?? "" }, "Usar em Cancelar Cliente")}
            ${quickFillButton("agendamentoCancelarBarbeiro", { id: agendamento.id ?? "" }, "Usar em Cancelar Barbeiro")}
        </div>
    `;

    return `
        <div class="entity-card">
            <div class="entity-title">${escapeHtml(`#${agendamento.id ?? "-"}`)} - ${escapeHtml(agendamento.clienteNome ?? "-")}</div>
            <div class="entity-grid">
                <span><b>Cliente:</b> ${escapeHtml(agendamento.clienteTelefone ?? "-")}</span>
                <span><b>Barbeiro:</b> ${escapeHtml(agendamento.barbeiroNome ?? "-")} (${escapeHtml(agendamento.barbeiroTelefone ?? "-")})</span>
                <span><b>Horario:</b> ${escapeHtml(agendamento.data ?? "-")} ${escapeHtml(agendamento.hora ?? "-")}</span>
                <span>${statusChip(agendamento.status ? "Concluido" : "Pendente", !!agendamento.status)}</span>
                <span>${statusChip(agendamento.cancelado ? "Cancelado" : "Ativo", !agendamento.cancelado)}</span>
            </div>
            ${actions}
        </div>
    `;
}

function readValue(form, name, required = true) {
    const input = form.elements[name];
    const value = input ? String(input.value).trim() : "";

    if (required && !value) {
        throw new Error(`Preencha o campo: ${name}`);
    }

    return value;
}

function setFormValue(formId, fieldName, value) {
    const form = document.getElementById(formId);
    if (!form || !form.elements[fieldName]) {
        return;
    }

    form.elements[fieldName].value = value ?? "";
}

function toNumber(value, fieldName) {
    const parsed = Number(value);

    if (!Number.isInteger(parsed) || parsed <= 0) {
        throw new Error(`${fieldName} deve ser um numero inteiro positivo.`);
    }

    return parsed;
}

function normalizeTime(timeValue) {
    if (!timeValue) {
        return timeValue;
    }

    return /^\d{2}:\d{2}$/.test(timeValue) ? `${timeValue}:00` : timeValue;
}

function extractErrorMessage(payload, status) {
    if (typeof payload === "string" && payload.trim()) {
        return payload;
    }

    if (payload && typeof payload === "object") {
        if (payload.message) {
            return payload.message;
        }

        if (payload.detail) {
            return payload.detail;
        }

        if (payload.error) {
            return payload.error;
        }

        if (Array.isArray(payload.errors) && payload.errors.length > 0) {
            return payload.errors.join(" | ");
        }
    }

    return `Erro na requisicao (HTTP ${status}).`;
}

async function request(path, { method = "GET", body, query } = {}) {
    const url = new URL(`${API_BASE}${path}`);

    if (query) {
        Object.entries(query).forEach(([key, value]) => {
            if (value !== undefined && value !== null && value !== "") {
                url.searchParams.set(key, String(value));
            }
        });
    }

    const options = { method, headers: {} };

    if (body !== undefined) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(body);
    }

    const response = await fetch(url, options);
    const contentType = response.headers.get("content-type") || "";

    let payload = null;
    if (response.status !== 204) {
        payload = contentType.includes("application/json")
            ? await response.json()
            : await response.text();
    }

    if (!response.ok) {
        throw new Error(extractErrorMessage(payload, response.status));
    }

    return payload;
}

function bindForm(formId, handler) {
    const form = document.getElementById(formId);
    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        showFeedback("Processando...", "info");

        try {
            await handler(form);
        } catch (error) {
            showFeedback(error.message || "Erro inesperado.", "error");
        }
    });
}

function renderRanking(clientes) {
    if (!rankingEl) {
        return;
    }

    rankingEl.innerHTML = "";

    if (!Array.isArray(clientes) || clientes.length === 0) {
        rankingEl.innerHTML = "<li><span>Nenhum cliente encontrado</span><span class='points'>0 pts</span></li>";
        return;
    }

    const ordenados = [...clientes].sort((a, b) => (b.agendamentoPoints || 0) - (a.agendamentoPoints || 0));

    ordenados.forEach((cliente, index) => {
        const item = document.createElement("li");
        item.innerHTML = `<span>${index + 1}o ${cliente.nome || "Cliente"}</span><span class="points">${cliente.agendamentoPoints || 0} pts</span>`;
        rankingEl.appendChild(item);
    });
}

async function atualizarRanking() {
    const clientes = await request("/clientes");
    renderRanking(clientes);
    return clientes;
}

async function atualizarListaClientes() {
    const clientes = await request("/clientes");
    renderList(clientesListEl, clientes, formatCliente, "Nenhum cliente cadastrado.", clientesListMetaEl, "cliente");
    return clientes;
}

async function atualizarListaBarbeiros() {
    const barbeiros = await request("/barbeiros");
    renderList(barbeirosListEl, barbeiros, formatBarbeiro, "Nenhum barbeiro cadastrado.", barbeirosListMetaEl, "barbeiro");
    return barbeiros;
}

bindForm("form-cliente-criar", async (form) => {
    const payload = {
        nome: readValue(form, "nome"),
        telefone: readValue(form, "telefone"),
        senha: readValue(form, "senha")
    };

    const response = await request("/clientes", { method: "POST", body: payload });
    showFeedback("Cliente cadastrado com sucesso.", "success");
    form.reset();
    const clientes = await atualizarListaClientes();
    renderRanking(clientes);
});

bindForm("form-cliente-listar", async () => {
    const response = await atualizarListaClientes();
    showFeedback("Lista de clientes carregada.", "success");
    renderRanking(response);
});

bindForm("form-cliente-buscar-id", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do cliente");

    try {
        const response = await request(`/clientes/${id}`);
        showFeedback("Cliente encontrado.", "success");
        renderList(clienteIdEl, [response], formatCliente, "Cliente nao encontrado.", clienteIdMetaEl, "cliente");
    } catch (error) {
        renderList(clienteIdEl, [], formatCliente, "Nenhum cliente encontrado para o ID informado.", clienteIdMetaEl, "cliente");
        throw error;
    }
});

bindForm("form-cliente-busca", async (form) => {
    const busca = readValue(form, "busca");

    try {
        const response = await request("/clientes", { query: { busca } });
        showFeedback("Busca realizada com sucesso.", "success");
        renderList(clientesBuscaEl, response, formatCliente, "Nenhum cliente encontrado.", clientesBuscaMetaEl, "resultado");
    } catch (error) {
        renderList(clientesBuscaEl, [], formatCliente, "Nenhum cliente encontrado para essa busca.", clientesBuscaMetaEl, "resultado");
        throw error;
    }
});

bindForm("form-cliente-atualizar", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do cliente");
    const nomeDigitado = readValue(form, "nome", false);
    const telefoneDigitado = readValue(form, "telefone", false);
    const senha = readValue(form, "senha");

    let nome = nomeDigitado;
    let telefone = telefoneDigitado;

    if (!nome || !telefone) {
        const atual = await request(`/clientes/${id}`);
        nome = nome || atual.nome;
        telefone = telefone || atual.telefone;
    }

    const payload = { nome, telefone, senha };

    const response = await request(`/clientes/${id}`, {
        method: "PUT",
        body: payload
    });

    showFeedback("Cliente atualizado com sucesso.", "success");
    form.reset();
    const clientes = await atualizarListaClientes();
    renderRanking(clientes);
});

bindForm("form-cliente-remover", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do cliente");
    await request(`/clientes/${id}`, { method: "DELETE" });
    showFeedback("Cliente removido com sucesso.", "success");
    form.reset();
    const clientes = await atualizarListaClientes();
    renderRanking(clientes);
});

bindForm("form-cliente-ranking", async () => {
    const clientes = await atualizarListaClientes();
    renderRanking(clientes);
    showFeedback("Ranking atualizado.", "success");
});

bindForm("form-barbeiro-criar", async (form) => {
    const payload = {
        nome: readValue(form, "nome"),
        especialidade: readValue(form, "especialidade"),
        telefone: readValue(form, "telefone"),
        cpf: readValue(form, "cpf"),
        senha: readValue(form, "senha")
    };

    const response = await request("/barbeiros", {
        method: "POST",
        body: payload
    });

    showFeedback("Barbeiro cadastrado com sucesso.", "success");
    form.reset();
    await atualizarListaBarbeiros();
});

bindForm("form-barbeiro-listar", async () => {
    const response = await atualizarListaBarbeiros();
    showFeedback("Lista de barbeiros carregada.", "success");
});

bindForm("form-barbeiro-buscar-id", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do barbeiro");

    try {
        const response = await request(`/barbeiros/${id}`);
        showFeedback("Barbeiro encontrado.", "success");
        renderList(barbeiroIdEl, [response], formatBarbeiro, "Barbeiro nao encontrado.", barbeiroIdMetaEl, "barbeiro");
    } catch (error) {
        renderList(barbeiroIdEl, [], formatBarbeiro, "Nenhum barbeiro encontrado para o ID informado.", barbeiroIdMetaEl, "barbeiro");
        throw error;
    }
});

bindForm("form-barbeiro-atualizar", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do barbeiro");

    const payload = {
        id,
        nome: readValue(form, "nome"),
        especialidade: readValue(form, "especialidade"),
        telefone: readValue(form, "telefone"),
        cpf: readValue(form, "cpf"),
        senha: readValue(form, "senha")
    };

    const response = await request(`/barbeiros/${id}`, {
        method: "PUT",
        body: payload
    });

    showFeedback("Barbeiro atualizado com sucesso.", "success");
    form.reset();
    await atualizarListaBarbeiros();
});

bindForm("form-barbeiro-remover", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do barbeiro");
    await request(`/barbeiros/${id}`, { method: "DELETE" });
    showFeedback("Barbeiro removido com sucesso.", "success");
    form.reset();
    await atualizarListaBarbeiros();
});

bindForm("form-horario-criar", async (form) => {
    const payload = {
        barbeiroid: toNumber(readValue(form, "barbeiroid"), "ID do barbeiro"),
        data: readValue(form, "data"),
        hora: normalizeTime(readValue(form, "hora")),
        disponivel: true
    };

    const response = await request("/Horarios", {
        method: "POST",
        body: payload
    });

    showFeedback("Horario cadastrado com sucesso.", "success");
    form.reset();
});

bindForm("form-horario-listar", async (form) => {
    const response = await request("/Horarios/disponiveis", {
        query: {
            barbeiroId: toNumber(readValue(form, "barbeiroId"), "ID do barbeiro"),
            clienteId: toNumber(readValue(form, "clienteId"), "ID do cliente"),
            data: readValue(form, "data")
        }
    });

    showFeedback("Horarios disponiveis carregados.", "success");
    renderList(horariosListEl, response, formatHorario, "Nenhum horario disponivel encontrado.", horariosListMetaEl, "horario");
});

bindForm("form-agendamento-criar", async (form) => {
    const payload = {
        nomeCliente: readValue(form, "nomeCliente"),
        telefoneCliente: readValue(form, "telefoneCliente"),
        telefoneBarbeiro: readValue(form, "telefoneBarbeiro"),
        data: readValue(form, "data"),
        hora: normalizeTime(readValue(form, "hora"))
    };

    const response = await request("/agendamentos", {
        method: "POST",
        body: payload
    });

    showFeedback("Agendamento criado com sucesso.", "success");
    form.reset();
});

bindForm("form-agendamento-listar-barbeiro", async (form) => {
    const telefoneBarbeiro = readValue(form, "telefoneBarbeiro");
    const response = await request("/agendamentos/barbeiro/listar", {
        query: { telefoneBarbeiro }
    });

    showFeedback("Agendamentos do barbeiro carregados.", "success");
    renderList(agendamentosListEl, response, formatAgendamento, "Nenhum agendamento encontrado.", agendamentosListMetaEl, "agendamento");
});

bindForm("form-agendamento-concluir", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do agendamento");
    const senhaBarbeiro = readValue(form, "senhaBarbeiro");

    const response = await request(`/agendamentos/${id}/concluir`, {
        method: "PATCH",
        query: { senhaBarbeiro }
    });

    showFeedback("Servico concluido com sucesso.", "success");
    form.reset();
    await atualizarRanking();

    if (response && response.barbeiroTelefone) {
        const atualizados = await request("/agendamentos/barbeiro/listar", {
            query: { telefoneBarbeiro: response.barbeiroTelefone }
        });

        renderList(agendamentosListEl, atualizados, formatAgendamento, "Nenhum agendamento encontrado.", agendamentosListMetaEl, "agendamento");
    }
});

bindForm("form-agendamento-cancelar-cliente", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do agendamento");
    const senhaCliente = readValue(form, "senhaCliente");

    const response = await request(`/agendamentos/${id}/cancelar/cliente`, {
        method: "PATCH",
        query: { senhaCliente }
    });

    showFeedback("Agendamento cancelado pelo cliente.", "success");
    form.reset();
});

bindForm("form-agendamento-cancelar-barbeiro", async (form) => {
    const id = toNumber(readValue(form, "id"), "ID do agendamento");
    const senhaBarbeiro = readValue(form, "senhaBarbeiro");

    const response = await request(`/agendamentos/${id}/cancelar/barbeiro`, {
        method: "PATCH",
        query: { senhaBarbeiro }
    });

    showFeedback("Agendamento cancelado pelo barbeiro.", "success");
    form.reset();
});

document.addEventListener("click", (event) => {
    const button = event.target.closest(".quick-fill-btn");
    if (!button) {
        return;
    }

    const kind = button.dataset.fillKind;

    if (kind === "clienteAtualizar") {
        setFormValue("form-cliente-atualizar", "id", button.dataset.id);
        setFormValue("form-cliente-atualizar", "nome", button.dataset.nome);
        setFormValue("form-cliente-atualizar", "telefone", button.dataset.telefone);
        showFeedback("Campos de atualizar cliente preenchidos.", "info");
        return;
    }

    if (kind === "clienteRemover") {
        setFormValue("form-cliente-remover", "id", button.dataset.id);
        showFeedback("Campo de remover cliente preenchido.", "info");
        return;
    }

    if (kind === "barbeiroAtualizar") {
        setFormValue("form-barbeiro-atualizar", "id", button.dataset.id);
        setFormValue("form-barbeiro-atualizar", "nome", button.dataset.nome);
        setFormValue("form-barbeiro-atualizar", "especialidade", button.dataset.especialidade);
        setFormValue("form-barbeiro-atualizar", "telefone", button.dataset.telefone);
        setFormValue("form-barbeiro-atualizar", "cpf", button.dataset.cpf);
        showFeedback("Campos de atualizar barbeiro preenchidos.", "info");
        return;
    }

    if (kind === "barbeiroRemover") {
        setFormValue("form-barbeiro-remover", "id", button.dataset.id);
        showFeedback("Campo de remover barbeiro preenchido.", "info");
        return;
    }

    if (kind === "agendamentoConcluir") {
        setFormValue("form-agendamento-concluir", "id", button.dataset.id);
        showFeedback("Campo de concluir agendamento preenchido.", "info");
        return;
    }

    if (kind === "agendamentoCancelarCliente") {
        setFormValue("form-agendamento-cancelar-cliente", "id", button.dataset.id);
        showFeedback("Campo de cancelar agendamento (cliente) preenchido.", "info");
        return;
    }

    if (kind === "agendamentoCancelarBarbeiro") {
        setFormValue("form-agendamento-cancelar-barbeiro", "id", button.dataset.id);
        showFeedback("Campo de cancelar agendamento (barbeiro) preenchido.", "info");
    }
});

(async () => {
    try {
        const clientes = await atualizarListaClientes();
        renderRanking(clientes);
        await atualizarListaBarbeiros();
    } catch (error) {
        showFeedback("Nao foi possivel carregar os dados iniciais. Verifique se a API esta ativa.", "error");
    }
})();
