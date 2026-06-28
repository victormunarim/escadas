import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ProvedorAutenticacao } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import ListagemPedidos from './pages/ListagemPedidos';
import FormularioPedido from './pages/FormularioPedido';
import VisualizacaoPedido from './pages/VisualizacaoPedido';
import ConfiguracaoGoogleDrive from './pages/ConfiguracaoGoogleDrive';

export default function App() {
    return (
        <ProvedorAutenticacao>
            <BrowserRouter>
                <Routes>
                    {/* Rota Publica */}
                    <Route path="/login" element={<Login />} />

                    {/* Rotas Protegidas dentro do Layout */}
                    <Route element={<Layout />}>
                        <Route path="/" element={<Navigate to="/pedidos" replace />} />
                        <Route path="/pedidos" element={<ListagemPedidos />} />
                        <Route path="/pedidos/novo" element={<FormularioPedido />} />
                        <Route path="/pedidos/:id/editar" element={<FormularioPedido />} />
                        <Route path="/pedidos/:id/visualizar" element={<VisualizacaoPedido />} />
                        <Route path="/token" element={<ConfiguracaoGoogleDrive />} />
                    </Route>

                    {/* Redirecionamento Geral */}
                    <Route path="*" element={<Navigate to="/pedidos" replace />} />
                </Routes>
            </BrowserRouter>
        </ProvedorAutenticacao>
    );
}
