package uz.pdp.dao;

import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.Image;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class ProfileImageDAO {
    private static ProfileImageDAO instance;
    private static final ImageDAO imageDAO = ImageDAO.getInstance();

    private ProfileImageDAO() {
    }

    public static ProfileImageDAO getInstance() {
        if (instance == null) {
            instance = new ProfileImageDAO();
        }
        return instance;
    }

    @SneakyThrows
    public void saveUserImage(Integer userId, Integer imageId) {
        String query = "INSERT INTO profile_images (user_id, image_id) " +
                "VALUES (?, ?)";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, imageId);
            ps.execute();
        }
    }

    @SneakyThrows
    public void deleteProfileImage(Integer userId, Integer imageId) {
        String query = "DELETE FROM profile_images WHERE user_id = ? AND image_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, imageId);
            ps.execute();
        }
    }

    @SneakyThrows
    public Optional<Image> findByUserId(Integer userId) {
        String query = "SELECT image_id FROM profile_images WHERE user_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return imageDAO.findById(rs.getInt("image_id"));
                }
                return Optional.empty();
            }
        }
    }

    @SneakyThrows
    public void replaceProfileImages(Integer userId, Integer imageId) {
        String query = "UPDATE profile_images SET image_id = ? WHERE user_id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, imageId);
            ps.setInt(2, userId);
            ps.execute();
        }
    }
}
