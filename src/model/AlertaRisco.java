package model;

import java.time.LocalTime;

public class AlertaRisco extends Insight {
    private String concorrenteIdentificado;

    public AlertaRisco(LocalTime timestamp, String trechoTexto, String concorrenteIdentificado) {
        super(timestamp, trechoTexto);
        this.concorrenteIdentificado = concorrenteIdentificado;
    }

    public AlertaRisco(Integer id, LocalTime timestamp, String trechoTexto, String concorrenteIdentificado) {
        super(id, timestamp, trechoTexto);
        this.concorrenteIdentificado = concorrenteIdentificado;
    }

    @Override
    public String getDescricaoTipo() {
        return "CRÍTICO - Risco de Churn (Concorrente citado: " + concorrenteIdentificado + ")";
    }

    public String getConcorrenteIdentificado() { return concorrenteIdentificado; }
    public void setConcorrenteIdentificado(String concorrenteIdentificado) { this.concorrenteIdentificado = concorrenteIdentificado; }
}