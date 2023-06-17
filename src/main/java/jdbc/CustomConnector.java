package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnector {

    public Connection getConnection(String url) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url)) {
            return connection;
        }
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            return connection;
        }
    }
}