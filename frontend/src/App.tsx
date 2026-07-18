import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ProvedorAutenticacao } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import ListagemPedidos from './pages/ListagemPedidos';
import FormularioPedido from './pages/FormularioPedido';
import VisualizacaoPedido from './pages/VisualizacaoPedido';
import ListagemOrcamentos from './pages/ListagemOrcamentos';
import FormularioOrcamento from './pages/FormularioOrcamento';
import VisualizacaoOrcamento from './pages/VisualizacaoOrcamento';
import ConfiguracaoGoogleDrive from './pages/ConfiguracaoGoogleDrive';
import RotaProtegida from './components/RotaProtegida';

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
                        <Route path="/pedidos" element={<RotaProtegida permissaoRequerida="PEDIDOS_VISUALIZAR"><ListagemPedidos /></RotaProtegida>} />
                        <Route path="/pedidos/novo" element={<RotaProtegida permissaoRequerida="PEDIDOS_ADICIONAR"><FormularioPedido /></RotaProtegida>} />
                        <Route path="/pedidos/:id/editar" element={<RotaProtegida permissaoRequerida="PEDIDOS_EDITAR"><FormularioPedido /></RotaProtegida>} />
                        <Route path="/pedidos/:id/visualizar" element={<RotaProtegida permissaoRequerida="PEDIDOS_VISUALIZAR"><VisualizacaoPedido /></RotaProtegida>} />
                        
                        <Route path="/orcamentos" element={<RotaProtegida permissaoRequerida="ORCAMENTOS_VISUALIZAR"><ListagemOrcamentos /></RotaProtegida>} />
                        <Route path="/orcamentos/novo" element={<RotaProtegida permissaoRequerida="ORCAMENTOS_ADICIONAR"><FormularioOrcamento /></RotaProtegida>} />
                        <Route path="/orcamentos/:id/editar" element={<RotaProtegida permissaoRequerida="ORCAMENTOS_EDITAR"><FormularioOrcamento /></RotaProtegida>} />
                        <Route path="/orcamentos/:id/visualizar" element={<RotaProtegida permissaoRequerida="ORCAMENTOS_VISUALIZAR"><VisualizacaoOrcamento /></RotaProtegida>} />
                        
                        <Route path="/token" element={<RotaProtegida permissaoRequerida="CONFIGURACAO_DRIVE"><ConfiguracaoGoogleDrive /></RotaProtegida>} />
                    </Route>

                    {/* Redirecionamento Geral */}
                    <Route path="*" element={<Navigate to="/pedidos" replace />} />
                </Routes>
            </BrowserRouter>
        </ProvedorAutenticacao>
    );
}
