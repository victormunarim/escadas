import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import { CampoRenderizador } from '../components/Campos';
import type { ListagemResumo } from '../types/crud';
import { usarAutenticacao } from '../context/AuthContext';

export default function ListagemOrcamentos() {
    const [dadosListagem, setDadosListagem] = useState<ListagemResumo | null>(null);
    const { temPermissao } = usarAutenticacao();

    useEffect(() => {
        const buscarOrcamentos = async () => {
            try {
                const dados = await requisicaoApi<ListagemResumo>(`/api/orcamentos${window.location.search}`);
                setDadosListagem(dados);
            } catch (erroRequisicao: any) {
                alert(erroRequisicao.message || 'Erro ao carregar orçamentos.');
            }
        };

        buscarOrcamentos();
    }, []);

    const lidarComExclusao = async (id: number) => {
        if (!window.confirm('Tem certeza que deseja excluir este orçamento?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/orcamentos/${id}`, { method: 'DELETE' });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao excluir orçamento.');
        }
    };

    return (
        <section className="pagina">
            <header className="pagina__cabecalho">
                <h1 className="pagina__titulo">Orçamentos</h1>

                <form className="busca-crud" method="GET" action="/orcamentos" role="search" aria-label="Filtros">
                    <div className="filtros-crud">
                        {dadosListagem?.filtros?.map((filtro, idx) => (
                            <CampoRenderizador
                                key={filtro.name || idx}
                                campo={filtro}
                            />
                        ))}

                        <div className="filtro-crud filtro-crud--acoes">
                            <button className="busca-crud__botao" type="submit">Pesquisar</button>
                        </div>
                    </div>
                </form>
            </header>

            <section className="cartao-crud">
                {dadosListagem === null ? (
                    <Carregando mensagem="Carregando orçamentos..." />
                ) : dadosListagem.linhas.length === 0 ? (
                    <div className="vazio-crud">
                        <div className="vazio-crud__titulo">Nenhum registro encontrado</div>
                        <div className="vazio-crud__texto">Tente ajustar os filtros.</div>
                    </div>
                ) : (
                    <div className="tabela-crud-wrap">
                        <table className="tabela-crud" aria-label="Tabela de orçamentos">
                            <thead className="tabela-crud__cabecalho">
                                <tr className="tabela-crud__linha">
                                    {dadosListagem.colunas.map(col => (
                                        <th key={col.chave} className="tabela-crud__coluna">{col.label}</th>
                                    ))}
                                    <th className="tabela-crud__coluna">Ações</th>
                                </tr>
                            </thead>
                            <tbody className="tabela-crud__corpo">
                                {dadosListagem.linhas.map(linha => (
                                    <tr key={linha.id} className="tabela-crud__linha">
                                        {dadosListagem.colunas.map(col => {
                                            const valor = linha.valores[col.chave];
                                            if (col.chave === 'descricao') {
                                                return (
                                                    <td key={col.chave} className="tabela-crud__celula" style={{ maxWidth: '200px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                                                        {valor}
                                                    </td>
                                                );
                                            }
                                            return (
                                                <td key={col.chave} className="tabela-crud__celula">
                                                    {valor === null || valor === undefined ? '' : String(valor)}
                                                </td>
                                            );
                                        })}
                                        <td className="tabela-crud__celula">
                                            <div className="acoes-tabela">
                                                {temPermissao('ORCAMENTOS_VISUALIZAR') && <Link className="acoes-tabela__botao acoes-tabela__botao--visualizar" to={`/orcamentos/${linha.id}/visualizar`}>Visualizar</Link>}
                                                {temPermissao('ORCAMENTOS_EDITAR') && <Link className="acoes-tabela__botao acoes-tabela__botao--editar" to={`/orcamentos/${linha.id}/editar`}>Editar</Link>}
                                                {temPermissao('ORCAMENTOS_EXCLUIR') && <button className="acoes-tabela__botao acoes-tabela__botao--excluir" onClick={() => lidarComExclusao(linha.id)}>Excluir</button>}
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </section>
        </section>
    );
}
