package uz.pdp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrderItem {
    private Integer basketId;
    private Integer productId;
    private String  name;
    private Double price;
    private Integer amount;
}
