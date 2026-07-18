package com.example.demo.auth.model;

import java.util.Set;

public enum Perfil {
    ADMIN("Administrador", Set.of("*")), // ADMIN possui todas as permissões (indicado por "*")
    PROJETO("Projeto", Set.of(
        "PEDIDOS_VISUALIZAR", "PEDIDOS_ADICIONAR", "PEDIDOS_EDITAR",
        "ORCAMENTOS_VISUALIZAR"
    )),
    ESCRITORIO("Escritório", Set.of(
        "PEDIDOS_VISUALIZAR", "PEDIDOS_EDITAR",
        "ORCAMENTOS_VISUALIZAR", "ORCAMENTOS_ADICIONAR", "ORCAMENTOS_EDITAR", "ORCAMENTOS_EXCLUIR"
    ));

    private final String descricao;
    private final Set<String> permissoes;

    Perfil(String descricao, Set<String> permissoes) {
        this.descricao = descricao;
        this.permissoes = permissoes;
    }

    public String getDescricao() {
        return descricao;
    }

    public Set<String> getPermissoes() {
        return permissoes;
    }

    /**
     * Verifica se o perfil atual tem uma permissão específica.
     * O perfil ADMIN sempre possui qualquer permissão.
     */
    public boolean temPermissao(String permissao) {
        if (this == ADMIN) {
            return true;
        }
        return permissoes.contains(permissao);
    }
}
