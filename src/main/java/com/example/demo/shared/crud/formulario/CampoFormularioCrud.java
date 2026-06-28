package com.example.demo.shared.crud.formulario;

import com.example.demo.shared.crud.render.CampoRender;

public interface CampoFormularioCrud<T> {

    String nome();

    String rotulo();

    String nomeFragmento();

    CampoRender renderizar(T dto);
}