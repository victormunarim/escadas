interface CarregandoProps {
    mensagem?: string;
    margemSuperior?: boolean;
}

export default function Carregando({ mensagem = 'Carregando...', margemSuperior = false }: CarregandoProps) {
    return (
        <div className="vazio-crud" style={margemSuperior ? { marginTop: '40px' } : undefined}>
            <div className="vazio-crud__titulo">{mensagem}</div>
        </div>
    );
}
