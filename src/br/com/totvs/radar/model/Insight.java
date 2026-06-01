package br.com.totvs.radar.model;

import java.time.LocalTime;

public abstract class Insight {
    protected LocalTime timestamp;
    protected String trechoTexto;

    public Insight(LocalTime timestamp, String trechoTexto) {
        this.timestamp = timestamp;
        this.trechoTexto = trechoTexto;
    }

    public abstract String getDescricaoTipo();

    public LocalTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalTime timestamp) { this.timestamp = timestamp; }

    public String getTrechoTexto() { return trechoTexto; }
    public void setTrechoTexto(String trechoTexto) { this.trechoTexto = trechoTexto; }
}