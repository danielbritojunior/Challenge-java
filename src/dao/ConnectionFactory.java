package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "rm562270";
    private static final String PASS = "020906";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do Oracle não encontrado.");
        } catch (SQLException e) {
            System.err.format("Erro de conexão SQL: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return conn;
    }
}