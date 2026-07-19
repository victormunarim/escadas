import type React from 'react';
import type { CampoRender } from '../types/crud';

interface CampoInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    rotulo: string;
    classeCampo?: string;
}

export function CampoInput({ rotulo, classeCampo = '', ...props }: CampoInputProps) {
    return (
        <label className={`filtro-crud campo ${classeCampo}`}>
            <span className="filtro-crud__rotulo">{rotulo}</span>
            <input className="filtro-crud__entrada" {...props} />
        </label>
    );
}

interface CampoSelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
    rotulo: string;
    classeCampo?: string;
    children: React.ReactNode;
}

export function CampoSelect({ rotulo, classeCampo = '', children, ...props }: CampoSelectProps) {
    return (
        <label className={`filtro-crud campo ${classeCampo}`}>
            <span className="filtro-crud__rotulo">{rotulo}</span>
            <select className="filtro-crud__selecao" {...props}>
                {children}
            </select>
        </label>
    );
}

interface CampoTextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
    rotulo: string;
    classeCampo?: string;
}

export function CampoTextarea({ rotulo, classeCampo = '', ...props }: CampoTextareaProps) {
    return (
        <label className={`filtro-crud campo ${classeCampo}`}>
            <span className="filtro-crud__rotulo">{rotulo}</span>
            <textarea className="formulario-crud__textoarea" {...props} />
        </label>
    );
}

interface CampoRenderizadorProps {
    campo: CampoRender;
    onChange?: (e: React.ChangeEvent<any>) => void;
}

export function CampoRenderizador({ campo, onChange }: CampoRenderizadorProps) {
    if (campo.tipo === 'secao') {
        return (
            <div className="filtro-crud--completo">
                <h3 style={{
                    color: 'var(--blue-900)',
                    borderBottom: '1px solid rgba(19, 80, 140, .12)',
                    paddingBottom: '6px',
                    margin: '14px 0 6px 0'
                }}>
                    {campo.rotulo}
                </h3>
            </div>
        );
    }

    if (campo.tipo === 'select') {
        const opcoesFiltradas = campo.options?.filter(
            opt => opt.valor !== "" && opt.valor !== null && opt.valor !== undefined
        );

        return (
            <CampoSelect
                rotulo={campo.rotulo!}
                classeCampo={campo.classeCampo}
                name={campo.name!}
                required={campo.required}
                defaultValue={campo.value === null || campo.value === undefined ? undefined : String(campo.value)}
                onChange={onChange}
            >
                <option value="">Selecione</option>
                {opcoesFiltradas?.map(opt => (
                    <option key={opt.valor} value={opt.valor}>{opt.rotulo}</option>
                ))}
            </CampoSelect>
        );
    }

    if (campo.tipo === 'textarea') {
        return (
            <CampoTextarea
                rotulo={campo.rotulo!}
                classeCampo={campo.classeCampo}
                name={campo.name!}
                rows={campo.rows}
                maxLength={campo.maxLength}
                defaultValue={campo.value === null || campo.value === undefined ? undefined : String(campo.value)}
                placeholder={campo.placeholder}
                onChange={onChange}
            />
        );
    }

    // tipo === 'input'
    return (
        <CampoInput
            rotulo={campo.rotulo!}
            classeCampo={campo.classeCampo}
            name={campo.name!}
            type={campo.type!}
            required={campo.required}
            min={campo.min}
            max={campo.max}
            maxLength={campo.maxLength}
            step={campo.step}
            inputMode={campo.inputMode}
            placeholder={campo.placeholder}
            defaultValue={campo.value === null || campo.value === undefined ? undefined : campo.value}
            onChange={onChange}
        />
    );
}
