package com.example.demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public String handlePedidoNaoEncontradoException(
            PedidoNaoEncontradoException ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("erro", "Pedido não encontrado (ID: " + ex.getId() + ").");
        return "redirect:/crud/pedidos";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(
            Exception ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro interno: " + ex.getMessage());
        return "redirect:/crud/pedidos";
    }
}
