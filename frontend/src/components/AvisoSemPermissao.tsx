import { Link } from 'react-router-dom';

interface PropriedadesAvisoSemPermissao {
    permissao?: string;
}

export default function AvisoSemPermissao({ permissao }: PropriedadesAvisoSemPermissao) {
    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            padding: '40px 20px',
            textAlign: 'center',
            background: 'var(--card)',
            borderRadius: 'var(--radius)',
            border: '1px solid rgba(220, 38, 38, 0.2)',
            boxShadow: 'var(--shadow)',
            maxWidth: '500px',
            margin: '40px auto 0 auto',
            fontFamily: 'sans-serif'
        }}>
            <div style={{
                fontSize: '48px',
                marginBottom: '16px',
                color: '#dc2626'
            }}>
                ⚠️
            </div>
            <h2 style={{
                margin: '0 0 10px 0',
                color: 'var(--blue-900)',
                fontWeight: 900
            }}>
                Acesso Restrito
            </h2>
            <p style={{
                margin: '0 0 20px 0',
                color: 'var(--text-600)',
                fontSize: '15px',
                lineHeight: '1.5'
            }}>
                Você não possui a permissão necessária para acessar esta página.
                {permissao && (
                    <span style={{
                        display: 'block', fontSize: '12px', marginTop: '8px', color: '#dc2626', fontWeight: 'bold'
                    }}>
                        Requerido: {permissao}
                    </span>
                )}
            </p>
            <Link 
                to="/" 
                className="busca-crud__botao" 
                style={{ 
                    textDecoration: 'none', 
                    display: 'inline-block',
                    padding: '10px 20px'
                }}
            >
                Voltar para o Início
            </Link>
        </div>
    );
}
