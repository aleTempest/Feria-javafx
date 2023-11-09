package edu.upvictoria.poo.Lib;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository {
    private final String DB_PATH;

    public UserRepository(String path) {
        this.DB_PATH = path;
    }

    public static void main(String[] args) {
        var test = new UserRepository("src/main/resources/main.db");
        var arr = test.getAllUsers();
        for (var user : arr) {
            System.out.println(user);
        }
        test.buyTicket(1,4);
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

    private boolean auth(String username, String password) {
        try (Connection connection = connect();
             PreparedStatement query = connection.prepareStatement(
                     "SELECT username, password FROM user_credentials_view WHERE username = ? AND password = ?"
             )) {
            query.setString(1, username);
            query.setString(2, password);
            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private Integer getUserId(String str) {
        var sql = "SELECT user_id FROM USERS WHERE email = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, str);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void insertBasicInfo(String firstName, String lastName, String email, String phoneNumber) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO USERS (first_name, last_name, email, phone_number, tickets) VALUES (?,?,?,?,?)"
             )) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setInt(5, 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createUser(String firstName, String lastName, String email, String phoneNumber, String username,
                            String password) {
        try (Connection connection = connect()) {
            this.insertBasicInfo(firstName, lastName, email, phoneNumber);
            var userId = this.getUserId(email);

            try (PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO CREDENTIALS (user_id, username, password) VALUES (?,?,?)"
            )) {
                statement2.setInt(1, userId);
                statement2.setString(2, username);
                statement2.setString(3, password);
                statement2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String str, boolean byEmail) {
        var sql = "DELETE FROM USERS WHERE ";
        if (byEmail) {
            sql += "email = ?";
        } else {
            sql += "username = ?";
        }
        try (var connection = connect()) {
            try (var statement = connection.prepareStatement(sql)) {
                statement.setString(1,str);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(int id) {
        try(var connection = connect()) {
            try (var statement = connection.prepareStatement("DELETE FROM USERS WHERE user_id = ?")) {
                statement.setInt(1,id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertEntry(int entry_id, int user_id) {
        try (var connection = connect()) {
            try (var statement = connection.prepareStatement( "INSERT INTO ENTRIES (entry_id,user_id) VALUES (?,?)" )) {
                statement.setInt(1,entry_id);
                statement.setInt(2,user_id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void updateUserTickets(int entry_id,int user_id) {
        try (var connection = connect()) {
            try (var statement = connection.prepareStatement(
                    "UPDATE USERS SET tickets = tickets - (SELECT price FROM ENTRY WHERE entry_id = ?) " +
                            "WHERE user_id = ?"
            )) {
                statement.setInt(1,entry_id);
                statement.setInt(2,user_id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buyTicket(int entry_id, int user_id) {
        this.insertEntry(entry_id,user_id);
        this.updateUserTickets(entry_id,user_id);
    }

    public ArrayList<User> getAllUsers() {
        var arr = new ArrayList<User>();
        try (var connection = connect()) {
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("SELECT * FROM user_credentials_view");
                while (result.next()) {
                    var id = Integer.parseInt(result.getString("user_id"));
                    var firstName = result.getString("first_name");
                    var lastName = result.getString("last_name");
                    var email = result.getString("email");
                    var phone_number = result.getString("phone_number");
                    var tickets = Integer.parseInt(result.getString("tickets"));
                    var username = result.getString("username");
                    var password = result.getString("password");
                    var user = UserFactory.createUser(id,firstName,lastName,email,phone_number,username,password);
                    user.setTiketCount(tickets);
                    arr.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arr;
    }
}