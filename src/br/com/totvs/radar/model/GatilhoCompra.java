package br.com.totvs.radar.model;

import java.time.LocalTime;

public class GatilhoCompra extends Insight {
    private String produtoSugerido;

    public GatilhoCompra(LocalTime timestamp, String trechoTexto, String produtoSugerido) {
        super(timestamp, trechoTexto);
        this.produtoSugerido = produtoSugerido;
    }

    @Override
    public String getDescricaoTipo() {
        return "OPORTUNIDADE - Gatilho de Compra (Produto sugerido: " + produtoSugerido + ")";
    }

    public String getProdutoSugerido() { return produtoSugerido; }
    public void setProdutoSugerido(String produtoSugerido) { this.produtoSugerido = produtoSugerido; }
}