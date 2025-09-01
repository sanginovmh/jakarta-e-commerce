package uz.pdp.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Basket {
    private Integer id;
    private String name;
    private Boolean ordered;
    private User user;
    private Timestamp date;
}
