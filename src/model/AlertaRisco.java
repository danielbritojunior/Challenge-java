package model;
import model.Insight;

import java.time.LocalTime;

public class AlertaRisco extends Insight {
    private String concorrenteIdentificado;

    public AlertaRisco(LocalTime timestamp, String trechoTexto, String concorrenteIdentificado) {
        super(timestamp, trechoTexto);
        this.concorrenteIdentificado = concorrenteIdentificado;
    }

    @Override
    public String getDescricaoTipo() {
        // Implementação do Polimorfismo
        return "CRÍTICO - Risco de Churn (Concorrente citado: " + concorrenteIdentificado + ")";
    }

    public String getConcorrenteIdentificado() { return concorrenteIdentificado; }
    public void setConcorrenteIdentificado(String concorrenteIdentificado) { this.concorrenteIdentificado = concorrenteIdentificado; }
}