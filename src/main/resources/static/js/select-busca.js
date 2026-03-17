(() => {
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

    function inicializarCampoInt(input, maxLength) {
        input.maxLength = maxLength;
        input.addEventListener("input", () => {
            input.value = (input.value || "").replace(/\D/g, "").slice(0, maxLength);
        });
    }

    function inicializarAutoSubmitUf(select) {
        const form = select.closest("form");
        if (!form) return;

        let submitted = false;
        const onChange = () => {
            if (submitted) return;
            submitted = true;
            form.submit();
        };

        select.addEventListener("change", onChange);
    }

    document.addEventListener("DOMContentLoaded", () => {
        document.querySelectorAll(".js-cep-input").forEach(inicializarCampoCep);

        document.querySelectorAll(".js-int10-input").forEach(el => inicializarCampoInt(el, 10));
        document.querySelectorAll(".js-int12-input").forEach(el => inicializarCampoInt(el, 12));
        document.querySelectorAll(".js-int14-input").forEach(el => inicializarCampoInt(el, 14));
        document.querySelectorAll(".js-int20-input").forEach(el => inicializarCampoInt(el, 20));

        document.querySelectorAll(".campo--uf select, .campo--uf-cliente select")
                .forEach(inicializarAutoSubmitUf);
    });
})();
