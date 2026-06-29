import type React from 'react';
import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import { CampoRenderizador } from '../components/Campos';
import type { CampoRender } from '../types/crud';

export default function FormularioOrcamento() {
    const { id } = useParams<{ id: string }>();
    const ehEdicao = !!id;

    const [salvando, setSalvando] = useState(false);
    const [campos, setCampos] = useState<CampoRender[] | null>(null);

    useEffect(() => {
        const carregarCampos = async () => {
            try {
                const url = ehEdicao ? `/api/orcamentos/${id}/formulario` : '/api/orcamentos/formulario';
                const dados = await requisicaoApi<CampoRender[]>(url);
                setCampos(dados);
            } catch (erroRequisicao: any) {
                alert(erroRequisicao.message || 'Erro ao carregar os campos do formulário.');
                setCampos([]);
            }
        };

        carregarCampos();
    }, [ehEdicao, id]);

    const lidarComEnvio = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setSalvando(true);

        const formData = new FormData(e.currentTarget);
        const payload: Record<string, any> = {};
        formData.forEach((valor, chave) => {
            payload[chave] = valor;
        });

        try {
            const url = ehEdicao ? `/api/orcamentos/${id}` : '/api/orcamentos';
            const metodo = ehEdicao ? 'PUT' : 'POST';
            const resposta = await requisicaoApi<{ id?: number }>(url, {
                method: metodo,
                body: payload
            });
            const targetId = ehEdicao ? id : (resposta?.id);
            if (targetId) {
                window.location.href = `/orcamentos/${targetId}/visualizar`;
            } else {
                window.location.href = '/orcamentos';
            }
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao salvar o orçamento.');
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
                <h1 className="pagina__titulo">{ehEdicao ? 'Editar Orçamento' : 'Novo Orçamento'}</h1>
                <p className="pagina__subtitulo">
                    {ehEdicao ? 'Atualize os dados do orçamento selecionado.' : 'Preencha os campos para cadastrar o orçamento.'}
                </p>
            </header>

            <section className="cartao-crud">
                <form className="formulario-crud" onSubmit={lidarComEnvio}>
                    <div className="formulario-crud__grade">
                        {campos.map((campo) => (
                            <CampoRenderizador
                                key={campo.name || campo.rotulo}
                                campo={campo}
                            />
                        ))}
                    </div>

                    <div className="formulario-crud__acoes" style={{ marginTop: '16px' }}>
                        <button className="busca-crud__botao" type="submit" disabled={salvando}>
                            {salvando ? 'Salvando...' : ehEdicao ? 'Salvar alterações' : 'Salvar orçamento'}
                        </button>
                        <Link className="formulario-crud__link" to="/orcamentos">Voltar para listagem</Link>
                    </div>
                </form>
            </section>
        </section>
    );
}
