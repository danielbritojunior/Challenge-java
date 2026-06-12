package service;

import model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProcessadorService {

    public List<CardAcao> gerarCardsDeAcao(Reuniao reuniao) {

        List<CardAcao> cards = new ArrayList<>();

        String texto = reuniao.getTextoBruto().toLowerCase();

        LocalTime base = LocalTime.of(0, 10, 0);
        int contador = 0;

        if (texto.contains("rh") || texto.contains("folha manual")) {

            LocalTime tempo = base.plusSeconds(contador += 5);

            cards.add(new CardAcao(
                    new GatilhoCompra(
                            tempo,
                            extrairTrecho(texto, "rh"),
                            "TOTVS RM"
                    ),
                    "Agendar reunião técnica para ofertar TOTVS RM"
            ));
        }

        if (texto.contains("senior")) {

            LocalTime tempo = base.plusSeconds(contador += 5);

            cards.add(new CardAcao(
                    new AlertaRisco(
                            tempo,
                            extrairTrecho(texto, "senior"),
                            "Senior"
                    ),
                    "Enviar diferenciais competitivos contra a Senior"
            ));
        }

        if (texto.contains("sap")) {

            LocalTime tempo = base.plusSeconds(contador += 5);

            cards.add(new CardAcao(
                    new AlertaRisco(
                            tempo,
                            extrairTrecho(texto, "sap"),
                            "SAP"
                    ),
                    "Avaliar risco competitivo SAP"
            ));
        }

        if (texto.contains("crm")) {

            LocalTime tempo = base.plusSeconds(contador += 5);

            cards.add(new CardAcao(
                    new GatilhoCompra(
                            tempo,
                            extrairTrecho(texto, "crm"),
                            "TOTVS CRM"
                    ),
                    "Ofertar CRM integrado"
            ));
        }
        if (texto.contains("estoque")) {

            LocalTime tempo = base.plusSeconds(contador += 5);

            cards.add(new CardAcao(
                    new GatilhoCompra(
                            tempo,
                            extrairTrecho(texto, "estoque"),
                            "TOTVS Supply"
                    ),
                    "Propor automação de estoque"
            ));
        }

        return cards;
    }

    public void priorizarEExibirCards(List<CardAcao> cards) {

        cards.sort((c1, c2) -> {

            boolean r1 = c1.getInsightVinculado() instanceof AlertaRisco;
            boolean r2 = c2.getInsightVinculado() instanceof AlertaRisco;

            if (r1 && !r2) return -1;
            if (!r1 && r2) return 1;

            return c1.getInsightVinculado()
                    .getTimestamp()
                    .compareTo(c2.getInsightVinculado().getTimestamp());
        });

        for (CardAcao card : cards) {
            card.exibirCard();
        }
    }

    private String extrairTrecho(String texto, String palavra) {

        int index = texto.indexOf(palavra);

        if (index == -1) {
            return "Trecho não encontrado";
        }

        int inicio = Math.max(0, index - 25);
        int fim = Math.min(texto.length(), index + 50);

        return texto.substring(inicio, fim).trim();
    }
}