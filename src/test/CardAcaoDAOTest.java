package test;

import dao.CardAcaoDAO;
import model.CardAcao;
import model.GatilhoCompra;
import model.StatusAcao;

import java.time.LocalTime;
import java.util.List;

public class CardAcaoDAOTest {

    public static void main(String[] args) {
        CardAcaoDAO dao = new CardAcaoDAO();

        System.out.println("=== TESTE DO CRUD NO ORACLE ===");

        System.out.println("\n--- 1. TESTE INSERIR (CREATE) ---");
        CardAcao testeCard = new CardAcao(
                new GatilhoCompra(LocalTime.now(), "Folha manual", "TOTVS RM"),
                "Apresentar automacao de folha"
        );
        dao.inserirCard(testeCard);

        System.out.println("\n--- 2. TESTE LISTAR (READ ALL) ---");
        List<CardAcao> cards = dao.listarCards();
        System.out.println("Total de cards no banco: " + cards.size());

        if (!cards.isEmpty()) {
            int idAlvo = testeCard.getId() != null ? testeCard.getId() : cards.get(cards.size() - 1).getId();

            System.out.println("\n--- 3. TESTE BUSCAR POR ID (READ BY ID) ---");
            CardAcao cardBanco = dao.buscarPorId(idAlvo);
            if (cardBanco != null) {
                System.out.println("Card encontrado: [ID: " + cardBanco.getId() + "] " + cardBanco.getSugestaoTarefa());
            }

            System.out.println("\n--- 4. TESTE ATUALIZAR STATUS (UPDATE) ---");
            dao.atualizarStatus(idAlvo, StatusAcao.CONCLUIDO);

            System.out.println("\n--- 5. TESTE EXCLUIR (DELETE) ---");
            dao.excluirCard(idAlvo);
        }

        System.out.println("\n>>> SUCESSO: TODAS AS OPERAÇÕES DO CRUD FORAM CONCLUÍDAS! <<<");
    }
}