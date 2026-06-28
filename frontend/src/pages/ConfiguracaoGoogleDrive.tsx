import type React from 'react';
import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { requisicaoApi } from '../api';
import Carregando from '../components/Carregando';

interface DadosConfiguracaoGoogleDrive {
    tokenConfigurado: boolean;
    nomeToken: string;
    credenciaisConfiguradas: boolean;
    clientId?: string;
    projectId?: string;
    parentFolder?: string;
}

export default function ConfiguracaoGoogleDrive() {
    const localizacao = useLocation();
    const [configuracao, setConfiguracao] = useState<DadosConfiguracaoGoogleDrive | null | undefined>(undefined);
    const [salvando, setSalvando] = useState(false);
    
    const [sucesso, setSucesso] = useState('');
    const [erro, setErro] = useState('');

    const buscarConfiguracao = async () => {
        try {
            const dados = await requisicaoApi<DadosConfiguracaoGoogleDrive>('/api/token/config');
            setConfiguracao(dados);
        } catch (erroRequisicao: any) {
            setErro(erroRequisicao.message || 'Erro ao carregar configurações do Google Drive.');
            setConfiguracao(null);
        }
    };

    useEffect(() => {
        buscarConfiguracao();

        const parametros = new URLSearchParams(localizacao.search);
        if (parametros.has('sucesso')) {
            setSucesso(parametros.get('sucesso') || '');
        }
        if (parametros.has('erro')) {
            setErro(parametros.get('erro') || '');
        }
    }, [localizacao.search]);

    const lidarComEnvio = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setSalvando(true);

        const dadosFormulario = new FormData(e.currentTarget);
        const parametros = new URLSearchParams();
        
        const clientIdVal = dadosFormulario.get('clientId') as string;
        const projectIdVal = dadosFormulario.get('projectId') as string;
        const clientSecretVal = dadosFormulario.get('clientSecret') as string;
        const pastaDestinoVal = dadosFormulario.get('parentFolder') as string;

        if (clientIdVal) parametros.append('clientId', clientIdVal);
        if (projectIdVal) parametros.append('projectId', projectIdVal);
        if (clientSecretVal) parametros.append('clientSecret', clientSecretVal);
        if (pastaDestinoVal) parametros.append('parentFolder', pastaDestinoVal);

        try {
            await requisicaoApi<{ sucesso?: string }>('/api/token/credenciais', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: parametros,
            });
            window.location.href = '/token?sucesso=Credenciais+do+Google+Drive+atualizadas.';
        } catch (erroRequisicao: any) {
            alert(erroRequisicao.message || 'Erro ao salvar credenciais.');
            setSalvando(false);
        }
    };

    const lidarComVinculoConta = () => {
        window.location.href = '/oauth/google/start';
    };

    if (configuracao === undefined) {
        return <Carregando mensagem="Carregando configurações..." margemSuperior />;
    }

    const tokenConfigurado = configuracao?.tokenConfigurado;
    const credenciaisConfiguradas = configuracao?.credenciaisConfiguradas;

    return (
        <section className="pagina">
            <header className="pagina__cabecalho">
                <h1 className="pagina__titulo">Configurações do Google Drive</h1>
                <p className="pagina__subtitulo">Configure o envio de anexos das vistorias diretamente para sua nuvem.</p>
            </header>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px', marginTop: '16px' }}>
                
                <section className="cartao-crud" style={{ marginTop: 0 }}>
                    <h2 className="pedido-visualizacao__titulo" style={{ borderBottom: '1px solid rgba(19, 80, 140, .12)', paddingBottom: '6px' }}>
                        Integração Google API
                    </h2>
                    
                    <div style={{ margin: '14px 0' }}>
                        <div style={{ fontWeight: '700', fontSize: '13px', color: 'var(--text-600)' }}>Status do Token</div>
                        <div style={{ marginTop: '4px' }}>
                            <span className={`rodape-selo`} style={{ 
                                background: tokenConfigurado ? 'rgba(22, 163, 74, 0.1)' : 'rgba(220, 38, 38, 0.1)',
                                color: tokenConfigurado ? '#166534' : '#991b1b',
                                borderColor: tokenConfigurado ? 'rgba(22, 163, 74, 0.22)' : 'rgba(220, 38, 38, 0.22)'
                            }}>
                                {tokenConfigurado ? 'Conectado / Configurado' : 'Pendente / Não Autorizado'}
                            </span>
                        </div>
                    </div>

                    <div style={{ margin: '14px 0' }}>
                        <p className="rodape-texto" style={{ marginBottom: '14px' }}>
                            {tokenConfigurado 
                                ? 'O Google Drive está devidamente vinculado e pronto para o envio de vistorias.'
                                : 'É necessário autorizar a conexão com a conta Google para permitir o upload dos PDFs e imagens.'
                            }
                        </p>
                        
                        {!tokenConfigurado && (
                            <button 
                                className="busca-crud__botao" 
                                style={{ width: '100%', display: 'block' }}
                                onClick={lidarComVinculoConta}
                                disabled={!credenciaisConfiguradas}
                                title={!credenciaisConfiguradas ? 'Salve as credenciais primeiro' : ''}
                            >
                                Vincular Conta Google
                            </button>
                        )}
                    </div>
                </section>

                <section className="cartao-crud" style={{ marginTop: 0 }}>
                    <h2 className="pedido-visualizacao__titulo" style={{ borderBottom: '1px solid rgba(19, 80, 140, .12)', paddingBottom: '6px' }}>
                        Configurar Credenciais
                    </h2>

                    {sucesso && <div className="alerta-formulario alerta-formulario--sucesso" style={{ margin: '10px 0' }}>{sucesso}</div>}
                    {erro && <div className="alerta-formulario alerta-formulario--erro" style={{ margin: '10px 0' }}>{erro}</div>}

                    <form className="formulario-crud" onSubmit={lidarComEnvio} style={{ marginTop: '12px' }}>
                        <div className="formulario-crud__grade" style={{ gridTemplateColumns: '1fr' }}>
                            
                            <label className="filtro-crud">
                                <span className="filtro-crud__rotulo">Client ID *</span>
                                <input
                                    className="filtro-crud__entrada"
                                    name="clientId"
                                    type="text"
                                    required
                                    defaultValue={configuracao?.clientId || ''}
                                />
                            </label>

                            <label className="filtro-crud">
                                <span className="filtro-crud__rotulo">Project ID *</span>
                                <input
                                    className="filtro-crud__entrada"
                                    name="projectId"
                                    type="text"
                                    required
                                    defaultValue={configuracao?.projectId || ''}
                                />
                            </label>

                            <label className="filtro-crud">
                                <span className="filtro-crud__rotulo">Client Secret</span>
                                <input
                                    className="filtro-crud__entrada"
                                    name="clientSecret"
                                    type="password"
                                    placeholder={credenciaisConfiguradas ? "Mantenha vazio para não alterar" : "Senha secreta do cliente"}
                                    required={!credenciaisConfiguradas}
                                    defaultValue=""
                                />
                            </label>

                            <label className="filtro-crud">
                                <span className="filtro-crud__rotulo">Pasta Objeto de Envio (ID Google Drive) *</span>
                                <input
                                    className="filtro-crud__entrada"
                                    name="parentFolder"
                                    type="text"
                                    required
                                    defaultValue={configuracao?.parentFolder || ''}
                                />
                            </label>

                        </div>

                        <button className="busca-crud__botao" type="submit" style={{ width: '100%' }} disabled={salvando}>
                            {salvando ? 'Salvando...' : 'Salvar Credenciais'}
                        </button>
                    </form>
                </section>

            </div>
        </section>
    );
}
