package uz.pdp.entity;

import lombok.Data;

@Data
public class LineItem {
    Integer basketId;
    Integer productId;
    Integer amount;
}
