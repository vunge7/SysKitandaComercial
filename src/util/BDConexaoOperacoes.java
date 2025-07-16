/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Vector;
import static util.JPAEntityMannagerFactoryUtil.leituraFicheiro;

/**
 *
 * @author marti
 */

public class BDConexaoOperacoes {

    private Connection connection;

    public BDConexaoOperacoes() {
        try {
            Vector<String> informacao = leituraFicheiro();

            String ip = informacao.get(0);
            String porta = informacao.get(1);
            String bd = DVML.BD;

            String url = "jdbc:mysql://" + ip + ":" + porta + "/" + bd + "?zeroDateTimeBehavior=convertToNull";
            String user = "root";
            String password = "DoV90x?#";

            this.connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.setAutoCommit(true); // garantir que reset
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

