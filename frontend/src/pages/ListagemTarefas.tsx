import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import { CampoRenderizador } from '../components/Campos';
import type { ListagemResumo } from '../types/crud';
import { usarAutenticacao } from '../context/AuthContext';

export default function ListagemTarefas() {
    const [dadosListagem, setDadosListagem] = useState<ListagemResumo | null>(null);
    const { temPermissao } = usarAutenticacao();

    const buscarTarefas = async () => {
        try {
            const dados = await requisicaoApi<ListagemResumo>(`/api/tarefas${window.location.search}`);
            setDadosListagem(dados);
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao carregar tarefas.');
        }
    };

    useEffect(() => {
        buscarTarefas();
    }, []);

    const lidarComExclusao = async (id: number) => {
        if (!window.confirm('Tem certeza que deseja excluir esta tarefa?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/tarefas/${id}`, { method: 'DELETE' });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao excluir tarefa.');
        }
    };

    const lidarComConclusao = async (id: number) => {
        if (!window.confirm('Tem certeza que deseja concluir esta tarefa?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/tarefas/${id}/concluir`, { method: 'PUT' });
            buscarTarefas();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao concluir tarefa.');
        }
    };

    return (
        <section className="pagina">
            <header className="pagina__cabecalho">
                <h1 className="pagina__titulo">Tarefas</h1>

                <form className="busca-crud" method="GET" action="/tarefas" role="search" aria-label="Filtros">
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
                    <Carregando mensagem="Carregando tarefas..." />
                ) : dadosListagem.linhas.length === 0 ? (
                    <div className="vazio-crud">
                        <div className="vazio-crud__titulo">Nenhuma tarefa encontrada</div>
                        <div className="vazio-crud__texto">Tente ajustar os filtros.</div>
                    </div>
                ) : (
                    <div className="tabela-crud-wrap">
                        <table className="tabela-crud" aria-label="Tabela de tarefas">
                            <thead className="tabela-crud__cabecalho">
                                <tr className="tabela-crud__linha">
                                    {dadosListagem.colunas.map(col => (
                                        <th key={col.chave} className="tabela-crud__coluna">{col.label}</th>
                                    ))}
                                    <th className="tabela-crud__coluna">Ações</th>
                                </tr>
                            </thead>
                            <tbody className="tabela-crud__corpo">
                                {dadosListagem.linhas.map(linha => {
                                    const flagConcluida = linha.valores['flagConcluida'];
                                    const ehConcluida = flagConcluida === 'Sim';

                                    return (
                                        <tr key={linha.id} className="tabela-crud__linha">
                                            {dadosListagem.colunas.map(col => {
                                                const valor = linha.valores[col.chave];
                                                return (
                                                    <td key={col.chave} className="tabela-crud__celula">
                                                        {valor === null || valor === undefined ? '' : String(valor)}
                                                    </td>
                                                );
                                            })}
                                            <td className="tabela-crud__celula">
                                                <div className="acoes-tabela">
                                                    {temPermissao('TAREFAS_EDITAR') && <Link className="acoes-tabela__botao acoes-tabela__botao--editar" to={`/tarefas/${linha.id}/editar`}>Editar</Link>}
                                                    {temPermissao('TAREFAS_EXCLUIR') && <button className="acoes-tabela__botao acoes-tabela__botao--excluir" onClick={() => lidarComExclusao(linha.id)}>Excluir</button>}
                                                    {!ehConcluida && temPermissao('TAREFAS_EDITAR') && (
                                                        <button 
                                                            className="acoes-tabela__botao acoes-tabela__botao--editar" 
                                                            style={{ backgroundColor: '#28a745', borderColor: '#28a745', color: '#fff' }}
                                                            onClick={() => lidarComConclusao(linha.id)}
                                                        >
                                                            Concluir
                                                        </button>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    </div>
                )}
            </section>
        </section>
    );
}
