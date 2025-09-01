package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.Basket;
import uz.pdp.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasketDAO {
    private static BasketDAO instance;
    private static UserDAO userDAO = UserDAO.getInstance();

    private BasketDAO() {
    }

    public static BasketDAO getInstance() {
        if (instance == null) {
            instance = new BasketDAO();
        }
        return instance;
    }

    @SneakyThrows
    public void saveBasket(Basket basket) {
        String query = "INSERT INTO baskets (name, user_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setString(1, basket.getName());
        preparedStatement.setInt(2, basket.getUser().getId());
        preparedStatement.execute();
    }

    public List<Basket> getActiveBaskets(Integer userId) {
        return getBasketsByUserId(userId).stream().filter(b -> b.getOrdered() == false).toList();
    }

    public List<Basket> getOrderedBaskets(Integer userId) {
        return getBasketsByUserId(userId).stream().filter(b -> b.getOrdered() == true).toList();
    }

    @SneakyThrows
    public Optional<Basket> findById(Integer id) {
        String query = "SELECT * FROM baskets WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(getBasketFromRs(resultSet));
        }
        return Optional.empty();
    }

    @SneakyThrows
    public void deleteBasket(Integer id) {
        String query = "DELETE FROM baskets WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public void deleteBasketsByUserId(Integer id) {
        String query = "DELETE FROM baskets WHERE user_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public void orderBasket(Integer id) {
        String query = "UPDATE baskets SET ordered = TRUE, date = now() WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public boolean checkUserIdMatch(Integer basketId, Integer userId) {
        String query = "SELECT user_id FROM baskets WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, basketId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("user_id") == userId;
        }
        return false;
    }

    @SneakyThrows
    private List<Basket> getBasketsByUserId(Integer id) {
        String query = "SELECT * FROM baskets WHERE user_id = ? ORDER BY id";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Basket> basketList = new ArrayList<>();
        while (resultSet.next()) {
            basketList.add(getBasketFromRs(resultSet));
        }
        return basketList;
    }

    @SneakyThrows
    private Basket getBasketFromRs(ResultSet resultSet) {
        Basket basket = new Basket();
        basket.setId(resultSet.getInt("id"));
        basket.setName(resultSet.getString("name"));
        basket.setOrdered(resultSet.getBoolean("ordered"));
        Optional<User> optionalUser = userDAO.findById(resultSet.getInt("user_id"));
        optionalUser.ifPresent(basket::setUser);
        basket.setDate(resultSet.getTimestamp("date"));
        return basket;
    }
}
