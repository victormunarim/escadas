package com.example.demo.auth.security;

import com.example.demo.auth.model.Perfil;
import com.example.demo.auth.service.UserDetailsImpl;
import com.example.demo.shared.crud.render.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class SecurityUtil {

    /**
     * Obtém o perfil do usuário atualmente autenticado.
     * Retorna null se não houver usuário autenticado ou se as informações do principal
     * não forem uma instância de UserDetailsImpl.
     */
    public static Perfil getPerfilUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getPerfil();
        }
        return null;
    }

    /**
     * Verifica se o usuário atualmente autenticado tem uma permissão específica.
     * O perfil ADMIN sempre retornará true para qualquer verificação.
     */
    public static boolean temPermissao(String permissao) {
        Perfil perfil = getPerfilUsuarioLogado();
        if (perfil == null) {
            return false;
        }
        return perfil.temPermissao(permissao);
    }

    /**
     * Centraliza a verificação de permissão para campos específicos do sistema.
     * Se novos campos restritos surgirem, basta adicioná-los nesta regra.
     */
    public static boolean temAcessoAoCampo(String nomeCampo) {
        if (nomeCampo == null) {
            return true;
        }
        Perfil perfil = getPerfilUsuarioLogado();
        if (perfil == null) {
            return false;
        }
        
        // ADMIN tem acesso irrestrito a todos os campos
        if (perfil == Perfil.ADMIN) {
            return true;
        }

        // Regra para campos financeiros (ex: custos e valores dos pedidos, brutos ou formatados)
//        if ("valor".equals(nomeCampo) || "valorTotal".equals(nomeCampo) ||
//            "valorFormatado".equals(nomeCampo) || "valorTotalFormatado".equals(nomeCampo) ||
//            "Valor".equals(nomeCampo) || "Valor Total".equals(nomeCampo)) {
//            return perfil.temPermissao("VER_CUSTOS");
//        }

        // Adicione outras regras de campos/telas aqui se necessário.
        // Por padrão, qualquer usuário autenticado tem acesso aos demais campos.
        return true;
    }

    /**
     * Filtra dinamicamente uma lista de campos a serem renderizados,
     * removendo campos sem permissão e limpando seções vazias.
     */
    public static List<CampoRender> filtrarCampos(List<CampoRender> campos) {
        if (campos == null) {
            return null;
        }

        List<CampoRender> resultado = new ArrayList<>();
        SecaoRender secaoPendente = null;

        for (CampoRender campo : campos) {
            if (campo instanceof SecaoRender secao) {
                // Guarda a seção para renderizar somente se houver algum campo sob ela
                secaoPendente = secao;
            } else {
                String nome = campo.name();
                // Se for um campo sem nome (ex: decorativo) ou se tiver permissão
                if (nome == null || temAcessoAoCampo(nome)) {
                    if (secaoPendente != null) {
                        resultado.add(secaoPendente);
                        secaoPendente = null;
                    }
                    resultado.add(campo);
                }
            }
        }

        return resultado;
    }

    /**
     * Filtra dinamicamente uma ListagemDTO, removendo colunas, valores e filtros
     * para os quais o usuário atual não possui permissão de visualização.
     */
    public static ListagemDTO filtrarListagem(ListagemDTO listagem) {
        if (listagem == null) {
            return null;
        }

        // 1. Filtrar as colunas
        List<ColunaListagem> colunasFiltradas = new ArrayList<>();
        Set<String> chavesPermitidas = new HashSet<>();
        for (ColunaListagem col : listagem.colunas()) {
            if (temAcessoAoCampo(col.chave())) {
                colunasFiltradas.add(col);
                chavesPermitidas.add(col.chave());
            }
        }

        // 2. Filtrar os valores de cada linha
        List<LinhaListagem> linhasFiltradas = new ArrayList<>();
        for (LinhaListagem linha : listagem.linhas()) {
            Map<String, Object> valoresFiltrados = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : linha.valores().entrySet()) {
                if (chavesPermitidas.contains(entry.getKey())) {
                    valoresFiltrados.put(entry.getKey(), entry.getValue());
                }
            }
            linhasFiltradas.add(new LinhaListagem(linha.id(), valoresFiltrados));
        }

        // 3. Filtrar os filtros de busca/listagem
        List<CampoRender> filtrosFiltrados = filtrarCampos(listagem.filtros());

        return new ListagemDTO(colunasFiltradas, linhasFiltradas, filtrosFiltrados);
    }
}
