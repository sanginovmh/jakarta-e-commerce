package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private static UserDAO instance;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    @SneakyThrows
    public void saveUser(User user) {
        String query = "INSERT INTO users (full_name, email, password, enabled, role) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setString(1, user.getFullName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setBoolean(4, user.getEnabled());
        preparedStatement.setString(5, user.getRole().name());
        preparedStatement.execute();
    }

    @SneakyThrows
    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setString(1, email);

        ResultSet resultSet = preparedStatement.executeQuery();
        User userFromRs = null;
        if (resultSet.next()) {
            userFromRs = getUserFromRs(resultSet);
        }
        return Optional.ofNullable(userFromRs);
    }

    @SneakyThrows
    public List<User> getAllUsers() {
        String query = "SELECT * FROM users ORDER BY id";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            userList.add(getUserFromRs(resultSet));
        }
        return userList;
    }

    @SneakyThrows
    public void deleteUser(Integer id) {
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public void disableUser(Integer id) {
        String query = "UPDATE users SET enabled = FALSE WHERE id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @SneakyThrows
    public String getRole(Integer id) {
        String query = "SELECT role FROM users WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("role");
    }

    @SneakyThrows
    public void updateRole(String role, Integer id) {
        String query = "UPDATE users SET role = ? WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setString(1, role);
        preparedStatement.setInt(2, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public Optional<User> findById(Integer id) {
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        User userFromRs = null;
        if (resultSet.next()) {
            userFromRs = getUserFromRs(resultSet);
        }
        return Optional.ofNullable(userFromRs);
    }

    @SneakyThrows
    private User getUserFromRs(ResultSet resultSet) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setEnabled(resultSet.getBoolean("enabled"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        return user;
    }
}
