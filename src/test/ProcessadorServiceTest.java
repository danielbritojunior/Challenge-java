package test;

import model.CardAcao;
import model.Reuniao;
import model.StatusAcao;
import service.ProcessadorService;

import java.util.List;
import java.util.Map;

public class ProcessadorServiceTest {

    public static void main(String[] args) {
        ProcessadorService service = new ProcessadorService();

        String transcricao = "O time de RH precisa automatizar folha no TOTVS RM. "
                + "O cliente viu demo da Senior concorrente. "
                + "O financeiro quer avaliar SAP.";

        Reuniao reuniao = new Reuniao(1, "Diretor Comercial", transcricao);

        System.out.println("=== INICIANDO TESTES DO PROCESSADOR SERVICE ===\n");

        List<CardAcao> cards = service.gerarCardsDeAcao(reuniao);
        System.out.println("1. Teste de Geração de Cards:");
        System.out.println("Total gerado: " + cards.size());
        assert !cards.isEmpty() : "Falha: nenhum card foi gerado!";
        System.out.println("-> OK: Cards gerados com sucesso.\n");

        System.out.println("2. Teste de Priorização:");
        service.priorizarEExibirCards(cards);
        System.out.println("-> OK: Exibição concluída.\n");

        System.out.println("3. Teste de Filtragem por Status:");
        cards.get(0).setStatus(StatusAcao.CONCLUIDO);
        if (cards.size() > 1) {
            cards.get(1).setStatus(StatusAcao.EM_ANDAMENTO);
        }

        List<CardAcao> concluidos = service.filtrarPorStatus(cards, StatusAcao.CONCLUIDO);
        List<CardAcao> emAndamento = service.filtrarPorStatus(cards, StatusAcao.EM_ANDAMENTO);
        List<CardAcao> pendentes = service.filtrarPorStatus(cards, StatusAcao.PENDENTE);

        System.out.println("Concluídos: " + concluidos.size());
        System.out.println("Em Andamento: " + emAndamento.size());
        System.out.println("Pendentes: " + pendentes.size());
        assert concluidos.size() == 1 : "Falha: filtro de concluídos incorreto!";
        System.out.println("-> OK: Filtros operando corretamente.\n");

        System.out.println("4. Teste de Métricas:");
        Map<String, Number> metricas = service.calcularMetricas(cards);
        System.out.println("Métricas calculadas: " + metricas);
        assert metricas.get("total").intValue() == cards.size() : "Falha: total de métricas divergente!";
        System.out.println("-> OK: Cálculo de métricas validado com sucesso.\n");

        System.out.println("==============================================");
        System.out.println(">>> TODOS OS TESTES DO SERVICE PASSARAM! <<<");
        System.out.println("==============================================");
    }
}