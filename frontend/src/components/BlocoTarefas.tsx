import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { requisicaoApi } from '../api';

interface TarefaRef {
    id: number;
    tipoTarefaNome: string;
    responsavelNome: string;
    flagConcluida: boolean;
    dataCadastroFormatado: string;
}

interface BlocoTarefasProps {
    extChave: string;
    extId: number;
    desabilitado?: boolean;
}

export default function BlocoTarefas({ extChave, extId, desabilitado }: BlocoTarefasProps) {
    const [tarefas, setTarefas] = useState<TarefaRef[] | null>(null);

    const carregarTarefas = async () => {
        try {
            const dados = await requisicaoApi<TarefaRef[]>(
                `/api/tarefas/referencia?extChave=${extChave}&extId=${extId}`
            );
            setTarefas(dados);
        } catch (erroRequisicao: any) {
            console.error('Erro ao carregar tarefas vinculadas:', erroRequisicao);
        }
    };

    useEffect(() => {
        carregarTarefas();
    }, [extChave, extId]);

    const lidarComConclusao = async (id: number) => {
        if (!window.confirm('Tem certeza que deseja concluir esta tarefa?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/tarefas/${id}/concluir`, { method: 'PUT' });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao concluir tarefa.');
        }
    };

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

    return (
        <section className="cartao-crud pedido-visualizacao__bloco-arquivos" style={{ marginTop: '24px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px', borderBottom: '2px solid #eee', paddingBottom: '10px' }}>
                <h2 style={{ fontSize: '1.4rem', fontWeight: 600, margin: 0, color: '#333' }}>Tarefas Vinculadas</h2>
                {!desabilitado && (
                    <Link 
                        className="busca-crud__botao" 
                        to={`/tarefas/novo?extChave=${extChave}&extId=${extId}`}
                        style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.9rem', padding: '6px 12px', color: '#fff' }}
                    >
                        <strong>+</strong> Adicionar Tarefa
                    </Link>
                )}
            </div>

            {tarefas === null ? (
                <div style={{ padding: '10px', color: '#666' }}>Carregando tarefas...</div>
            ) : tarefas.length === 0 ? (
                <div className="pedido-visualizacao__arquivos-placeholder" style={{ margin: 0, padding: '15px' }}>
                    Nenhuma tarefa vinculada a este registro.
                </div>
            ) : (
                <div className="tabela-crud-wrap" style={{ marginTop: '10px' }}>
                    <table className="tabela-crud" style={{ width: '100%', borderCollapse: 'collapse' }}>
                        <thead>
                            <tr className="tabela-crud__linha">
                                <th className="tabela-crud__coluna">Tipo de Tarefa</th>
                                <th className="tabela-crud__coluna">Responsável</th>
                                <th className="tabela-crud__coluna">Data Cadastro</th>
                                <th className="tabela-crud__coluna">Situação</th>
                                <th className="tabela-crud__coluna">Ações</th>
                            </tr>
                        </thead>
                        <tbody className="tabela-crud__corpo">
                            {tarefas.map((tarefa) => (
                                <tr key={tarefa.id} className="tabela-crud__linha">
                                    <td className="tabela-crud__celula">{tarefa.tipoTarefaNome}</td>
                                    <td className="tabela-crud__celula">{tarefa.responsavelNome}</td>
                                    <td className="tabela-crud__celula">{tarefa.dataCadastroFormatado}</td>
                                    <td className="tabela-crud__celula">
                                        <span style={{ 
                                            padding: '4px 8px', 
                                            borderRadius: '4px', 
                                            fontSize: '0.85rem', 
                                            fontWeight: 500,
                                            backgroundColor: tarefa.flagConcluida ? '#e2f0d9' : '#fff2cc',
                                            color: tarefa.flagConcluida ? '#385723' : '#7f6000'
                                        }}>
                                            {tarefa.flagConcluida ? 'Concluída' : 'Pendente'}
                                        </span>
                                    </td>
                                    <td className="tabela-crud__celula">
                                        <div style={{ display: 'inline-flex', gap: '8px' }}>
                                            {!desabilitado ? (
                                                <>
                                                    <Link 
                                                        className="acoes-tabela__botao acoes-tabela__botao--editar" 
                                                        style={{ padding: '2px 8px', fontSize: '0.8rem', height: 'auto', lineHeight: 1.2 }}
                                                        to={`/tarefas/${tarefa.id}/editar?extChave=${extChave}&extId=${extId}`}
                                                    >
                                                        Editar
                                                    </Link>
                                                    <button 
                                                        className="acoes-tabela__botao acoes-tabela__botao--excluir" 
                                                        style={{ padding: '2px 8px', fontSize: '0.8rem', height: 'auto', lineHeight: 1 }}
                                                        onClick={() => lidarComExclusao(tarefa.id)}
                                                    >
                                                        Excluir
                                                    </button>
                                                    {!tarefa.flagConcluida && (
                                                        <button 
                                                            className="acoes-tabela__botao" 
                                                            style={{ backgroundColor: '#28a745', borderColor: '#28a745', color: '#fff', padding: '2px 8px', fontSize: '0.8rem', height: 'auto', lineHeight: 1 }}
                                                            onClick={() => lidarComConclusao(tarefa.id)}
                                                        >
                                                            Concluir
                                                        </button>
                                                    )}
                                                </>
                                            ) : (
                                                <span style={{ color: '#aaa', fontSize: '0.9rem' }}>-</span>
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </section>
    );
}
