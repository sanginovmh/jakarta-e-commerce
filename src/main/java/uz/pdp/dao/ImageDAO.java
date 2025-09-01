package uz.pdp.dao;

import jakarta.servlet.http.Part;
import lombok.SneakyThrows;
import uz.pdp.db.DbSource;
import uz.pdp.entity.Image;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class ImageDAO {
    private static ImageDAO instance;
    private static final String location = "src/main/img/";

    private ImageDAO() {
    }

    public static ImageDAO getInstance() {
        if (instance == null) {
            instance = new ImageDAO();
        }
        return instance;
    }


    public int uploadAndSaveImage(Part part) {
        Image image = uploadImage(part);
        return saveImage(image);
    }

    @SneakyThrows
    public Optional<Image> findById(Integer id) {
        String query = "SELECT * FROM images WHERE id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(getImageFromRs(rs));
                }
                return Optional.empty();
            }
        }
    }

    @SneakyThrows
    public void deleteImage(Integer id) {
        String query = "DELETE FROM images WHERE id = ?";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }

    @SneakyThrows
    private Image uploadImage(Part part) {
        String fileName = part.getSubmittedFileName();
        String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
        Image image = Image.builder()
                .fileName(fileName)
                .fileSize(part.getSize())
                .fileLocation(location + fileName)
                .suffix(suffix)
                .build();
        Files.copy(part.getInputStream(),
                Paths.get(location + fileName));
        return image;
    }

    @SneakyThrows
    private int saveImage(Image image) {
        String query = "INSERT INTO images (file_name, file_size, file_location, suffix) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DbSource.getConnection().prepareStatement(query)) {
            ps.setString(1, image.getFileName());
            ps.setLong(2, image.getFileSize());
            ps.setString(3, image.getFileLocation());
            ps.setString(4, image.getSuffix());
            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getInt(1);
                }
            }
        }
        return -1;
    }

    @SneakyThrows
    private Image getImageFromRs(ResultSet rs) {
        return Image.builder()
                .fileName(rs.getString("file_name"))
                .fileSize(rs.getLong("file_size"))
                .fileLocation(rs.getString("file_location"))
                .suffix(rs.getString("suffix"))
                .build();
    }
}
