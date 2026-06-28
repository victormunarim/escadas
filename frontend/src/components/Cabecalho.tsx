import type React from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { usarAutenticacao } from '../context/AuthContext';

export default function Cabecalho() {
    const { usuario, sair } = usarAutenticacao();
    const navegar = useNavigate();

    const lidarComSaida = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await sair();
            navegar('/login');
        } catch (erro) {
            console.error(erro);
        }
    };

    return (
        <nav className="cabecalho-site" aria-label="Navegação principal">
            <div className="cabecalho-site__wrap">
                <Link to="/" className="marca" aria-label="Página inicial">
                    <span className="marca__simbolo">E</span>
                    <span className="marca__texto">
                        <span className="marca__titulo">Escadas Munarim</span>
                    </span>
                </Link>

                <div className="navegacao">
                    <NavLink to="/pedidos" end>Pedidos</NavLink>
                    <NavLink to="/pedidos/novo">Novo Pedido</NavLink>
                    <NavLink to="/token">Google Drive</NavLink>
                </div>

                <div className="usuario-barra">
                    {usuario && <span className="usuario-nome">{usuario.username}</span>}
                    <form onSubmit={lidarComSaida}>
                        <button type="submit" className="usuario-sair">Sair</button>
                    </form>
                </div>
            </div>
        </nav>
    );
}
