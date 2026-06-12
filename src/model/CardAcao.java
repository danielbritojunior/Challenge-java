package model;

import model.Insight;
import model.StatusAcao;

public class CardAcao {

    private Insight insightVinculado;
    private String sugestaoTarefa;
    private StatusAcao status;

    public CardAcao(Insight insightVinculado, String sugestaoTarefa) {
        this.insightVinculado = insightVinculado;
        this.sugestaoTarefa = sugestaoTarefa;
        this.status = StatusAcao.PENDENTE;
    }

    public void exibirCard() {
        System.out.println("-------------------------------------------------");
        System.out.println("[" + insightVinculado.getTimestamp() + "] " + insightVinculado.getDescricaoTipo());
        System.out.println("Trecho: \"" + insightVinculado.getTrechoTexto() + "\"");
        System.out.println("Ação sugerida: " + sugestaoTarefa);
        System.out.println("Status: " + status);
        System.out.println("-------------------------------------------------");
    }

    public Insight getInsightVinculado() { return insightVinculado; }
    public void setInsightVinculado(Insight insightVinculado) { this.insightVinculado = insightVinculado; }

    public String getSugestaoTarefa() { return sugestaoTarefa; }
    public void setSugestaoTarefa(String sugestaoTarefa) { this.sugestaoTarefa = sugestaoTarefa; }

    public StatusAcao getStatus() { return status; }
    public void setStatus(StatusAcao status) { this.status = status; }
}