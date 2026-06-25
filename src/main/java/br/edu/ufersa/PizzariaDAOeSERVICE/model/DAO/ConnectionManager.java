package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectionManager {

    private static final String URL  = "jdbc:mysql://localhost/pizzariadb";
    private static final String USER = "root";
    private static final String PASS = "gadelhameajude";

    //unica instância da classe
    private static ConnectionManager instance;

    private ConnectionManager() {
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
