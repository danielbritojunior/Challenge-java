package main;

import dao.CardAcaoDAO;
import model.CardAcao;
import model.Reuniao;
import model.StatusAcao;
import service.ProcessadorService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CardAcaoDAO dao = new CardAcaoDAO();
        ProcessadorService processador = new ProcessadorService();

        String transcricaoExemplo =
                "Bom dia equipe. O time de RH está sofrendo muito com a folha manual e precisamos automatizar isso no TOTVS RM. "
                        + "Além disso, o cliente comentou que viu uma demo da Senior e ficou muito interessado na solução concorrente. "
                        + "O financeiro relatou problemas com controle de estoque e sugeriu avaliar SAP como alternativa. "
                        + "O diretor mencionou que o CRM atual não está integrado com o Protheus e isso está gerando retrabalho. "
                        + "Também foi citado que a área de vendas precisa de uma ferramenta mais moderna para gestão de leads e funil comercial. "
                        + "Por fim, há preocupação com custo elevado da operação e busca por redução de despesas com automação.";

        Reuniao reuniaoComercial =
                new Reuniao(1, "Gestor Operacional (Decisor)", transcricaoExemplo);

        List<CardAcao> cardsAtuais = dao.listarCards();

        int opcao;

        do {
            System.out.println("\n========================================");
            System.out.println("RadarIA - DASHBOARD ORACLE");
            System.out.println("========================================");
            System.out.println("1 - Visualizar transcrição");
            System.out.println("2 - Gerar cards");
            System.out.println("3 - Listar cards");
            System.out.println("4 - Alterar status");
            System.out.println("5 - Excluir card");
            System.out.println("6 - Resumo de Métricas");
            System.out.println("7 - Filtrar cards por status");
            System.out.println("8 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();

            switch (opcao) {

                case 1:
                    System.out.println("\n=== TRANSCRIÇÃO DA REUNIÃO ===");
                    imprimirTextoFormatado(reuniaoComercial.getTextoBruto(), 70);
                    break;

                case 2:
                    System.out.println("\n[1/2] Processando texto da reunião e gerando cards...");
                    List<CardAcao> novosCards = processador.gerarCardsDeAcao(reuniaoComercial);

                    System.out.println("[2/2] Persistindo insights e cards no Oracle...");
                    for (CardAcao card : novosCards) {
                        dao.inserirCard(card);
                    }

                    cardsAtuais = dao.listarCards();

                    System.out.println("\n=== CARDS SALVOS COM SUCESSO NO BANCO ===");
                    processador.priorizarEExibirCards(cardsAtuais);
                    break;

                case 3:
                    System.out.println("\n[Consultando registros na tabela CARD_ACAO...]");
                    cardsAtuais = dao.listarCards();

                    if (cardsAtuais.isEmpty()) {
                        System.out.println("Nenhum card cadastrado no Oracle. Utilize a opção 2 para gerar.");
                    } else {
                        System.out.println("\n=== CARDS CADASTRADOS NO BANCO (" + cardsAtuais.size() + ") ===");
                        processador.priorizarEExibirCards(cardsAtuais);
                    }
                    break;

                case 4:
                    if (cardsAtuais == null || cardsAtuais.isEmpty()) {
                        cardsAtuais = dao.listarCards();
                    }

                    if (cardsAtuais.isEmpty()) {
                        System.out.println("Nenhum card disponível para alterar status.");
                        break;
                    }

                    System.out.println("\n=== SELECIONE O CARD PARA ATUALIZAR STATUS (UPDATE) ===");
                    for (int i = 0; i < cardsAtuais.size(); i++) {
                        CardAcao c = cardsAtuais.get(i);
                        System.out.println((i + 1) + " - [ID: " + c.getId() + "] "
                                + c.getInsightVinculado().getDescricaoTipo()
                                + " | Status atual: " + c.getStatus());
                    }
                    System.out.println("0 - Cancelar");
                    System.out.print("Informe o número do card: ");
                    int indice = sc.nextInt();

                    if (indice == 0) {
                        System.out.println("Operação cancelada.");
                        break;
                    }

                    int posicao = indice - 1;
                    if (posicao < 0 || posicao >= cardsAtuais.size()) {
                        System.out.println("Opção inválida.");
                        break;
                    }

                    CardAcao cardSelecionado = cardsAtuais.get(posicao);

                    System.out.println("\nEscolha o novo status:");
                    System.out.println("1 - PENDENTE");
                    System.out.println("2 - EM ANDAMENTO");
                    System.out.println("3 - CONCLUIDO");
                    System.out.println("4 - DESCARTADO");
                    System.out.println("0 - Cancelar");
                    System.out.print("Informe a opção: ");
                    int opStatus = sc.nextInt();

                    StatusAcao novoStatus = switch (opStatus) {
                        case 1 -> StatusAcao.PENDENTE;
                        case 2 -> StatusAcao.EM_ANDAMENTO;
                        case 3 -> StatusAcao.CONCLUIDO;
                        case 4 -> StatusAcao.DESCARTADO;
                        default -> null;
                    };

                    if (novoStatus != null) {
                        dao.atualizarStatus(cardSelecionado.getId(), novoStatus);
                        cardSelecionado.setStatus(novoStatus);
                        System.out.println("Status atualizado com sucesso!");
                    } else if (opStatus != 0) {
                        System.out.println("Status inválido.");
                    }
                    break;

                case 5:
                    if (cardsAtuais == null || cardsAtuais.isEmpty()) {
                        cardsAtuais = dao.listarCards();
                    }

                    if (cardsAtuais.isEmpty()) {
                        System.out.println("Nenhum card disponível para exclusão.");
                        break;
                    }

                    System.out.println("\n=== SELECIONE O CARD PARA EXCLUIR (DELETE) ===");
                    for (int i = 0; i < cardsAtuais.size(); i++) {
                        CardAcao c = cardsAtuais.get(i);
                        System.out.println((i + 1) + " - [ID: " + c.getId() + "] "
                                + c.getInsightVinculado().getDescricaoTipo()
                                + " | Tarefa: " + c.getSugestaoTarefa());
                    }
                    System.out.println("0 - Cancelar");
                    System.out.print("Informe o número do card a remover: ");
                    int delIndice = sc.nextInt();

                    if (delIndice == 0) {
                        System.out.println("Operação cancelada.");
                        break;
                    }

                    int posDel = delIndice - 1;
                    if (posDel >= 0 && posDel < cardsAtuais.size()) {
                        CardAcao cardRemover = cardsAtuais.get(posDel);
                        dao.excluirCard(cardRemover.getId());
                        cardsAtuais.remove(posDel);
                        System.out.println("Card removido do Oracle e da lista com sucesso!");
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;

                case 6:
                    if (cardsAtuais == null || cardsAtuais.isEmpty()) {
                        cardsAtuais = dao.listarCards();
                    }

                    if (cardsAtuais.isEmpty()) {
                        System.out.println("Não há cards cadastrados para gerar métricas.");
                        break;
                    }

                    Map<String, Number> metricas = processador.calcularMetricas(cardsAtuais);
                    System.out.println("\n=== MÉTRICAS GERAIS (PROCESSADOR SERVICE) ===");
                    System.out.println("Total de Cards: " + metricas.get("total"));
                    System.out.println("Cards Pendentes: " + metricas.get("pendentes"));
                    System.out.println("Cards Concluídos: " + metricas.get("concluidos"));
                    System.out.println("Taxa de Resolução: " + metricas.get("taxaResolucao") + "%");
                    break;

                case 7:
                    if (cardsAtuais == null || cardsAtuais.isEmpty()) {
                        cardsAtuais = dao.listarCards();
                    }

                    if (cardsAtuais.isEmpty()) {
                        System.out.println("Não há cards cadastrados para filtrar.");
                        break;
                    }

                    System.out.println("\n=== FILTRAR CARDS ===");
                    System.out.println("1 - Pendentes");
                    System.out.println("2 - Em Andamento");
                    System.out.println("3 - Concluídos");
                    System.out.println("4 - Descartados");
                    System.out.println("0 - Cancelar");
                    System.out.print("Escolha o status: ");
                    int filtro = sc.nextInt();

                    StatusAcao statusFiltro = switch (filtro) {
                        case 1 -> StatusAcao.PENDENTE;
                        case 2 -> StatusAcao.EM_ANDAMENTO;
                        case 3 -> StatusAcao.CONCLUIDO;
                        case 4 -> StatusAcao.DESCARTADO;
                        default -> null;
                    };

                    if (statusFiltro != null) {
                        List<CardAcao> filtrados = processador.filtrarPorStatus(cardsAtuais, statusFiltro);
                        System.out.println("\n=== RESULTADO DO FILTRO: " + statusFiltro + " (" + filtrados.size() + ") ===");
                        if (filtrados.isEmpty()) {
                            System.out.println("Nenhum card encontrado com este status.");
                        } else {
                            filtrados.forEach(CardAcao::exibirCard);
                        }
                    }
                    break;

                case 8:
                    System.out.println("Encerrando o RadarIA. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 8);

        sc.close();
    }

    public static void imprimirTextoFormatado(String texto, int largura) {
        String[] palavras = texto.split(" ");
        StringBuilder linha = new StringBuilder();

        for (String palavra : palavras) {
            if (linha.length() + palavra.length() + 1 > largura) {
                System.out.println(linha);
                linha = new StringBuilder();
            }
            linha.append(palavra).append(" ");
        }
        System.out.println(linha);
    }
}