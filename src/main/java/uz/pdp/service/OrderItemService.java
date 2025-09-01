package uz.pdp.service;

import uz.pdp.entity.BrowserItem;
import uz.pdp.entity.OrderItem;

import java.util.LinkedList;
import java.util.List;

public class OrderItemService {
    private static OrderItemService instance;

    private OrderItemService() {}

    public static OrderItemService getInstance() {
        if (instance == null) {
            instance = new OrderItemService();
        }
        return instance;
    }

    public List<OrderItem> convertToOrderItems(Integer basketId, List<BrowserItem> browserItemList) {
        List<OrderItem> orderItemList = new LinkedList<>();
        for (BrowserItem browserItem : browserItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBasketId(basketId);
            orderItem.setProductId(browserItem.getProductId());
            orderItem.setName(browserItem.getName());
            orderItem.setPrice(browserItem.getPrice());
            orderItem.setAmount(browserItem.getAmount());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }
}
