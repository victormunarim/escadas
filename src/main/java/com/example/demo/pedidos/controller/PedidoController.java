package com.example.demo.pedidos.controller;

import com.example.demo.pedidos.service.PedidoService;
import com.example.demo.pedidos.exception.PedidoNaoEncontradoException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pedidos/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        byte[] pdf;
        try {
            pdf = pedidoService.gerarPdf(id);
        } catch (PedidoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }

        String nomeArquivo = "pedido-" + id + ".pdf";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename(nomeArquivo)
                                .build()
                                .toString()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}