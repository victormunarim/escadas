export interface OpcoesRequisicaoApi extends Omit<RequestInit, 'body'> {
    body?: any;
}

export async function requisicaoApi<T = any>(url: string, opcoes: OpcoesRequisicaoApi = {}): Promise<T> {
    opcoes.credentials = 'include';
    
    const opcoesRequisicao = { ...opcoes } as any;
    
    if (
        opcoesRequisicao.body &&
        typeof opcoesRequisicao.body === 'object' &&
        !(opcoesRequisicao.body instanceof FormData) &&
        !(opcoesRequisicao.body instanceof URLSearchParams)
    ) {
        opcoesRequisicao.headers = {
            'Content-Type': 'application/json',
            ...opcoesRequisicao.headers,
        };
        opcoesRequisicao.body = JSON.stringify(opcoesRequisicao.body);
    }
    
    const resposta = await fetch(url, opcoesRequisicao as RequestInit);
    if (!resposta.ok) {
        if (resposta.status === 401) {
            throw new Error('UNAUTHORIZED');
        }
        const texto = await resposta.text();
        try {
            const json = JSON.parse(texto);
            throw new Error(json.erro || json.message || 'Ocorreu um erro.');
        } catch {
            throw new Error(texto || 'Erro na requisição.');
        }
    }
    const texto = await resposta.text();
    return texto ? (JSON.parse(texto) as T) : (null as any);
}
