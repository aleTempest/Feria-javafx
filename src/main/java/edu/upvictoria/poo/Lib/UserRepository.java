package edu.upvictoria.poo.Lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserRepository {
    private final String DB_PATH;

    public UserRepository(String path) {
        this.DB_PATH = path;
    }

    private Connection connect() {
        Connection connection = null;
        try {
            var url = "jdbc:sqlite:" + this.DB_PATH;
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
