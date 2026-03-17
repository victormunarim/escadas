package com.example.demo.exception;

public class PedidoNaoEncontradoException extends RuntimeException {
    private final Long id;

    public PedidoNaoEncontradoException(Long id) {
        super("Pedido não encontrado.");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
