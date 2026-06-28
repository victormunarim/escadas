import { createContext, useState, useEffect, useContext, type ReactNode } from 'react';
import { requisicaoApi } from '../api';

export interface Usuario {
    username: string;
}

export interface TipoContextoAutenticacao {
    usuario: Usuario | null | undefined;
    entrar: (usuarioNome: string, senha: string) => Promise<void>;
    sair: () => Promise<void>;
    verificarAutenticacao: () => Promise<void>;
}

const ContextoAutenticacao = createContext<TipoContextoAutenticacao | null>(null);

export function ProvedorAutenticacao({ children }: { children: ReactNode }) {
    const [usuario, setUsuario] = useState<Usuario | null | undefined>(undefined);

    const verificarAutenticacao = async () => {
        try {
            const dados = await requisicaoApi<Usuario>('/api/auth/me');
            setUsuario(dados);
        } catch (erro) {
            setUsuario(null);
        }
    };

    useEffect(() => {
        verificarAutenticacao();
    }, []);

    const entrar = async (usuarioNome: string, senha: string) => {
        const dadosFormulario = new URLSearchParams();
        dadosFormulario.append('username', usuarioNome);
        dadosFormulario.append('password', senha);

        await requisicaoApi('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: dadosFormulario,
        });
        await verificarAutenticacao();
    };

    const sair = async () => {
        await requisicaoApi('/logout', { method: 'POST' });
        setUsuario(null);
    };

    return (
        <ContextoAutenticacao.Provider value={{ usuario, entrar, sair, verificarAutenticacao }}>
            {children}
        </ContextoAutenticacao.Provider>
    );
}

export function usarAutenticacao() {
    const contexto = useContext(ContextoAutenticacao);
    if (!contexto) {
        throw new Error('usarAutenticacao deve ser usado dentro de um ProvedorAutenticacao');
    }
    return contexto;
}
