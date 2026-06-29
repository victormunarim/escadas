import type React from 'react';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import { CampoRenderizador } from '../components/Campos';
import type { CampoRender } from '../types/crud';

export default function FormularioTarefa() {
    const { id } = useParams<{ id: string }>();
    const ehEdicao = id !== undefined;
    const [campos, setCampos] = useState<CampoRender[] | null>(null);
    const [salvando, setSalvando] = useState(false);

    useEffect(() => {
        const carregarCampos = async () => {
            try {
                const url = ehEdicao ? `/api/tarefas/${id}/formulario` : '/api/tarefas/formulario';
                let dados = await requisicaoApi<CampoRender[]>(url);
                
                if (!ehEdicao) {
                    const searchParams = new URLSearchParams(window.location.search);
                    const extChave = searchParams.get('extChave');
                    const extId = searchParams.get('extId');
                    dados = dados.map(campo => {
                        if (campo.name === 'extChave' && extChave) {
                            return { ...campo, value: extChave };
                        }
                        if (campo.name === 'extId' && extId) {
                            return { ...campo, value: extId };
                        }
                        return campo;
                    });
                }
                
                setCampos(dados);
            } catch (erroRequisicao: any) {
                alert(erroRequisicao.message || 'Erro ao carregar campos do formulário.');
            }
        };

        carregarCampos();
    }, [id, ehEdicao]);

    const lidarComEnvio = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setSalvando(true);

        const formData = new FormData(e.currentTarget);
        const payload: Record<string, any> = {};
        formData.forEach((valor, chave) => {
            payload[chave] = valor;
        });

        const searchParams = new URLSearchParams(window.location.search);
        const extChave = searchParams.get('extChave');
        const extId = searchParams.get('extId');

        if (!ehEdicao) {
            if (extChave) payload.extChave = extChave;
            if (extId) payload.extId = Number(extId);
        }

        try {
            const url = ehEdicao ? `/api/tarefas/${id}` : '/api/tarefas';
            const metodo = ehEdicao ? 'PUT' : 'POST';
            await requisicaoApi(url, {
                method: metodo,
                body: payload
            });
            if (extChave === 'pedido_id' && extId) {
                window.location.href = `/pedidos/${extId}/visualizar`;
            } else if (extChave === 'orcamento_id' && extId) {
                window.location.href = `/orcamentos/${extId}/visualizar`;
            } else {
                window.location.href = '/tarefas';
            }
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao salvar a tarefa.');
        } finally {
            setSalvando(false);
        }
    };

    if (!campos) {
        return <Carregando mensagem="Carregando formulário..." margemSuperior />;
    }

    return (
        <section className="pagina">
            <header className="pagina__cabecalho">
                <h1 className="pagina__titulo">{ehEdicao ? 'Editar Tarefa' : 'Nova Tarefa'}</h1>
            </header>

            <section className="cartao-crud">
                <form className="formulario-crud" onSubmit={lidarComEnvio}>
                    <div className="formulario-crud__campos">
                        {campos.map((campo, idx) => (
                            <CampoRenderizador
                                key={campo.name || idx}
                                campo={campo}
                            />
                        ))}
                    </div>

                    <div className="formulario-crud__acoes">
                        <button className="busca-crud__botao" type="submit" disabled={salvando}>
                            {salvando ? 'Salvando...' : 'Salvar'}
                        </button>
                        <button 
                            type="button"
                            className="formulario-crud__link"
                            style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 0, font: 'inherit', color: 'var(--cor-texto-secundario)' }}
                            onClick={() => {
                                const searchParams = new URLSearchParams(window.location.search);
                                const extChave = searchParams.get('extChave');
                                const extId = searchParams.get('extId');
                                if (extChave === 'pedido_id' && extId) {
                                    window.location.href = `/pedidos/${extId}/visualizar`;
                                } else if (extChave === 'orcamento_id' && extId) {
                                    window.location.href = `/orcamentos/${extId}/visualizar`;
                                } else {
                                    window.location.href = '/tarefas';
                                }
                            }}
                        >
                            Cancelar
                        </button>
                    </div>
                </form>
            </section>
        </section>
    );
}
