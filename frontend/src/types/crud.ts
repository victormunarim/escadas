export interface OpcaoCrud {
    valor: string;
    rotulo: string;
}

export interface CampoRender {
    tipo: 'input' | 'select' | 'textarea' | 'secao';
    rotulo?: string;
    name?: string;
    classeCampo?: string;
    type?: string;
    required?: boolean;
    min?: string;
    max?: string;
    maxLength?: number;
    step?: string;
    inputMode?: any;
    placeholder?: string;
    value?: any;
    options?: OpcaoCrud[];
    rows?: number;
}

export interface ColunaListagem {
    chave: string;
    label: string;
}

export interface LinhaListagem {
    id: number;
    valores: Record<string, any>;
}

export interface ListagemResumo {
    colunas: ColunaListagem[];
    linhas: LinhaListagem[];
    filtros: CampoRender[];
}
