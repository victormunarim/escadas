import type React from 'react';
import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import BlocoDetalhes from '../components/BlocoDetalhes';
import BlocoTarefas from '../components/BlocoTarefas';

interface ArquivoOrcamento {
    id: number;
    nome: string;
    link: string;
    etapa?: number;
}

interface DetalhesOrcamento {
    id: number;
    nome: string;
    bairro: string;
    descricao: string;
    detalhes: Record<string, string>;
    arquivos: ArquivoOrcamento[];
    flagEncerrado?: boolean;
}

const obterLinkDownload = (link: string) => {
    if (!link) return '';
    if (link.includes('/file/d/')) {
        const partes = link.split('/file/d/');
        if (partes.length > 1) {
            const id = partes[1].split('/')[0];
            return `https://drive.google.com/uc?export=download&id=${id}`;
        }
    }
    return link;
};

export default function VisualizacaoOrcamento() {
    const { id } = useParams<{ id: string }>();
    const [dadosOrcamento, setDadosOrcamento] = useState<DetalhesOrcamento | null | undefined>(undefined);
    const [enviando, setEnviando] = useState<Record<number, boolean>>({});
    const [arquivosSelecionados, setArquivosSelecionados] = useState<Record<number, File>>({});
    const [etapasVisiveis, setEtapasVisiveis] = useState(1);

    const buscarDetalhesOrcamento = async () => {
        try {
            const dados = await requisicaoApi<DetalhesOrcamento>(`/api/orcamentos/${id}`);
            setDadosOrcamento(dados);
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao carregar detalhes do orçamento.');
            setDadosOrcamento(null);
        }
    };

    const lidarComEncerramento = async () => {
        if (!window.confirm('Tem certeza que deseja encerrar este orçamento?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/orcamentos/${id}/encerrar`, {
                method: 'PUT'
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao encerrar orçamento.');
        }
    };

    const lidarComReabertura = async () => {
        if (!window.confirm('Tem certeza que deseja reabrir este orçamento?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/orcamentos/${id}/reabrir`, {
                method: 'PUT'
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao reabrir orçamento.');
        }
    };

    useEffect(() => {
        buscarDetalhesOrcamento();
    }, [id]);

    useEffect(() => {
        if (dadosOrcamento?.arquivos) {
            const maxEtapa = dadosOrcamento.arquivos.reduce((max, arq) => Math.max(max, arq.etapa || 1), 1);
            setEtapasVisiveis(prev => Math.max(prev, maxEtapa));
        }
    }, [dadosOrcamento]);

    const lidarComMudancaArquivo = (e: React.ChangeEvent<HTMLInputElement>, etapa: number) => {
        if (e.target.files && e.target.files.length > 0) {
            setArquivosSelecionados(prev => ({
                ...prev,
                [etapa]: e.target.files![0]
            }));
        }
    };

    const lidarComEnvioArquivo = async (e: React.FormEvent<HTMLFormElement>, etapa: number) => {
        e.preventDefault();
        const arquivoSelecionado = arquivosSelecionados[etapa];
        if (!arquivoSelecionado) {
            alert('Selecione um arquivo para enviar.');
            return;
        }

        setEnviando(prev => ({ ...prev, [etapa]: true }));
        const dadosFormulario = new FormData();
        dadosFormulario.append('arquivo', arquivoSelecionado);

        try {
            await requisicaoApi<{ sucesso?: string }>(`/api/orcamentos/${id}/arquivos?etapa=${etapa}`, {
                method: 'POST',
                body: dadosFormulario,
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao enviar arquivo.');
            setEnviando(prev => ({ ...prev, [etapa]: false }));
        }
    };

    const lidarComExclusaoArquivo = async (arquivoId: number) => {
        if (!window.confirm('Tem certeza que deseja excluir este arquivo?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/orcamentos/${id}/arquivos/${arquivoId}`, {
                method: 'DELETE'
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao excluir arquivo.');
        }
    };

    if (dadosOrcamento === undefined) {
        return <Carregando mensagem="Carregando detalhes do orçamento..." margemSuperior />;
    }

    if (dadosOrcamento === null) {
        return (
            <section className="pagina">
                <div className="alerta-formulario alerta-formulario--erro">
                    Orçamento não encontrado.
                </div>
                <Link className="formulario-crud__link" to="/orcamentos">Voltar para listagem</Link>
            </section>
        );
    }

    const { id: orcamentoId, nome, detalhes, arquivos } = dadosOrcamento;

    const listEtapas = Array.from({ length: etapasVisiveis }, (_, index) => etapasVisiveis - index);

    return (
        <section className="pagina pedido-visualizacao">
            <header className="pagina__cabecalho pedido-visualizacao__cabecalho">
                <div>
                    <h1 className="pagina__titulo">Visualização do Orçamento</h1>
                    <p className="pagina__subtitulo">
                        Orçamento #{orcamentoId} - {nome}
                        {dadosOrcamento.flagEncerrado && (
                            <span style={{ marginLeft: '10px', backgroundColor: '#f8d7da', color: '#721c24', padding: '2px 8px', borderRadius: '4px', fontSize: '0.85rem', fontWeight: 600 }}>
                                Encerrado
                            </span>
                        )}
                    </p>
                </div>
                <div className="pedido-visualizacao__acoes" style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    {!dadosOrcamento.flagEncerrado ? (
                        <button 
                            className="acoes-tabela__botao" 
                            style={{ backgroundColor: '#dc3545', borderColor: '#dc3545', color: '#fff', padding: '6px 12px', fontSize: '0.9rem', height: 'auto', lineHeight: 1.2, cursor: 'pointer' }}
                            onClick={lidarComEncerramento}
                        >
                            Encerrar Orçamento
                        </button>
                    ) : (
                        <button 
                            className="acoes-tabela__botao" 
                            style={{ backgroundColor: '#28a745', borderColor: '#28a745', color: '#fff', padding: '6px 12px', fontSize: '0.9rem', height: 'auto', lineHeight: 1.2, cursor: 'pointer' }}
                            onClick={lidarComReabertura}
                        >
                            Reabrir Orçamento
                        </button>
                    )}
                    <Link className="acoes-tabela__botao acoes-tabela__botao--editar" to={`/orcamentos/${orcamentoId}/editar`}>
                        Editar
                    </Link>
                    <Link className="formulario-crud__link" to="/orcamentos">Voltar para listagem</Link>
                </div>
            </header>

            <div className="pedido-visualizacao__grade">
                <BlocoDetalhes titulo="Resumo do Orçamento" dados={detalhes} />
            </div>

            <section className="cartao-crud pedido-visualizacao__bloco-arquivos" style={{ marginTop: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px', borderBottom: '2px solid #eee', paddingBottom: '10px' }}>
                    <h2 style={{ fontSize: '1.4rem', fontWeight: 600, margin: 0, color: '#333' }}>Arquivos do Orçamento por Etapas</h2>
                    {!dadosOrcamento.flagEncerrado && (
                        <button 
                            className="busca-crud__botao" 
                            type="button"
                            style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.9rem', padding: '6px 12px' }}
                            onClick={() => setEtapasVisiveis(prev => prev + 1)}
                        >
                            <strong>+</strong> Iniciar Nova Etapa
                        </button>
                    )}
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    {listEtapas.map((etapa) => {
                        const arquivosDaEtapa = (arquivos || []).filter(arq => (arq.etapa || 1) === etapa);

                        return (
                            <div key={etapa} style={{ border: '1px solid #ddd', borderRadius: '6px', padding: '16px', backgroundColor: '#fcfcfc' }}>
                                <h3 style={{ fontSize: '1.15rem', fontWeight: 600, marginBottom: '12px', color: '#13508c', borderBottom: '1px solid #eee', paddingBottom: '6px' }}>
                                    Etapa {etapa}
                                </h3>

                                {!dadosOrcamento.flagEncerrado && (
                                    <form className="pedido-visualizacao__upload" onSubmit={(e) => lidarComEnvioArquivo(e, etapa)} style={{ marginBottom: '16px' }}>
                                        <input 
                                            className="pedido-visualizacao__arquivo-input" 
                                            type="file" 
                                            name={`arquivo-${etapa}`} 
                                            required 
                                            onChange={(e) => lidarComMudancaArquivo(e, etapa)}
                                        />
                                        <button className="busca-crud__botao" type="submit" disabled={enviando[etapa]}>
                                            {enviando[etapa] ? 'Enviando...' : 'Enviar para Google Drive'}
                                        </button>
                                    </form>
                                )}

                                {arquivosDaEtapa.length === 0 ? (
                                    <div className="pedido-visualizacao__arquivos-placeholder" style={{ margin: 0, padding: '15px' }}>
                                        Nenhum arquivo vinculado à Etapa {etapa}.
                                    </div>
                                ) : (
                                    <ul className="pedido-visualizacao__arquivos-lista" style={{ margin: 0 }}>
                                        {arquivosDaEtapa.map((arquivo) => (
                                            <li key={arquivo.id} className="pedido-visualizacao__arquivo-item" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '8px 12px' }}>
                                                <span>{arquivo.nome}</span>
                                                <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                                                    <a 
                                                        href={obterLinkDownload(arquivo.link)} 
                                                        target="_blank" 
                                                        rel="noopener noreferrer"
                                                        className="acoes-tabela__botao"
                                                        style={{ 
                                                            backgroundColor: '#17a2b8', 
                                                            borderColor: '#17a2b8', 
                                                            color: '#fff', 
                                                            padding: '2px 8px', 
                                                            fontSize: '0.8rem', 
                                                            height: 'auto', 
                                                            lineHeight: 1.2,
                                                            textDecoration: 'none',
                                                            display: 'inline-flex',
                                                            alignItems: 'center',
                                                            cursor: 'pointer'
                                                        }}
                                                    >
                                                        Download
                                                    </a>
                                                    {!dadosOrcamento.flagEncerrado && (
                                                        <button 
                                                            className="acoes-tabela__botao acoes-tabela__botao--excluir" 
                                                            type="button"
                                                            style={{ padding: '2px 8px', fontSize: '0.8rem', height: 'auto', lineHeight: 1 }}
                                                            onClick={() => lidarComExclusaoArquivo(arquivo.id)}
                                                        >
                                                            Excluir
                                                        </button>
                                                    )}
                                                </div>
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        );
                    })}
                </div>
            </section>

            <BlocoTarefas extChave="orcamento_id" extId={orcamentoId} desabilitado={dadosOrcamento.flagEncerrado} />
        </section>
    );
}
