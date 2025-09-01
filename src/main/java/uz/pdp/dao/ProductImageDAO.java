package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.Image;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductImageDAO {
    private static ProductImageDAO instance;
    private static final ImageDAO imageDAO = ImageDAO.getInstance();

    private ProductImageDAO() {
    }

    public static ProductImageDAO getInstance() {
        if (instance == null) {
            instance = new ProductImageDAO();
        }
        return instance;
    }

    @SneakyThrows
    public Optional<Image> getMainImageByProductId(Integer id) {
        String query = "SELECT image_id FROM product_images WHERE product_id = ? ORDER BY image_id LIMIT 1";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return imageDAO.findById(rs.getInt("image_id"));
                }
                return Optional.empty();
            }
        }
    }

    @SneakyThrows
    public void saveProductImage(Integer productId, Integer imageId) {
        String query = "INSERT INTO product_images (product_id, image_id) " +
                "VALUES (?, ?)";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, imageId);
            ps.execute();
        }
    }

    @SneakyThrows
    public void deleteProductImage(Integer productId, Integer imageId) {
        String query = "DELETE FROM product_images WHERE product_id = ? AND image_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, imageId);
            ps.execute();
        }
    }

    @SneakyThrows
    public void deleteByProductId(Integer productId) {
        String query = "DELETE FROM product_images WHERE product_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.execute();
        }
    }

    @SneakyThrows
    public List<Image> getImagesByProductId(Integer productId) {
        String query = "SELECT image_id FROM product_images WHERE product_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Image> imageList = new ArrayList<>();
                while (rs.next()) {
                    imageDAO.findById(rs.getInt("image_id"));
                }
                return imageList;
            }
        }
    }
}
