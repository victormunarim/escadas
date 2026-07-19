import type React from 'react';
import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import BlocoDetalhes from '../components/BlocoDetalhes';

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
    pedidoId?: number;
    pedidoNumero?: number;
    pedidoCliente?: string;
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
    const ehTecnico = window.location.pathname.startsWith('/tecnicos');
    const apiEndpoint = ehTecnico ? '/api/tecnicos' : '/api/orcamentos';
    const rotaBase = ehTecnico ? '/tecnicos' : '/orcamentos';
    const tituloModulo = ehTecnico ? 'Técnico' : 'Orçamento';

    const [dadosOrcamento, setDadosOrcamento] = useState<DetalhesOrcamento | null | undefined>(undefined);
    const [enviando, setEnviando] = useState<Record<number, boolean>>({});
    const [arquivosSelecionados, setArquivosSelecionados] = useState<Record<number, File>>({});
    const [etapasVisiveis, setEtapasVisiveis] = useState(1);

    const buscarDetalhesOrcamento = async () => {
        try {
            const dados = await requisicaoApi<DetalhesOrcamento>(`${apiEndpoint}/${id}`);
            setDadosOrcamento(dados);
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || `Erro ao carregar detalhes do ${tituloModulo.toLowerCase()}.`);
            setDadosOrcamento(null);
        }
    };

    const lidarComEncerramento = async () => {
        if (!window.confirm(`Tem certeza que deseja encerrar este ${tituloModulo.toLowerCase()}?`)) {
            return;
        }

        try {
            await requisicaoApi(`${apiEndpoint}/${id}/encerrar`, {
                method: 'PUT'
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || `Erro ao encerrar ${tituloModulo.toLowerCase()}.`);
        }
    };

    const lidarComReabertura = async () => {
        if (!window.confirm(`Tem certeza que deseja reabrir este ${tituloModulo.toLowerCase()}?`)) {
            return;
        }

        try {
            await requisicaoApi(`${apiEndpoint}/${id}/reabrir`, {
                method: 'PUT'
            });
            window.location.href = `/orcamentos/${id}/visualizar`;
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || `Erro ao reabrir ${tituloModulo.toLowerCase()}.`);
        }
    };

    useEffect(() => {
        buscarDetalhesOrcamento();
    }, [id, apiEndpoint]);

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
            await requisicaoApi<{ sucesso?: string }>(`${apiEndpoint}/${id}/arquivos?etapa=${etapa}`, {
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
            await requisicaoApi(`${apiEndpoint}/${id}/arquivos/${arquivoId}`, {
                method: 'DELETE'
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao excluir arquivo.');
        }
    };

    if (dadosOrcamento === undefined) {
        return <Carregando mensagem={`Carregando detalhes do ${tituloModulo.toLowerCase()}...`} margemSuperior />;
    }

    if (dadosOrcamento === null) {
        return (
            <section className="pagina">
                <div className="alerta-formulario alerta-formulario--erro">
                    {tituloModulo} não encontrado.
                </div>
                <Link className="formulario-crud__link" to={rotaBase}>Voltar para listagem</Link>
            </section>
        );
    }

    const { id: itemKey, nome, detalhes, arquivos, pedidoId, pedidoNumero, pedidoCliente } = dadosOrcamento;

    const podeGerenciarArquivos = !dadosOrcamento.flagEncerrado || ehTecnico;
    const listEtapas = Array.from({ length: etapasVisiveis }, (_, index) => etapasVisiveis - index);

    return (
        <section className="pagina pedido-visualizacao">
            <header className="pagina__cabecalho pedido-visualizacao__cabecalho">
                <div>
                    <h1 className="pagina__titulo">{`Visualização do ${tituloModulo}`}</h1>
                    <p className="pagina__subtitulo">
                        {`${tituloModulo} #${itemKey} - ${nome}`}
                        {dadosOrcamento.flagEncerrado && (
                            <span style={{ marginLeft: '10px', backgroundColor: '#f8d7da', color: '#721c24', padding: '2px 8px', borderRadius: '4px', fontSize: '0.85rem', fontWeight: 600 }}>
                                Encerrado
                            </span>
                        )}
                        {pedidoId && (
                            <span>
                                {' '}| Vinculado ao Pedido:{' '}
                                <Link to={`/pedidos/${pedidoId}/visualizar`} style={{ color: '#13508c', fontWeight: 600, textDecoration: 'underline' }}>
                                    #{pedidoNumero} - {pedidoCliente}
                                </Link>
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
                            {`Encerrar ${tituloModulo}`}
                        </button>
                    ) : (
                        <button 
                            className="acoes-tabela__botao" 
                            style={{ backgroundColor: '#28a745', borderColor: '#28a745', color: '#fff', padding: '6px 12px', fontSize: '0.9rem', height: 'auto', lineHeight: 1.2, cursor: 'pointer' }}
                            onClick={lidarComReabertura}
                        >
                            {`Reabrir ${tituloModulo}`}
                        </button>
                    )}
                    <Link className="acoes-tabela__botao acoes-tabela__botao--editar" to={`${rotaBase}/${itemKey}/editar`}>
                        Editar
                    </Link>
                    <Link className="formulario-crud__link" to={rotaBase}>Voltar para listagem</Link>
                </div>
            </header>

            <div className="pedido-visualizacao__grade">
                <BlocoDetalhes titulo={`Resumo do ${tituloModulo}`} dados={detalhes} />
            </div>

            <section className="cartao-crud pedido-visualizacao__bloco-arquivos" style={{ marginTop: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px', borderBottom: '2px solid #eee', paddingBottom: '10px' }}>
                    <h2 style={{ fontSize: '1.4rem', fontWeight: 600, margin: 0, color: '#333' }}>{`Arquivos do ${tituloModulo} por Etapas`}</h2>
                    {podeGerenciarArquivos && (
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

                                {podeGerenciarArquivos && (
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
                                                    {podeGerenciarArquivos && (
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
        </section>
    );
}
