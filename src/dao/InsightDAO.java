package dao;

import model.AlertaRisco;
import model.GatilhoCompra;
import model.Insight;

import java.sql.*;
import java.time.LocalTime;


public class InsightDAO {

    private static final String SEPARADOR = " ::: ";

    private static final String SQL_PROXIMO_ID =
            "SELECT NVL(MAX(ID_INSIGHT), 0) + 1 FROM INSIGHT_IA";

    private static final String SQL_INSERIR =
            "INSERT INTO INSIGHT_IA (ID_INSIGHT, TIPO_INSIGHT, MINUTO_REUNIAO, DESCRICAO, REUNIAO_ID_REUNIAO) "
                    + "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID_INSIGHT, TIPO_INSIGHT, MINUTO_REUNIAO, DESCRICAO FROM INSIGHT_IA WHERE ID_INSIGHT = ?";


    private static final String SQL_REUNIAO_VALIDA =
            "SELECT ID_REUNIAO FROM REUNIAO WHERE ROWNUM = 1";

    public int inserirInsight(Insight insight) {
        String tipo;
        String dadoExtra;

        if (insight instanceof AlertaRisco risco) {
            tipo = "RISCO";
            dadoExtra = risco.getConcorrenteIdentificado();
        } else if (insight instanceof GatilhoCompra gatilho) {
            tipo = "COMPRA";
            dadoExtra = gatilho.getProdutoSugerido();
        } else {
            throw new IllegalArgumentException("Tipo de Insight não suportado: " + insight.getClass());
        }

        try (Connection conn = ConnectionFactory.getConnection()) {

            int proximoId = 1;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SQL_PROXIMO_ID)) {
                if (rs.next()) {
                    proximoId = rs.getInt(1);
                }
            }

            int idReuniao = obterIdReuniaoValida(conn);
            if (idReuniao == -1) {
                System.err.println("Nenhuma REUNIAO encontrada no banco para vincular o insight "
                        + "(REUNIAO_ID_REUNIAO é obrigatório). Cadastre ao menos uma REUNIAO antes de inserir cards.");
                return -1;
            }

            // MINUTO_REUNIAO não tem como guardar hora:minuto:segundo completos;
            // convertemos o LocalTime em "minutos desde a meia-noite".
            int minutoReuniao = insight.getTimestamp().getHour() * 60 + insight.getTimestamp().getMinute();
            String descricaoCompleta = insight.getTrechoTexto() + SEPARADOR + dadoExtra;

            try (PreparedStatement ps = conn.prepareStatement(SQL_INSERIR)) {
                ps.setInt(1, proximoId);
                ps.setString(2, tipo);
                ps.setInt(3, minutoReuniao);
                ps.setString(4, descricaoCompleta);
                ps.setInt(5, idReuniao);
                ps.executeUpdate();
            }

            insight.setId(proximoId);
            return proximoId;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir insight: " + e.getMessage());
            return -1;
        }
    }

    // Busca e reconstrói o Insight com o TIPO REAL (AlertaRisco ou GatilhoCompra).
    public Insight buscarPorId(int idInsight) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, idInsight);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("ID_INSIGHT");
                    String tipo = rs.getString("TIPO_INSIGHT");
                    int minuto = rs.getInt("MINUTO_REUNIAO");
                    String descricaoCompleta = rs.getString("DESCRICAO");

                    LocalTime timestamp = LocalTime.of(minuto / 60, minuto % 60);

                    String trecho;
                    String dadoExtra;
                    int idx = descricaoCompleta == null ? -1 : descricaoCompleta.indexOf(SEPARADOR);
                    if (idx >= 0) {
                        trecho = descricaoCompleta.substring(0, idx);
                        dadoExtra = descricaoCompleta.substring(idx + SEPARADOR.length());
                    } else {
                        trecho = descricaoCompleta;
                        dadoExtra = "Não identificado";
                    }

                    if ("RISCO".equals(tipo)) {
                        return new AlertaRisco(id, timestamp, trecho, dadoExtra);
                    } else {
                        return new GatilhoCompra(id, timestamp, trecho, dadoExtra);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar insight: " + e.getMessage());
        }
        return null;
    }

    private int obterIdReuniaoValida(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_REUNIAO_VALIDA)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
}