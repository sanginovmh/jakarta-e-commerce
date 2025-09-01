package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.Product;
import uz.pdp.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {
    private static ProductDAO instance;
    private static final UserDAO userDAO = UserDAO.getInstance();

    private ProductDAO() {
    }

    public static ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }

    @SneakyThrows
    public void saveProduct(Product product, Integer userId) {
        String query = "INSERT INTO products (name, description, price, quantity, user_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = setUpPreparedStatement(product, userId, query);
        preparedStatement.execute();
    }

    @SneakyThrows
    private void deleteProduct(Integer id) {
        String query = "DELETE FROM products WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    @SneakyThrows
    public Optional<Product> findById(Integer id) {
        String query = "SELECT * FROM products WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(getProductFromRs(resultSet));
        }
        return Optional.empty();
    }

    @SneakyThrows
    public boolean checkUserIdMatch(Integer productId, Integer userId) {
        String query = "SELECT user_id FROM products WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, productId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("user_id") == userId;
    }

    @SneakyThrows
    public List<Product> getProductsByUserId(Integer id) {
        String query = "SELECT * FROM products WHERE user_id = ? ORDER BY id";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            productList.add(getProductFromRs(resultSet));
        }
        return productList;
    }

    @SneakyThrows
    public List<Integer> getProductIdsByUserId(Integer id) {
        String query = "SELECT id FROM products WHERE user_id = ? ORDER BY id";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Integer> productIdList = new ArrayList<>();
        while (resultSet.next()) {
            productIdList.add(resultSet.getInt("id"));
        }
        return productIdList;
    }

    @SneakyThrows
    public void updateProduct(Integer id, Product product) {
        String query = "UPDATE products " +
                "SET name = ?, " +
                "    description = ?, " +
                "    price = ?, " +
                "    quantity = ? " +
                "WHERE id = ?";
        PreparedStatement preparedStatement = setUpPreparedStatement(product, id, query);
        preparedStatement.execute();
    }

    @SneakyThrows
    public List<Product> getAllProducts() {
        return getAllProductsIncludingOutOfStock().stream().
                filter(p -> p.getQuantity() > 0)
                .toList();
    }

    @SneakyThrows
    public List<Product> getAllProductsIncludingOutOfStock() {
        String query = "SELECT * FROM products ORDER BY id";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Product> productList = new ArrayList<>();
        while (resultSet.next()) {
            productList.add(getProductFromRs(resultSet));
        }
        return productList;
    }

    @SneakyThrows
    private int deleteProductsByUserId(Integer id) {
        String query = "DELETE FROM products WHERE user_id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public int removeFromStockByUserId(Integer id) {
        String query = "UPDATE products SET quantity = 0 WHERE user_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    @SneakyThrows
    public int getQuantity(Integer id) {
        String query = "SELECT quantity FROM products WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("quantity");
        }
        return 0;
    }

    @SneakyThrows
    public void removeFromStock(Integer productId) {
        String query = "UPDATE products SET quantity = 0 WHERE id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    @SneakyThrows
    public void reduceQuantityBy(Integer productId, Integer amount) {
        String query = "UPDATE products SET quantity = (quantity - ?) WHERE id = ?";
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setInt(1, amount);
        preparedStatement.setInt(2, productId);
        preparedStatement.execute();
    }

    private PreparedStatement setUpPreparedStatement(Product product, Integer userId, String query) throws SQLException {
        PreparedStatement preparedStatement = DbSource.getConnection().prepareStatement(query);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setString(2, product.getDescription());
        preparedStatement.setDouble(3, product.getPrice());
        preparedStatement.setInt(4, product.getQuantity());
        preparedStatement.setInt(5, userId);
        return preparedStatement;
    }

    @SneakyThrows
    private Product getProductFromRs(ResultSet resultSet) {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getDouble("price"));
        product.setQuantity(resultSet.getInt("quantity"));
        Integer userId = resultSet.getInt("user_id");
        Optional<User> optionalUser = userDAO.findById(userId);
        optionalUser.ifPresent(product::setUser);
        return product;
    }
}
