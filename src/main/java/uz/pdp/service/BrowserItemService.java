package uz.pdp.service;

import uz.pdp.entity.BrowserItem;
import uz.pdp.entity.LineItem;
import uz.pdp.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BrowserItemService {
    private static BrowserItemService instance;

    private BrowserItemService() {}

    public static BrowserItemService getInstance() {
        if (instance == null) {
            instance = new BrowserItemService();
        }
        return instance;
    }

    public List<BrowserItem> alignBrowserItems(List<Product> productList, List<LineItem> lineItemList) {
        Map<Integer, Integer> lineItemAmountMap = lineItemList.stream()
                .collect(Collectors.toMap(LineItem::getProductId, LineItem::getAmount));

        List<BrowserItem> browserItemList = new ArrayList<>(productList.size());
        for (Product product : productList) {
            int amount = lineItemAmountMap.getOrDefault(product.getId(), 0);
            browserItemList.add(
                    new BrowserItem(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getQuantity(),
                            amount
                    )
            );
        }

        return browserItemList;
    }

    public List<BrowserItem> collectBrowserItems(List<Product> productList, List<LineItem> lineItemList) {
        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<BrowserItem> browserItemList = new ArrayList<>(productList.size());
        for (LineItem lineItem : lineItemList) {
            Product product = productMap.get(lineItem.getProductId());
            browserItemList.add(
                    new BrowserItem(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getQuantity(),
                            lineItem.getAmount()
                    )
            );
        }
        return browserItemList;
    }

    public List<BrowserItem> getInvalidItemsList(List<BrowserItem> browserItemList, BiPredicate<Integer, Integer> amountToQuantityCheck) {
        return browserItemList.stream()
                .filter(i -> amountToQuantityCheck.test(i.getAmount(), i.getProductId()))
                .toList();
    }
}
