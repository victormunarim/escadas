package com.example.demo.pedidos.service;

import com.example.demo.pedidos.model.Pedido;
import com.example.demo.util.FormatacaoUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class GeradorPdfPedido {
    private String logoBase64Cache = null;

    public byte[] gerarPdfPedido(Pedido pedido) {
        String html = montarHtmlPedido(pedido);
        return gerarPdf(html);
    }

    private byte[] gerarPdf(String html) {
        try (ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(saida);
            builder.run();
            return saida.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar PDF", e);
        }
    }

    private String montarHtmlPedido(Pedido pedido) {
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html lang=\"pt-PT\"><head><meta charset=\"UTF-8\" />")
                .append("<style>")
                .append("@page { size: A4; margin: 28mm 6mm 14mm 6mm; } ")
                .append("@page { @top-center { content: element(header); } ")
                .append("@bottom-left-corner { background-color: #0b2a4a; } ")
                .append("@bottom-left { background-color: #0b2a4a; } ")
                .append("@bottom-center { content: element(footer); background-color: #0b2a4a; } ")
                .append("@bottom-right { background-color: #0b2a4a; } ")
                .append("@bottom-right-corner { background-color: #0b2a4a; } } ")
                .append(
                        "body { font-family: Arial, Helvetica, sans-serif; color: #000; "
                                + "font-size: 16px; padding: 2mm 4mm; } "
                )
                .append(
                        ".page-border { position: fixed; top: 0; left: 0; bottom: 0; right: 0; "
                                + "border: 2px solid #000; z-index: -1; } "
                )
                .append("div.header { position: running(header); width: 100%; padding-top: 4mm; } ")
                .append(
                        ".logo { position: absolute; top: -2mm; left: -4mm; height: 30mm; width: 70mm; "
                                + "z-index: 100; } "
                )
                .append(
                        ".header-text { text-align: right; font-size: 18px; font-weight: bold; "
                                + "line-height: 1.3; margin: 0; } "
                )
                .append(
                        "div.footer { position: running(footer); width: 100%; text-align: center; "
                                + "font-size: 14px; font-weight: bold; color: #ffffff; padding-top: 2mm; } "
                )
                .append(
                        ".section-title { background-color: #e5e7eb; padding: 4px 8px; "
                                + "font-weight: bold; font-size: 15px; border: 1px solid #000; "
                                + "border-bottom: none; margin-top: 10px; } "
                )
                .append("table.content-table { width: 100%; border-collapse: collapse; margin-bottom: 5px; } ")
                .append("table.content-table td { border: 1px solid #000; padding: 4px 8px; vertical-align: top; } ")
                .append(
                        ".label { font-size: 12px; color: #555; display: block; margin-bottom: 2px; "
                                + "text-transform: uppercase; } "
                )
                .append(".value { font-size: 15px; font-weight: bold; } ")
                .append(".signature-line { border-top: 1px solid #000; width: 60%; margin: 0 auto 5px auto; } ")
                .append(
                        ".title-pedido { text-align: center; font-size: 20px; font-weight: bold; "
                                + "margin: 15px 0; text-transform: uppercase; } "
                )
                .append(".termo-garantia p { margin-bottom: 10px; text-align: justify; line-height: 1.4; } ")
                .append("</style></head><body>");

        html.append("<div class='page-border'></div>");
        html.append("<div class='header'>");
        if (logoBase64Cache != null || !obterLogoEmBase64().isEmpty()) {
            html.append("<img class='logo' src='").append(obterLogoEmBase64()).append("' />");
        }
        html.append("<div class='header-text'>")
                .append("FABRICAÇÃO E COMÉRCIO DE<br/>")
                .append("ESCADAS MUNARIM LTDA ME<br/>")
                .append("CNPJ 08.689.875/0001-54")
                .append("</div>")
                .append("</div>");
        html.append("<div class='footer'>")
                .append(
                        "(48) 3257-2015 &#160; (48) 99981-0707 &#160; | &#160; @ESCADASMUNARIM "
                                + "&#160; | &#160; WWW.ESCADASMUNARIM.COM.BR<br/>"
                )
                .append(
                        "RUA TREZE DE JUNHO, 893 - FLOR DE NÁPOLIS CEP 88.106-470 - "
                                + "SÃO JOSÉ - SANTA CATARINA"
                )
                .append("</div>");
        html.append("<div class='title-pedido'>PEDIDO ")
                .append(valor(pedido.getNumeroPedido()))
                .append("</div>");

        html.append("<div class='section-title'>DADOS DO CLIENTE</div>");
        html.append("<table class='content-table'>");
        html.append("<tr>")
                .append(
                        "<td colspan='3'><span class='label'>Nome/Razão Social</span><span class='value'>"
                )
                .append(valor(pedido.getNomeCliente()))
                .append("</span></td>")
                .append("</tr><tr>")
                .append("<td><span class='label'>CPF</span><span class='value'>")
                .append(FormatacaoUtil.formatarCpf(pedido.getCpf()))
                .append("</span></td>")
                .append("<td><span class='label'>RG</span><span class='value'>")
                .append(FormatacaoUtil.formatarRg(pedido.getRg()))
                .append("</span></td>")
                .append("<td><span class='label'>CNPJ</span><span class='value'>")
                .append(FormatacaoUtil.formatarCnpj(pedido.getCnpj()))
                .append("</span></td>")
                .append("</tr><tr>")
                .append("<td colspan='2'><span class='label'>Email</span><span class='value'>")
                .append(valor(pedido.getEmail()))
                .append("</span></td>")
                .append("<td><span class='label'>Telefones</span><span class='value'>")
                .append(FormatacaoUtil.formatarTelefoneCelular(pedido.getTelefone()))
                .append(" / ")
                .append(FormatacaoUtil.formatarTelefoneFixo(pedido.getTelefoneFixo()))
                .append("</span></td>")
                .append("</tr><tr>")
                .append("<td colspan='2'><span class='label'>Endereço do Cliente</span><span class='value'>")
                .append(valor(pedido.getBairroCliente()))
                .append(", ")
                .append(valor(pedido.getMunicipioCliente()))
                .append("</span></td>")
                .append("<td><span class='label'>CEP</span><span class='value'>")
                .append(FormatacaoUtil.formatarCep(pedido.getCepCliente()))
                .append("</span></td>")
                .append("</tr>");
        html.append("</table>");

        html.append("<div class='section-title'>DADOS DA OBRA</div>");
        html.append("<table class='content-table'>");
        html.append("<tr>")
                .append("<td colspan='2'><span class='label'>Administrador da Obra</span><span class='value'>")
                .append(valor(pedido.getAdmObra()))
                .append("</span></td>")
                .append("<td><span class='label'>Serviço Social / Profissão</span><span class='value'>")
                .append(valor(pedido.getServicoSocial()))
                .append(" / ")
                .append(valor(pedido.getProfissao()))
                .append("</span></td>")
                .append("</tr><tr>")
                .append("<td colspan='2'><span class='label'>Endereço da Obra</span><span class='value'>")
                .append(valor(pedido.getBairro()))
                .append(", ")
                .append(valor(pedido.getNumero()))
                .append(" - ")
                .append(valor(pedido.getMunicipio()))
                .append("</span></td>")
                .append("<td><span class='label'>CEP</span><span class='value'>")
                .append(FormatacaoUtil.formatarCep(pedido.getCep()))
                .append("</span></td>")
                .append("</tr><tr>")
                .append("<td colspan='3'><span class='label'>Referência</span><span class='value'>")
                .append(valor(pedido.getReferencia()))
                .append("</span></td>")
                .append("</tr>");
        html.append("</table>");

        html.append("<div class='section-title'>ESPECIFICAÇÕES TÉCNICAS</div>");
        html.append("<table class='content-table'>");
        html.append("<tr>")
                .append("<td><span class='label'>Acabamento</span><span class='value'>")
                .append(valor(pedido.getAcabamento()))
                .append("</span></td>")
                .append("<td><span class='label'>Tubos</span><span class='value'>")
                .append(valor(pedido.getTubos()))
                .append("</span></td>")
                .append("<td><span class='label'>Revestimento</span><span class='value'>")
                .append(Boolean.TRUE.equals(pedido.getRevestimento()) ? "Sim" : "Não")
                .append("</span></td>")
                .append("</tr><tr>")
                .append("<td colspan='3'><span class='label'>Descrição do Pedido</span><span class='value'>")
                .append(escaparHtml(valor(pedido.getDescricao())))
                .append("</span></td>")
                .append("</tr>");
        html.append("</table>");

        html.append("<div class='section-title'>VALORES E CONDIÇÕES</div>");
        html.append("<table class='content-table'>");
        html.append("<tr>")
                .append("<td><span class='label'>Prazo de Montagem</span><span class='value'>")
                .append(valor(pedido.getPrazoMontagem()))
                .append("</span></td>")
                .append("<td><span class='label'>Valor Base</span><span class='value'>R$ ")
                .append(FormatacaoUtil.formatarValor(pedido.getValor()))
                .append("</span></td>")
                .append("<td><span class='label'>Valor Total</span><span class='value'>R$ ")
                .append(FormatacaoUtil.formatarValor(pedido.getValorTotal()))
                .append("</span></td>")
                .append("</tr>");
        html.append("</table>");
        html.append("<div style='margin-top: 15px; text-align: center; font-size: 15px;'>")
                .append("Projeto aprovado pelo cliente, conforme sua assinatura e data do mesmo.<br/>")
                .append("Estou ciente dos termos de garantia, anexado ao pedido.<br/>")
                .append("<strong>Data: ").append(dataAtual).append("</strong>")
                .append("</div>");

        html.append("<table style='width: 100%; border-collapse: collapse; margin-top: 50px;'>")
                .append("<tr><td style='text-align: center; width: 50%; border: none;'>")
                .append("<div class='signature-line'></div><span class='value'>Representante</span>")
                .append("</td><td style='text-align: center; width: 50%; border: none;'>")
                .append("<div class='signature-line'></div><span class='value'>Cliente</span>")
                .append("</td></tr></table>");
        html.append("<div style='page-break-before: always;'></div>");

        html.append("<div class='title-pedido'>TERMO DE GARANTIA</div>");

        html.append("<div class='termo-garantia'>");
        html.append(
                "<p><strong>1.</strong> As escadas MUNARIM provê garantia de 5 (cinco) anos na escada "
                        + "(peças de concreto), contando a partir da data de entrega e montagem da mesma.</p>"
        );

        html.append(
                        "<p><strong>2.</strong> Entende-se por garantia, o reparo dos produtos que apresentem "
                                + "eventuais defeitos de fabricação, desde que seja respeitado o prazo de cura "
                                + "da escada, informado pela empresa no ato da montagem.<br/>"
                )
                .append("&#8226; Pré-fabricada: 03 (três) dias sem uso e 07 (sete) dias escoradas<br/>")
                .append("&#8226; Moldadas in loco: 03 (três) dias sem uso e 10 (dez) a 20 (vinte) dias escoradas</p>");

        html.append(
                "<p><strong>3.</strong> Para acionar a garantia, por favor, entre em contato através do "
                        + "telefone (48) 3257-2015 ou visite nosso endereço físico na rua 13 de junho, nº 893, "
                        + "bairro Flor de Nápoles, São José/SC. Nossa equipe técnica irá avaliar o problema "
                        + "no local.</p>"
        );

        html.append("<p><strong>4.</strong> A MUNARIM não cobre a garantia nos casos:<br/>")
                .append(
                        "a) quando terceiros causam danos ao acabamento da escada, cimento queimado ou "
                                + "pintura de fundo, como andar com calçados sujos, manipulação de materiais "
                                + "ou aplicação de fitas adesivas, entre outros;<br/>"
                )
                .append(
                        "b) quando terceiros realizam serviços que possam afetar a estrutura da escada, "
                                + "como instalação de revestimento ou corrimão sem a supervisão de um "
                                + "técnico da MUNARIM.<br/>"
                )
                .append(
                        "Os custos de cada serviço irão variar de acordo com o modelo, tamanho, local e "
                                + "tempo de execução de cada escada. Este serviço deve ser solicitado através "
                                + "do telefone (48) 3257-2015 com no mínimo de 20 (vinte) dias de antecedência.<br/>"
                )
                .append(
                        "Serviços como sapata, preparação do solo, reboco, pintura, revestimento e corrimão "
                                + "não estão inclusos nos serviços da Munarim.</p>"
                );

        html.append(
                        "<p><strong>5.</strong> Para a preservação das escadas, é fundamental seguir as "
                                + "orientações abaixo:<br/>"
                )
                .append(
                        "&#8226; Logo após o período de cura, é necessário pintar as escadas de acordo com "
                                + "o acabamento:<br/>"
                )
                .append(
                        "&#160;&#160;a) Cimento queimado, aplicar resina acrílica a base de água, "
                                + "imediatamente antes de qualquer outro serviço, devido à delicadeza do "
                                + "acabamento. Cuidando com fitas adesivas, que tem histórico de danificar.<br/>"
                )
                .append(
                        "&#160;&#160;b) Pintura de fundo, pintar das peças para proteger a escada, assim "
                                + "como outros elementos que requerem pintura definitiva, uma vez que a escada "
                                + "é entregue apenas com um fundo.<br/>"
                )
                .append(
                        "&#8226; Realizar manutenção regular do acabamento de pintura ou resina é essencial. "
                                + "Para escadas internas a manutenção de pintura pode seguir o mesmo planejamento "
                                + "das paredes internas; Para escadas externas, recomenda-se fazer essa manutenção "
                                + "a cada 02 (dois) anos, para garantir a eficácia da pintura na preservação do "
                                + "concreto e, consequentemente, prolongar a vida útil da escada.<br/>"
                )
                .append(
                        "&#8226; Em escadas externas, é altamente aconselhável aplicar um cobrimento "
                                + "impermeabilizante para prevenir infiltrações. Essa medida é crucial, visto "
                                + "que a água pode percolar pelo concreto, atingindo a armadura e causando "
                                + "corrosão, enfraquecendo a integridade estrutural da escada.</p>"
                );

        html.append("<p><strong>6.</strong> Acessórios: tubos, calotinhas, parafusos, arruelas e porcas.<br/>")
                .append(
                        "&#8226; Aconselha-se a limpeza frequente dos tubos, principalmente na fase de obra "
                                + "ou em regiões próximas do mar.<br/>"
                )
                .append(
                        "&#8226; Acessórios em alumínio com pintura eletrostática: deve-se utilizar um pano "
                                + "com detergente neutro.<br/>"
                )
                .append(
                        "&#8226; Acessórios em inox polidos: deve-se utilizar apenas um pano de água morna "
                                + "ou detergente neutro.<br/>"
                )
                .append(
                        "&#8226; Acessórios em inox escovados: caso uma crosta de sujeita já tenha se "
                                + "formado nos tubos, orientamos que a limpeza seja realizada com o lado verde "
                                + "da esponja de cozinha, observando o sentido das linhas do aço escovado. "
                                + "Podendo mudar o aspecto do escovado.<br/>"
                )
                .append("&#8226; Jamais usar água sanitária<br/>")
                .append(
                        "&#8226; Atenção: o acúmulo de sujeira neste tipo material costuma ser confundido "
                                + "com ferrugem.<br/>"
                )
                .append(
                        "&#8226; Parafusos, arruelas e porcas: são produtos zincados e a garantia é "
                                + "diretamente do fabricante.</p>"
                );

        html.append("<div style='font-size: 12px; font-weight: bold; margin-top: 30px;'>Revisão 01</div>");
        html.append("</div>");

        html.append("</body></html>");
        return html.toString();
    }

    private String obterLogoEmBase64() {
        if (logoBase64Cache != null) {
            return logoBase64Cache;
        }

        try {
            ClassPathResource resource = new ClassPathResource("static/logo.png");
            try (InputStream is = resource.getInputStream()) {
                byte[] imageBytes = is.readAllBytes();
                logoBase64Cache =
                        "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
                return logoBase64Cache;
            }
        } catch (Exception e) {
            return "";
        }
    }

    private String valor(Object valor) {
        return valor == null ? "" : String.valueOf(valor);
    }

    private String escaparHtml(String texto) {
        if (texto == null) return "";
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
