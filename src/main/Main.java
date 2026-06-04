package main;

import model.CardAcao;
import model.Reuniao;
import service.ProcessadorService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("INICIANDO RADAR DE AÇÃO TOTVS - SPRINT JAVA");
        System.out.println("==================================================");

        String transcricaoExemplo = "Bom dia, João. Então, o nosso Protheus está atendendo o backoffice, "
                + "mas o time de RH está sofrendo muito com a folha manual. O pessoal viu uma demo da "
                + "Senior e gostou, mas eu prefiro consolidar tudo na TOTVS se o RM for realmente integrado. "
                + "Ah, e por favor, não mencione esse valor de R$ 50 mil que conversamos para o meu CFO ainda.";

        Reuniao reuniaoComercial = new Reuniao(1, "Gestor Operacional (Decisor)", transcricaoExemplo);

        ProcessadorService processador = new ProcessadorService();

        System.out.println("\n[Analisando texto bruto da reunião...");
        List<CardAcao> cardsGerados = processador.gerarCardsDeAcao(reuniaoComercial);

        System.out.println("\n[Dashboard] Ordenando cards por prioridade visual...");
        processador.priorizarEExibirCards(cardsGerados);

        System.out.println("\n==================================================");
        System.out.println("EXECUÇÃO DE TESTES CONCLUÍDA COM SUCESSO!");
        System.out.println("==================================================");
    }
}