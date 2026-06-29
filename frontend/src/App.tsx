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
import ListagemTarefas from './pages/ListagemTarefas';
import FormularioTarefa from './pages/FormularioTarefa';
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
                        
                        <Route path="/orcamentos" element={<ListagemOrcamentos />} />
                        <Route path="/orcamentos/novo" element={<FormularioOrcamento />} />
                        <Route path="/orcamentos/:id/editar" element={<FormularioOrcamento />} />
                        <Route path="/orcamentos/:id/visualizar" element={<VisualizacaoOrcamento />} />

                        <Route path="/tarefas" element={<ListagemTarefas />} />
                        <Route path="/tarefas/novo" element={<FormularioTarefa />} />
                        <Route path="/tarefas/:id/editar" element={<FormularioTarefa />} />
                        
                        <Route path="/token" element={<ConfiguracaoGoogleDrive />} />
                    </Route>

                    {/* Redirecionamento Geral */}
                    <Route path="*" element={<Navigate to="/pedidos" replace />} />
                </Routes>
            </BrowserRouter>
        </ProvedorAutenticacao>
    );
}
