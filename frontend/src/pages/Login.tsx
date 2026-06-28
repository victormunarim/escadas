import type React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { usarAutenticacao } from '../context/AuthContext';

export default function Login() {
    const { entrar, usuario } = usarAutenticacao();
    const navegar = useNavigate();
    const localizacao = useLocation();
    
    const [usuarioNome, setUsuarioNome] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);

    useEffect(() => {
        if (usuario) {
            navegar('/pedidos', { replace: true });
        }
    }, [usuario, navegar]);

    const parametros = new URLSearchParams(localizacao.search);
    const ehSaida = parametros.has('logout');

    const lidarComEnvio = async (e: React.FormEvent) => {
        e.preventDefault();
        setErro('');
        setCarregando(true);

        try {
            await entrar(usuarioNome, senha);
            navegar('/pedidos');
        } catch (erroRequisicao: any) {
            setErro(erroRequisicao.message || 'Credenciais inválidas.');
        } finally {
            setCarregando(false);
        }
    };

    return (
        <div className="login-pagina">
            <div className="login-container">
                <div className="login-cartao">
                    <div className="login-marca marca">
                        <span className="marca__simbolo">E</span>
                        <div className="marca__texto">
                            <span className="marca__titulo">Escadas Munarim</span>
                            <span className="marca__subtitulo">Painel Administrativo</span>
                        </div>
                    </div>

                    <h1 className="login-titulo">Entrar</h1>
                    <p className="login-subtitulo">Acesse para gerenciar os pedidos.</p>

                    {erro && (
                        <div className="login-alerta login-alerta--erro">
                            {erro}
                        </div>
                    )}

                    {ehSaida && !erro && (
                        <div className="login-alerta login-alerta--ok">
                            Você saiu com sucesso.
                        </div>
                    )}

                    <form className="login-formulario" onSubmit={lidarComEnvio}>
                        <div className="filtro-crud">
                            <label className="login-label" htmlFor="username">Login</label>
                            <input
                                className="login-input"
                                id="username"
                                type="text"
                                value={usuarioNome}
                                onChange={(e) => setUsuarioNome(e.target.value)}
                                required
                                autoComplete="username"
                            />
                        </div>

                        <div className="filtro-crud">
                            <label className="login-label" htmlFor="password">Senha</label>
                            <input
                                className="login-input"
                                id="password"
                                type="password"
                                value={senha}
                                onChange={(e) => setSenha(e.target.value)}
                                required
                                autoComplete="current-password"
                            />
                        </div>

                        <button className="login-botao" type="submit" disabled={carregando}>
                            {carregando ? 'Entrando...' : 'Entrar'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}
