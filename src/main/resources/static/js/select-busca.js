(() => {
    function montarUrl(baseUrl, termo, uf, maximo) {
        const url = new URL(baseUrl, window.location.origin);
        url.searchParams.set("q", termo);
        url.searchParams.set("uf", uf);
        url.searchParams.set("limit", String(maximo));
        return url.toString();
    }

    async function buscarOpcoes(baseUrl, termo, uf, maximo) {
        const resposta = await fetch(montarUrl(baseUrl, termo, uf, maximo), {
            headers: { "Accept": "application/json" }
        });
        if (!resposta.ok) {
            throw new Error(`HTTP ${resposta.status}`);
        }
        return resposta.json();
    }

    function limparLista(datalist) {
        datalist.replaceChildren();
    }

    function preencherLista(datalist, opcoes) {
        limparLista(datalist);
        opcoes.forEach((opcao) => {
            const item = document.createElement("option");
            item.value = opcao.valor;
            datalist.appendChild(item);
        });
    }

    function limparSelect(select) {
        const placeholder = select.querySelector("option[value='']");
        select.replaceChildren();
        if (placeholder) {
            select.appendChild(placeholder);
        } else {
            const option = document.createElement("option");
            option.value = "";
            option.textContent = "Selecione";
            select.appendChild(option);
        }
    }

    function preencherSelect(select, opcoes, valorAtual) {
        limparSelect(select);
        opcoes.forEach((opcao) => {
            const option = document.createElement("option");
            option.value = opcao.valor;
            option.textContent = opcao.rotulo || opcao.valor;
            if (valorAtual && option.value === valorAtual) {
                option.selected = true;
            }
            select.appendChild(option);
        });
    }

    function inicializarCampo(input) {
        const form = input.closest("form");
        const listId = input.dataset.listId;
        const sourceUrl = input.dataset.sourceUrl;
        const minChars = Number(input.dataset.minChars || "3");
        const maxOptions = Number(input.dataset.maxOptions || "50");
        const ufInput = form ? form.querySelector("[name='uf']") : null;

        if (!listId || !sourceUrl) {
            return;
        }

        const datalist = document.getElementById(listId);
        if (!datalist) {
            return;
        }

        let requestId = 0;

        input.addEventListener("input", async () => {
            const termo = (input.value || "").trim();
            if (termo.length < minChars) {
                limparLista(datalist);
                return;
            }

            requestId += 1;
            const currentRequestId = requestId;

            try {
                const uf = (ufInput?.value || "SC").trim().toUpperCase();
                const opcoes = await buscarOpcoes(sourceUrl, termo, uf, maxOptions);
                if (currentRequestId !== requestId) {
                    return;
                }
                if (!Array.isArray(opcoes) || opcoes.length === 0) {
                    limparLista(datalist);
                    return;
                }
                preencherLista(datalist, opcoes.slice(0, maxOptions));
            } catch (_error) {
                if (currentRequestId !== requestId) {
                    return;
                }
                limparLista(datalist);
            }
        });

        if (ufInput) {
            const limparAoAlterarUf = () => {
                limparLista(datalist);
            };
            ufInput.addEventListener("input", limparAoAlterarUf);
            ufInput.addEventListener("change", limparAoAlterarUf);
        }
    }

    function inicializarSelectMunicipio(select) {
        const form = select.closest("form");
        const sourceUrl = select.dataset.sourceUrl;
        const ufField = (select.dataset.ufField || "uf").trim();
        const maxOptions = Number(select.dataset.maxOptions || "500");
        const ufInput = form ? form.querySelector(`[name='${ufField}']`) : null;

        if (!sourceUrl || !ufInput) {
            return;
        }

        let requestId = 0;

        const atualizarOpcoes = async () => {
            const uf = (ufInput.value || "").trim().toUpperCase();
            const valorAtual = (select.value || "").trim();

            if (!uf) {
                select.disabled = true;
                limparSelect(select);
                return;
            }

            requestId += 1;
            const currentRequestId = requestId;
            select.disabled = false;

            try {
                const opcoes = await buscarOpcoes(sourceUrl, "", uf, maxOptions);
                if (currentRequestId !== requestId) {
                    return;
                }
                if (!Array.isArray(opcoes)) {
                    limparSelect(select);
                    return;
                }
                preencherSelect(select, opcoes, valorAtual);
            } catch (_error) {
                if (currentRequestId !== requestId) {
                    return;
                }
                limparSelect(select);
            }
        };

        ufInput.addEventListener("input", atualizarOpcoes);
        ufInput.addEventListener("change", atualizarOpcoes);
        atualizarOpcoes();
    }

    function inicializarCampoCep(input) {
        input.maxLength = 9;

        input.addEventListener('input', () => {
            let valor = input.value.replace(/\D/g, '');

            if (valor.length > 5) {
                valor = valor.replace(/^(\d{5})(\d)/, '$1-$2');
            }

            input.value = valor;
        });
    }

    function inicializarCampoInt10(input) {
        input.maxLength = 10;
        input.addEventListener("input", () => {
            input.value = (input.value || "").replace(/\D/g, "").slice(0, 10);
        });
    }

    function inicializarCampoInt12(input) {
        input.maxLength = 12;
        input.addEventListener("input", () => {
            input.value = (input.value || "").replace(/\D/g, "").slice(0, 12);
        });
    }

    function inicializarCampoInt14(input) {
        input.maxLength = 14;
        input.addEventListener("input", () => {
            input.value = (input.value || "").replace(/\D/g, "").slice(0, 14);
        });
    }

    function inicializarCampoInt20(input) {
        input.maxLength = 20;
        input.addEventListener("input", () => {
            input.value = (input.value || "").replace(/\D/g, "").slice(0, 20);
        });
    }

    document.addEventListener("DOMContentLoaded", () => {
        document.querySelectorAll(".js-localidade-input").forEach(inicializarCampo);
        document.querySelectorAll(".js-municipio-select").forEach(inicializarSelectMunicipio);
        document.querySelectorAll(".js-cep-input").forEach(inicializarCampoCep);
        document.querySelectorAll(".js-int10-input").forEach(inicializarCampoInt10);
        document.querySelectorAll(".js-int12-input").forEach(inicializarCampoInt12);
        document.querySelectorAll(".js-int14-input").forEach(inicializarCampoInt14);
        document.querySelectorAll(".js-int20-input").forEach(inicializarCampoInt20);
    });
})();
