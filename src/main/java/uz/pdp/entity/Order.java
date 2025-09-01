package uz.pdp.entity;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Basket basket;
    private List<OrderItem> orderItemList;
}
