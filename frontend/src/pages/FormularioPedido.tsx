import type React from 'react';
import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';
import { CampoRenderizador } from '../components/Campos';
import type { CampoRender } from '../types/crud';

export default function FormularioPedido() {
    const { id } = useParams<{ id: string }>();
    const ehEdicao = !!id;

    const [salvando, setSalvando] = useState(false);
    const [campos, setCampos] = useState<CampoRender[] | null>(null);

    useEffect(() => {
        const carregarCampos = async () => {
            try {
                const url = ehEdicao ? `/api/pedidos/${id}/formulario` : '/api/pedidos/formulario';
                const dados = await requisicaoApi<CampoRender[]>(url);
                setCampos(dados);
            } catch (erroRequisicao: any) {
                alert(erroRequisicao.message || 'Erro ao carregar os campos do formulário.');
                setCampos([]);
            }
        };

        carregarCampos();
    }, [ehEdicao, id]);

    const formatarApenasDigitos = (e: React.ChangeEvent<HTMLInputElement>, maxLen: number) => {
        const val = e.target.value.replace(/\D/g, '');
        e.target.value = val.substring(0, maxLen);
    };

    const formatarCep = (e: React.ChangeEvent<HTMLInputElement>) => {
        let val = e.target.value.replace(/\D/g, '');
        if (val.length > 8) {
            val = val.substring(0, 8);
        }
        if (val.length > 5) {
            val = val.substring(0, 5) + '-' + val.substring(5);
        }
        e.target.value = val;
    };

    const lidarComMudancaUf = async (nomeCampo: string, valorUf: string) => {
        if (!campos) return;

        let nomeMunicipioAlvo = nomeCampo === 'uf' ? 'municipio' : 'municipioCliente';
        let nomeBairroAlvo = nomeCampo === 'uf' ? 'bairro' : 'bairroCliente';

        try {
            const [dadosMunicipios, dadosBairros] = await Promise.all([
                requisicaoApi<string[]>(`/api/localidades/municipios?uf=${valorUf}`),
                requisicaoApi<string[]>(`/api/localidades/bairros?uf=${valorUf}`)
            ]);

            const novosCampos = campos.map(c => {
                if (c.name === nomeCampo) {
                    return { ...c, value: valorUf };
                }
                if (c.name === nomeMunicipioAlvo) {
                    return {
                        ...c,
                        value: '',
                        options: dadosMunicipios.map(m => ({ valor: m, rotulo: m }))
                    };
                }
                if (c.name === nomeBairroAlvo) {
                    return {
                        ...c,
                        value: '',
                        options: dadosBairros.map(b => ({ valor: b, rotulo: b }))
                    };
                }
                return c;
            });

            setCampos(novosCampos);
        } catch (err: any) {
            console.error('Erro ao recarregar cidades/bairros:', err);
        }
    };

    const lidarComEnvio = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setSalvando(true);

        const formData = new FormData(e.currentTarget);
        const payload: Record<string, any> = {};
        formData.forEach((valor, chave) => {
            if (chave === 'revestimento') {
                payload[chave] = (valor === 'true');
            } else {
                payload[chave] = valor;
            }
        });

        try {
            const url = ehEdicao ? `/api/pedidos/${id}` : '/api/pedidos';
            const metodo = ehEdicao ? 'PUT' : 'POST';
            await requisicaoApi(url, {
                method: metodo,
                body: JSON.stringify(payload)
            });
            alert(ehEdicao ? 'Pedido atualizado com sucesso!' : 'Pedido cadastrado com sucesso!');
            window.location.href = '/pedidos';
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao salvar o pedido.');
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
                <h1 className="pagina__titulo">{ehEdicao ? 'Editar Pedido' : 'Novo Pedido'}</h1>
                <p className="pagina__subtitulo">
                    {ehEdicao ? 'Atualize os dados do pedido selecionado.' : 'Preencha todos os campos para inserir na tabela.'}
                </p>
            </header>

            <section className="cartao-crud">
                <form className="formulario-crud" onSubmit={lidarComEnvio}>
                    <div className="formulario-crud__grade">
                        {campos.map((campo) => (
                            <CampoRenderizador
                                key={campo.name || campo.rotulo}
                                campo={campo}
                                onChange={
                                    (campo.name === 'uf' || campo.name === 'ufCliente')
                                        ? (e) => lidarComMudancaUf(campo.name!, e.target.value)
                                        : (campo.name === 'cpf' || campo.name === 'rg')
                                        ? (e) => formatarApenasDigitos(e, campo.name === 'cpf' ? 14 : 12)
                                        : (campo.name === 'telefone' || campo.name === 'telefoneFixo' || campo.name === 'numero' || campo.name === 'numeroCliente')
                                        ? (e) => formatarApenasDigitos(e, 20)
                                        : (campo.name === 'cep' || campo.name === 'cepCliente')
                                        ? formatarCep
                                        : undefined
                                }
                            />
                        ))}
                    </div>

                    <div className="formulario-crud__acoes" style={{ marginTop: '16px' }}>
                        <button className="busca-crud__botao" type="submit" disabled={salvando}>
                            {salvando ? 'Salvando...' : ehEdicao ? 'Salvar alterações' : 'Salvar pedido'}
                        </button>
                        <Link className="formulario-crud__link" to="/pedidos">Voltar para listagem</Link>
                    </div>
                </form>
            </section>
        </section>
    );
}
