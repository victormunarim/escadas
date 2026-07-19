package com.example.demo.shared.crud.render;

import com.example.demo.shared.crud.OpcaoCrud;
import java.util.List;

public record CampoSelecaoRender(
        String tipo,
        String rotulo,
        String name,
        String classeCampo,
        boolean required,
        Object value,
        List<OpcaoCrud> options
) implements CampoRender {
    public CampoSelecaoRender(
            String rotulo, String name, String classeCampo, boolean required,
            Object value, List<OpcaoCrud> options
    ) {
        this("select", rotulo, name, classeCampo, required, value, options);
    }
}
