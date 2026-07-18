import type { ReactNode } from 'react';
import { usarAutenticacao } from '../context/AuthContext';
import AvisoSemPermissao from './AvisoSemPermissao';

interface PropriedadesRotaProtegida {
    permissaoRequerida: string;
    children: ReactNode;
}

export default function RotaProtegida({ permissaoRequerida, children }: PropriedadesRotaProtegida) {
    const { usuario, temPermissao } = usarAutenticacao();

    if (usuario === undefined) {
        return null; // Layout will handle loading
    }

    if (!usuario) {
        return null; // Layout will handle redirection
    }

    if (!temPermissao(permissaoRequerida)) {
        return <AvisoSemPermissao permissao={permissaoRequerida} />;
    }

    return <>{children}</>;
}
