package dao;

import model.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CardAcaoDAO {

    private final InsightDAO insightDAO = new InsightDAO();

    public void inserirCard(CardAcao card) {
        String sqlId = "SELECT NVL(MAX(ID_CARD), 0) + 1 FROM CARD_ACAO";
        String sqlInsert = "INSERT INTO CARD_ACAO (ID_CARD, SUGESTAO_ACAO, STATUS_ACAO, INSIGHT_IA_ID_INSIGHT) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            int proximoId = 1;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlId)) {
                if (rs.next()) {
                    proximoId = rs.getInt(1);
                }
            }

            int idInsight = insightDAO.inserirInsight(card.getInsightVinculado());

            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, proximoId);
                ps.setString(2, card.getSugestaoTarefa());
                ps.setString(3, card.getStatus().name());
                ps.setInt(4, idInsight);

                ps.executeUpdate();
                card.setId(proximoId);
                System.out.println("Card salvo no Oracle com sucesso! ID: " + card.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir card: " + e.getMessage());
        }
    }

    public List<CardAcao> listarCards() {
        List<CardAcao> cards = new ArrayList<>();
        String sql = "SELECT ID_CARD, SUGESTAO_ACAO, STATUS_ACAO, INSIGHT_IA_ID_INSIGHT FROM CARD_ACAO ORDER BY ID_CARD";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cards.add(montarCard(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar cards: " + e.getMessage());
        }
        return cards;
    }

    public CardAcao buscarPorId(int id) {
        String sql = "SELECT ID_CARD, SUGESTAO_ACAO, STATUS_ACAO, INSIGHT_IA_ID_INSIGHT FROM CARD_ACAO WHERE ID_CARD = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarCard(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar card por ID: " + e.getMessage());
        }
        return null;
    }

    // 4. UPDATE (Atualizar apenas o status)
    public void atualizarStatus(int id, StatusAcao novoStatus) {
        String sql = "UPDATE CARD_ACAO SET STATUS_ACAO = ? WHERE ID_CARD = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, novoStatus.name());
            ps.setInt(2, id);

            int linhas = ps.executeUpdate();
            if (linhas > 0) {
                System.out.println("Status atualizado com sucesso no Oracle!");
            } else {
                System.out.println("Nenhum card localizado com ID " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
        }
    }

    public void atualizarCard(CardAcao card) {
        String sql = "UPDATE CARD_ACAO SET SUGESTAO_ACAO = ?, STATUS_ACAO = ? WHERE ID_CARD = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, card.getSugestaoTarefa());
            ps.setString(2, card.getStatus().name());
            ps.setInt(3, card.getId());

            int linhas = ps.executeUpdate();
            if (linhas > 0) {
                System.out.println("Card atualizado com sucesso no Oracle!");
            } else {
                System.out.println("Nenhum card localizado com ID " + card.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar card: " + e.getMessage());
        }
    }

    public void excluirCard(int id) {
        String sql = "DELETE FROM CARD_ACAO WHERE ID_CARD = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int linhas = ps.executeUpdate();
            if (linhas > 0) {
                System.out.println("Card removido do banco com sucesso!");
            } else {
                System.out.println("Nenhum card localizado com ID " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir card: " + e.getMessage());
        }
    }

    private CardAcao montarCard(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_CARD");
        String tarefa = rs.getString("SUGESTAO_ACAO");
        String statusStr = rs.getString("STATUS_ACAO");
        int idInsight = rs.getInt("INSIGHT_IA_ID_INSIGHT");

        StatusAcao status;
        try {
            status = StatusAcao.valueOf(statusStr != null ? statusStr.trim().toUpperCase() : "PENDENTE");
        } catch (IllegalArgumentException e) {
            status = StatusAcao.PENDENTE;
        }

        Insight insight = insightDAO.buscarPorId(idInsight);

        if (insight == null) {
            insight = new GatilhoCompra(LocalTime.now(), "Insight não localizado", "TOTVS");
        }

        return new CardAcao(id, insight, tarefa, status);
    }
}