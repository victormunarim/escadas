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
    const ehTecnico = window.location.pathname.startsWith('/tecnicos');
    const baseApiUrl = ehTecnico ? '/api/tecnicos' : '/api/orcamentos';
    const baseRedirectUrl = ehTecnico ? '/tecnicos' : '/orcamentos';
    const tituloModulo = ehTecnico ? 'Técnico' : 'Orçamento';

    const [salvando, setSalvando] = useState(false);
    const [campos, setCampos] = useState<CampoRender[] | null>(null);

    useEffect(() => {
        const carregarCampos = async () => {
            try {
                const url = ehEdicao ? `${baseApiUrl}/${id}/formulario` : `${baseApiUrl}/formulario`;
                const dados = await requisicaoApi<CampoRender[]>(url);
                setCampos(dados);
            } catch (erroRequisicao: any) {
                alert(erroRequisicao.message || 'Erro ao carregar os campos do formulário.');
                setCampos([]);
            }
        };

        carregarCampos();
    }, [ehEdicao, id, baseApiUrl]);

    const lidarComEnvio = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setSalvando(true);

        const formData = new FormData(e.currentTarget);
        const payload: Record<string, any> = {};
        formData.forEach((valor, chave) => {
            payload[chave] = valor;
        });

        try {
            const url = ehEdicao ? `${baseApiUrl}/${id}` : baseApiUrl;
            const metodo = ehEdicao ? 'PUT' : 'POST';
            const resposta = await requisicaoApi<{ id?: number }>(url, {
                method: metodo,
                body: payload
            });
            const targetId = ehEdicao ? id : (resposta?.id);
            const temPedidoAssociado = payload.pedidoId !== undefined
                && payload.pedidoId !== null
                && String(payload.pedidoId).trim() !== '';
            const finalRedirectBase = (ehTecnico || temPedidoAssociado) ? '/tecnicos' : '/orcamentos';
            if (targetId) {
                window.location.href = `${finalRedirectBase}/${targetId}/visualizar`;
            } else {
                window.location.href = finalRedirectBase;
            }
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || `Erro ao salvar ${tituloModulo.toLowerCase()}.`);
        } finally {
            setSalvando(false);
        }
    };

    if (!campos) {
        return <Carregando mensagem="Carregando formulário..." margemSuperior />;
    }

    const subtituloText = ehEdicao
        ? `Atualize os dados do ${tituloModulo.toLowerCase()} selecionado.`
        : `Preencha os campos para cadastrar o ${tituloModulo.toLowerCase()}.`;

    const textoBotao = salvando
        ? 'Salvando...'
        : ehEdicao
            ? 'Salvar alterações'
            : `Salvar ${tituloModulo.toLowerCase()}`;

    return (
        <section className="pagina">
            <header className="pagina__cabecalho">
                <h1 className="pagina__titulo">{ehEdicao ? `Editar ${tituloModulo}` : `Novo ${tituloModulo}`}</h1>
                <p className="pagina__subtitulo">{subtituloText}</p>
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
                            {textoBotao}
                        </button>
                        <Link className="formulario-crud__link" to={baseRedirectUrl}>Voltar para listagem</Link>
                    </div>
                </form>
            </section>
        </section>
    );
}
