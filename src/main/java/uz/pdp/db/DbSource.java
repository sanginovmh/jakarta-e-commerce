package uz.pdp.db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbSource {
    @Getter
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/marketdb",
                    "user_product_master",
                    "admin"
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
