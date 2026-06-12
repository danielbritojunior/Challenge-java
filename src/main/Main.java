package main;

import model.CardAcao;
import model.Reuniao;
import model.StatusAcao;
import service.ProcessadorService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String transcricaoExemplo =
                "Bom dia equipe. O time de RH está sofrendo muito com a folha manual e precisamos automatizar isso no TOTVS RM. "
                        + "Além disso, o cliente comentou que viu uma demo da Senior e ficou muito interessado na solução concorrente. "
                        + "O financeiro relatou problemas com controle de estoque e sugeriu avaliar SAP como alternativa. "
                        + "O diretor mencionou que o CRM atual não está integrado com o Protheus e isso está gerando retrabalho. "
                        + "Também foi citado que a área de vendas precisa de uma ferramenta mais moderna para gestão de leads e funil comercial. "
                        + "Por fim, há preocupação com custo elevado da operação e busca por redução de despesas com automação.";

        Reuniao reuniaoComercial =
                new Reuniao(1, "Gestor Operacional (Decisor)", transcricaoExemplo);

        ProcessadorService processador = new ProcessadorService();

        List<CardAcao> cardsGerados = null;

        int opcao;

        do {

            System.out.println("\n========================================");
            System.out.println("RADAR DE AÇÃO TOTVS - DASHBOARD");
            System.out.println("========================================");
            System.out.println("1 - Visualizar transcrição");
            System.out.println("2 - Gerar / Visualizar cards");
            System.out.println("3 - Alterar status de card");
            System.out.println("4 - Resumo geral");
            System.out.println("5 - Filtrar cards");
            System.out.println("6 - Sair");
            System.out.print("Escolha: ");

            opcao = sc.nextInt();

            switch (opcao) {

                case 1:
                    System.out.println("\n=== TRANSCRIÇÃO ===");
                    imprimirTextoFormatado(reuniaoComercial.getTextoBruto(), 70);
                    break;

                case 2:
                    if (cardsGerados == null) {
                        System.out.println("\n[Gerando cards...]");
                        cardsGerados = processador.gerarCardsDeAcao(reuniaoComercial);
                    }

                    System.out.println("\n=== CARDS ===");
                    processador.priorizarEExibirCards(cardsGerados);
                    break;

                case 3:

                    if (cardsGerados == null || cardsGerados.isEmpty()) {
                        System.out.println("Gere os cards primeiro!");
                        break;
                    }

                    boolean alterou = false;

                    while (true) {

                        System.out.println("\n=== SELECIONE UM CARD ===");

                        for (int i = 0; i < cardsGerados.size(); i++) {
                            System.out.println((i + 1) + " - "
                                    + cardsGerados.get(i).getInsightVinculado().getDescricaoTipo()
                                    + " | Status: "
                                    + cardsGerados.get(i).getStatus());
                        }
                        System.out.println("0 - Voltar");
                        System.out.print("Informe a opção desejada: : ");
                        int indice = sc.nextInt();

                        if (indice == 0) {
                            System.out.println("Retornando ao menu...");
                            break;
                        }

                        indice = indice - 1;

                        if (indice < 0 || indice >= cardsGerados.size()) {
                            System.out.println("Card não existe. Tente novamente.");
                            continue;
                        }

                        while (true) {

                            System.out.println("\nCard selecionado: "
                                    + cardsGerados.get(indice).getInsightVinculado().getDescricaoTipo());

                            System.out.println("1 - EXECUTADO");
                            System.out.println("2 - DESCARTADO");
                            System.out.println("0 - Voltar");
                            System.out.print("Informe a opção desejada: ");

                            int status = sc.nextInt();

                            if (status == 0) {
                                System.out.println("Voltando para o menu...");
                                break;
                            }

                            if (status == 1) {
                                cardsGerados.get(indice).setStatus(StatusAcao.EXECUTADO);
                                System.out.println("Status atualizado para EXECUTADO");
                                alterou = true;
                                break;
                            }

                            if (status == 2) {
                                cardsGerados.get(indice).setStatus(StatusAcao.DESCARTADO);
                                System.out.println("Status atualizado para DESCARTADO");
                                alterou = true;
                                break;
                            }

                            System.out.println("Status inválido. Tente novamente.");
                        }

                        break;
                    }

                    if (alterou) {
                        System.out.println("\nDASHBOARD ATUALIZADO:");
                        processador.priorizarEExibirCards(cardsGerados);
                    }

                    break;

                case 4:

                    if (cardsGerados == null || cardsGerados.isEmpty()) {
                        System.out.println("Nenhum card gerado.");
                        break;
                    }

                    int pend = 0;
                    int exec = 0;
                    int desc = 0;

                    for (CardAcao c : cardsGerados) {
                        switch (c.getStatus()) {
                            case PENDENTE -> pend++;
                            case EXECUTADO -> exec++;
                            case DESCARTADO -> desc++;
                        }
                    }

                    System.out.println("\n=== DASHBOARD RESUMO ===");
                    System.out.println("Total de cards: " + cardsGerados.size());
                    System.out.println("Pendentes: " + pend);
                    System.out.println("Executados: " + exec);
                    System.out.println("Descartados: " + desc);

                    break;

                case 5:

                    if (cardsGerados == null || cardsGerados.isEmpty()) {
                        System.out.println("Gere os cards primeiro!");
                        break;
                    }

                    while (true) {

                        System.out.println("\n=== FILTRAR CARDS ===");
                        System.out.println("1 - Pendentes");
                        System.out.println("2 - Executados");
                        System.out.println("3 - Descartados");
                        System.out.println("4 - Todos");
                        System.out.println("0 - Voltar");
                        System.out.print("Informe a opção desejada: ");

                        int filtro = sc.nextInt();

                        if (filtro == 0) {
                            System.out.println("Retornando ao menu...");
                            break;
                        }

                        if (filtro < 1 || filtro > 4) {
                            System.out.println("Opção inválida. Tente novamente.");
                            continue;
                        }

                        boolean encontrou = false;

                        System.out.println("\n=== RESULTADO DO FILTRO ===");

                        for (CardAcao c : cardsGerados) {

                            boolean mostrar = switch (filtro) {
                                case 1 -> c.getStatus() == StatusAcao.PENDENTE;
                                case 2 -> c.getStatus() == StatusAcao.EXECUTADO;
                                case 3 -> c.getStatus() == StatusAcao.DESCARTADO;
                                case 4 -> true;
                                default -> false;
                            };
                            if (mostrar) {
                                c.exibirCard();
                                encontrou = true;
                            }
                        }
                        if (!encontrou) {
                            System.out.println("Não existem cards para esse filtro.");
                        }
                        break;
                    }
                    break;

                case 6:
                    System.out.println("Encerrando sistema...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 6);

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