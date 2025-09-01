package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.OrderItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class OrderItemDAO {
    private static OrderItemDAO instance;

    private OrderItemDAO() {
    }

    public static OrderItemDAO getInstance() {
        if (instance == null) {
            instance = new OrderItemDAO();
        }
        return instance;
    }

    @SneakyThrows
    public void saveOrderItems(List<OrderItem> orderItemList) {
        String query = "INSERT INTO ordered_items (basket_id, product_id, name, price, amount) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        for (OrderItem orderItem : orderItemList) {
            preparedStatement.setInt(1, orderItem.getBasketId());
            preparedStatement.setInt(2, orderItem.getProductId());
            preparedStatement.setString(3, orderItem.getName());
            preparedStatement.setDouble(4, orderItem.getPrice());
            preparedStatement.setInt(5, orderItem.getAmount());
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    public List<OrderItem> getOrderItemsByBasketId(Integer id) {
        String query = "SELECT * FROM ordered_items WHERE basket_id = ?";
        return getOrderItemsUsingId(id, query);
    }

    @SneakyThrows
    public List<OrderItem> getOrderItemsByProductId(Integer id) {
        String query = "SELECT * FROM ordered_items WHERE product_id = ?";
        return getOrderItemsUsingId(id, query);
    }

    private List<OrderItem> getOrderItemsUsingId(Integer id, String query) throws SQLException {
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<OrderItem> orderItemList = new LinkedList<>();
        while (resultSet.next()) {
            orderItemList.add(getOrderItemFromRs(resultSet));
        }
        return orderItemList;
    }

    @SneakyThrows
    private OrderItem getOrderItemFromRs(ResultSet resultSet) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBasketId(resultSet.getInt("basket_id"));
        orderItem.setProductId(resultSet.getInt("product_id"));
        orderItem.setName(resultSet.getString("name"));
        orderItem.setPrice(resultSet.getDouble("price"));
        orderItem.setAmount(resultSet.getInt("amount"));
        return orderItem;
    }
}
