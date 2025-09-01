package uz.pdp.service;

import uz.pdp.entity.Basket;
import uz.pdp.entity.Order;
import uz.pdp.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrderService {
    private static OrderService instance;

    private OrderService() {
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public List<Order> getOrderList(List<Basket> basketList, Function<Integer, List<OrderItem>> getOrderListFunction) {
        List<Order> orderList = new ArrayList<>(basketList.size());
        for (Basket basket : basketList) {
            Order order = new Order();
            order.setBasket(basket);
            order.setOrderItemList(getOrderListFunction.apply(basket.getId()));
            orderList.add(order);
        }
        return orderList;
    }
}
