package uz.pdp.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.BrowserItem;

import java.util.List;

@RequiredArgsConstructor
public class RequestBasketConfirmationPopupHelper {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    @SneakyThrows
    public void forwardWithConfirmationPopup(List<BrowserItem> invalidItemsList, LineItemDAO lineItemDAO, Integer basketId, ProductDAO productDAO) {
        req.setAttribute("showInvalidPopup", true);
        List<BrowserItem> forConfirmationList = invalidItemsList.stream()
                .filter(i -> lineItemDAO.makeAmountValid(i.getProductId(), basketId, i.getQuantity()))
                .toList();
        List<BrowserItem> deletedList = invalidItemsList.stream()
                .filter(i -> productDAO.getQuantity(i.getProductId()) == 0)
                .toList();
        req.setAttribute("forConfirmationList", forConfirmationList);
        req.setAttribute("deletedList", deletedList);
        req.getRequestDispatcher("/templates/view-basket.jsp").forward(req, resp);
    }
}
