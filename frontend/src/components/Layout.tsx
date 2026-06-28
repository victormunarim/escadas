import { Outlet, Navigate } from 'react-router-dom';
import { usarAutenticacao } from '../context/AuthContext';
import Cabecalho from './Cabecalho';
import Rodape from './Rodape';

export default function Layout() {
    const { usuario } = usarAutenticacao();

    if (usuario === undefined) {
        return (
            <div style={{ display: 'grid', placeItems: 'center', height: '100vh', fontFamily: 'sans-serif' }}>
                <h3>Carregando...</h3>
            </div>
        );
    }

    if (!usuario) {
        return <Navigate to="/login" replace />;
    }

    return (
        <div className="aplicativo">
            <Cabecalho />
            <main className="aplicativo__principal">
                <Outlet />
            </main>
            <Rodape />
        </div>
    );
}
