package uz.pdp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrowserItem {
    private Integer productId;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Integer amount;
}
