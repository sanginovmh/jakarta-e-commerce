package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.LineItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LineItemDAO {
    private static LineItemDAO instance;

    private LineItemDAO() {
    }

    public static LineItemDAO getInstance() {
        if (instance == null) {
            instance = new LineItemDAO();
        }
        return instance;
    }

    @SneakyThrows
    public int getAmount(Integer productId, Integer basketId) {
        String query = "SELECT amount FROM line_items WHERE product_id = ? AND basket_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, productId);
        preparedStatement.setInt(2, basketId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("amount");
        }
        return 0;
    }

    @SneakyThrows
    public void deleteLineItem(Integer productId, Integer basketId) {
        String query = "DELETE FROM line_items WHERE product_id = ? AND basket_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, productId);
        preparedStatement.setInt(2, basketId);
        preparedStatement.execute();
    }

    @SneakyThrows
    public void incrementAmount(Integer productId, Integer basketId) {
        if (exists(productId, basketId)) {
            String query = "UPDATE line_items SET amount = (amount + 1) WHERE product_id = ? AND basket_id = ?";
            PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, basketId);
            preparedStatement.execute();
            return;
        }
        createLineItem(productId, basketId);
    }

    @SneakyThrows
    public void decrementAmount(Integer productId, Integer basketId) {
        if (getAmount(productId, basketId) == 0) {
            return;
        }
        if (getAmount(productId, basketId) == 1) {
            deleteLineItem(productId, basketId);
        }
        else {
            String query = "UPDATE line_items SET amount = (amount - 1) WHERE product_id = ? AND basket_id = ?";
            PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, basketId);
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    public List<LineItem> getLineItemsByBasketId(Integer id) {
        String query = "SELECT * FROM line_items WHERE basket_id = ? ORDER BY product_id";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<LineItem> lineItemList = new ArrayList<>();
        while (resultSet.next()) {
            lineItemList.add(getLineItemFromRs(resultSet));
        }
        return lineItemList;
    }

    @SneakyThrows
    public void persistLineItem(Integer productId, Integer basketId, Integer amount) {
        if (exists(productId, basketId)) {
            updateLineItem(productId, basketId, amount);
            return;
        }
        createLineItem(productId, basketId, amount);
    }

    @SneakyThrows
    public void deleteLineItemsByBasketId(Integer id) {
        String query = "DELETE FROM line_items WHERE basket_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public boolean makeAmountValid(Integer productId, Integer basketId, Integer quantity) {
        String query = "UPDATE line_items SET amount = ? WHERE product_id = ? AND basket_id = ?";
        if (quantity == 0) {
            deleteLineItem(productId, basketId);
            return false;
        }
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, quantity);
        preparedStatement.setInt(2, productId);
        preparedStatement.setInt(3, basketId);
        preparedStatement.execute();
        return true;
    }

    @SneakyThrows
    private void createLineItem(Integer productId, Integer basketId, Integer amount) {
        String query = "INSERT INTO line_items (product_id, basket_id, amount) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, productId);
        preparedStatement.setInt(2, basketId);
        preparedStatement.setInt(3, amount);
        preparedStatement.execute();
    }

    @SneakyThrows
    private void updateLineItem(Integer productId, Integer basketId, Integer amount) {
        if (amount == 0) {
            deleteLineItem(productId, basketId);
            return;
        }
        String query = "UPDATE line_items SET amount = ? WHERE product_id = ? AND basket_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, amount);
        preparedStatement.setInt(2, productId);
        preparedStatement.setInt(3, basketId);
        preparedStatement.execute();
    }

    @SneakyThrows
    private void createLineItem(Integer productId, Integer basketId) {
        String query = "INSERT INTO line_items (product_id, basket_id) VALUES (?, ?)";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, productId);
        preparedStatement.setInt(2, basketId);
        preparedStatement.execute();
    }

    @SneakyThrows
    private boolean exists(Integer productId, Integer basketId) {
        String query = "SELECT * FROM line_items WHERE product_id = ? AND basket_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, productId);
        preparedStatement.setInt(2, basketId);
        return preparedStatement.executeQuery().next();
    }

    @SneakyThrows
    private LineItem getLineItemFromRs(ResultSet resultSet) {
        LineItem lineItem = new LineItem();
        lineItem.setProductId(resultSet.getInt("product_id"));
        lineItem.setBasketId(resultSet.getInt("basket_id"));
        lineItem.setAmount(resultSet.getInt("amount"));
        return lineItem;
    }
}
