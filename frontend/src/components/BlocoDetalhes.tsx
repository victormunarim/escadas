interface BlocoDetalhesProps {
    titulo: string;
    dados: Record<string, string>;
}

export default function BlocoDetalhes({ titulo, dados }: BlocoDetalhesProps) {
    return (
        <section className="cartao-crud pedido-visualizacao__bloco">
            <h2 className="pedido-visualizacao__titulo">{titulo}</h2>
            <dl className="pedido-visualizacao__lista">
                {Object.entries(dados || {}).map(([chave, valor]) => (
                    <div key={chave} className="pedido-visualizacao__item">
                        <dt>{chave}</dt>
                        <dd>{!valor || valor.trim() === '' ? '-' : valor}</dd>
                    </div>
                ))}
            </dl>
        </section>
    );
}
