import type React from 'react';
import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import BlocoDetalhes from '../components/BlocoDetalhes';
import BlocoTarefas from '../components/BlocoTarefas';

interface ArquivoPedido {
    id: number;
    nome: string;
    link: string;
}

interface DetalhesPedido {
    pedidoId: number;
    resumoPedido: Record<string, string>;
    dadosCliente: Record<string, string>;
    enderecoObra: Record<string, string>;
    enderecoCliente: Record<string, string>;
    arquivosPedido: ArquivoPedido[];
    orcamentoId?: number;
    orcamentoNome?: string;
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

export default function VisualizacaoPedido() {
    const { id } = useParams<{ id: string }>();
    const [dadosPedido, setDadosPedido] = useState<DetalhesPedido | null | undefined>(undefined);
    const [enviando, setEnviando] = useState(false);
    const [arquivoSelecionado, setArquivoSelecionado] = useState<File | null>(null);

    const buscarDetalhesPedido = async () => {
        try {
            const dados = await requisicaoApi<DetalhesPedido>(`/api/pedidos/${id}`);
            setDadosPedido(dados);
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao carregar detalhes do pedido.');
            setDadosPedido(null);
        }
    };

    useEffect(() => {
        buscarDetalhesPedido();
    }, [id]);

    const lidarComMudancaArquivo = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setArquivoSelecionado(e.target.files[0]);
        }
    };

    const lidarComEnvioArquivo = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!arquivoSelecionado) {
            alert('Selecione um arquivo para enviar.');
            return;
        }

        setEnviando(true);
        const dadosFormulario = new FormData();
        dadosFormulario.append('arquivo', arquivoSelecionado);

        try {
            await requisicaoApi<{ sucesso?: string }>(`/api/pedidos/${id}/arquivos`, {
                method: 'POST',
                body: dadosFormulario,
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao enviar arquivo.');
            setEnviando(false);
        }
    };

    const lidarComExclusaoArquivo = async (arquivoId: number) => {
        if (!window.confirm('Tem certeza que deseja excluir este arquivo?')) {
            return;
        }

        try {
            await requisicaoApi(`/api/pedidos/${id}/arquivos/${arquivoId}`, {
                method: 'DELETE'
            });
            window.location.reload();
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao excluir arquivo.');
        }
    };

    if (dadosPedido === undefined) {
        return <Carregando mensagem="Carregando detalhes do pedido..." margemSuperior />;
    }

    if (dadosPedido === null) {
        return (
            <section className="pagina">
                <div className="alerta-formulario alerta-formulario--erro">
                    Pedido não encontrado.
                </div>
                <Link className="formulario-crud__link" to="/pedidos">Voltar para listagem</Link>
            </section>
        );
    }

    const { pedidoId, resumoPedido, dadosCliente, enderecoObra, enderecoCliente, arquivosPedido } = dadosPedido;

    return (
        <section className="pagina pedido-visualizacao">
            <header className="pagina__cabecalho pedido-visualizacao__cabecalho">
                <div>
                    <h1 className="pagina__titulo">Visualização do Pedido</h1>
                    <p className="pagina__subtitulo">
                        Pedido #{pedidoId}
                        {dadosPedido.orcamentoId && (
                            <span>
                                {' '}| Vinculado ao Orçamento:{' '}
                                <Link to={`/orcamentos/${dadosPedido.orcamentoId}/visualizar`} style={{ color: '#13508c', fontWeight: 600, textDecoration: 'underline' }}>
                                    #{dadosPedido.orcamentoId} - {dadosPedido.orcamentoNome}
                                </Link>
                            </span>
                        )}
                    </p>
                </div>
                <div className="pedido-visualizacao__acoes">
                    <Link className="acoes-tabela__botao acoes-tabela__botao--editar" to={`/pedidos/${pedidoId}/editar`}>
                        Editar
                    </Link>
                    <a className="acoes-tabela__botao acoes-tabela__botao--pdf" href={`/pedidos/${pedidoId}/pdf`} target="_blank" rel="noopener noreferrer">
                        PDF
                    </a>
                    <Link className="formulario-crud__link" to="/pedidos">Voltar para listagem</Link>
                </div>
            </header>

            <div className="pedido-visualizacao__grade">
                <BlocoDetalhes titulo="Resumo do Pedido" dados={resumoPedido} />
                <BlocoDetalhes titulo="Dados do Cliente" dados={dadosCliente} />
                <BlocoDetalhes titulo="Endereço da Obra" dados={enderecoObra} />
                <BlocoDetalhes titulo="Endereço do Cliente" dados={enderecoCliente} />
            </div>

            <section className="cartao-crud pedido-visualizacao__bloco-arquivos">
                <h2 className="pedido-visualizacao__titulo">Arquivos</h2>
                <form className="pedido-visualizacao__upload" onSubmit={lidarComEnvioArquivo}>
                    <input 
                        className="pedido-visualizacao__arquivo-input" 
                        type="file" 
                        name="arquivo" 
                        required 
                        onChange={lidarComMudancaArquivo}
                    />
                    <button className="busca-crud__botao" type="submit" disabled={enviando}>
                        {enviando ? 'Enviando...' : 'Enviar para Google Drive'}
                    </button>
                </form>

                {!arquivosPedido || arquivosPedido.length === 0 ? (
                    <div className="pedido-visualizacao__arquivos-placeholder">
                        Nenhum arquivo vinculado a este pedido.
                    </div>
                ) : (
                    <ul className="pedido-visualizacao__arquivos-lista">
                        {arquivosPedido.map((arquivo) => (
                            <li key={arquivo.id} className="pedido-visualizacao__arquivo-item" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
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
                                    <button 
                                        className="acoes-tabela__botao acoes-tabela__botao--excluir" 
                                        type="button"
                                        style={{ padding: '2px 8px', fontSize: '0.8rem', height: 'auto', lineHeight: 1 }}
                                        onClick={() => lidarComExclusaoArquivo(arquivo.id)}
                                    >
                                        Excluir
                                    </button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </section>

            <BlocoTarefas extChave="pedido_id" extId={pedidoId} />
        </section>
    );
}
