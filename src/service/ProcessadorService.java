package service;

import model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProcessadorService {

    public List<CardAcao> gerarCardsDeAcao(Reuniao reuniao) {
        List<CardAcao> cards = new ArrayList<>();
        String texto = reuniao.getTextoBruto().toLowerCase();

        LocalTime tempoGatilho = LocalTime.of(0, 10, 15);
        LocalTime tempoRisco = LocalTime.of(0, 10, 15);

        if (texto.contains("rh") || texto.contains("folha manual")) {
            Insight gatilho = new GatilhoCompra(tempoGatilho, "time de RH está sofrendo muito com a folha manual", "TOTVS RM");
            CardAcao cardCompra = new CardAcao(gatilho, "Agendar reunião técnica para ofertar o módulo TOTVS RM (Folha de Pagamento).");
            cards.add(cardCompra);
        }

        if (texto.contains("senior")) {
            Insight risco = new AlertaRisco(tempoRisco, "O pessoal viu uma demo da Senior e gostou", "Senior");
            CardAcao cardRisco = new CardAcao(risco, "CRÍTICO: Enviar material de diferenciais competitivos contra a Senior e reter cliente.");
            cards.add(cardRisco);
        }
        return cards;
    }

    public void priorizarEExibirCards(List<CardAcao> cards) {
        cards.sort((card1, card2) -> {
            int min1 = card1.getInsightVinculado().getTimestamp().getMinute();
            int min2 = card2.getInsightVinculado().getTimestamp().getMinute();

            if (min1 == min2) {
                boolean c1EhRisco = card1.getInsightVinculado() instanceof AlertaRisco;
                boolean c2EhRisco = card2.getInsightVinculado() instanceof AlertaRisco;
                if (c1EhRisco && !c2EhRisco) return -1; // Coloca card1 no topo
                if (!c1EhRisco && c2EhRisco) return 1;  // Coloca card2 no topo
            }
            return card1.getInsightVinculado().getTimestamp().compareTo(card2.getInsightVinculado().getTimestamp());
        });

        for (CardAcao card : cards) {
            card.exibirCard();
        }
    }
}