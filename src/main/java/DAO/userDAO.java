package DAO;

import java.sql.*;


import Model.Account;
import Util.ConnectionUtil;

public class userDAO {
    public Account getUserByUsernameAndPassword(String username, String password) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password")
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public Account insertUser(Account account) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());

        int rowsInserted = ps.executeUpdate();

        if (rowsInserted > 0) {
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                return new Account(generatedId, account.getUsername(), account.getPassword());
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public Account getUserById(int id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}



}
