package model;

import java.time.LocalTime;

public abstract class Insight {
    protected LocalTime timestamp;
    protected String trechoTexto;
    private Integer id;

    public Insight(Integer id, LocalTime timestamp, String trechoTexto) {
        this.id = id;
        this.timestamp = timestamp;
        this.trechoTexto = trechoTexto;
    }
    public Insight(LocalTime timestamp, String trechoTexto) {
        this.timestamp = timestamp;
        this.trechoTexto = trechoTexto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public abstract String getDescricaoTipo();

    public LocalTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalTime timestamp) { this.timestamp = timestamp; }

    public String getTrechoTexto() { return trechoTexto; }
    public void setTrechoTexto(String trechoTexto) { this.trechoTexto = trechoTexto; }
}