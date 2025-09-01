<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Basket View</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f8f9fa; margin: 0; padding: 0; }
        .container { width: 90%; margin: 20px auto; background: #fff; padding: 20px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);}
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { border: 1px solid #e0e0e0; padding: 10px 12px; text-align: center; }
        th { background: #343a40; color: #fff; }
        tr:nth-child(even) { background: #f2f2f2; }
        .actions form { display: inline; }
        .actions button { background: #007bff; border: none; color: #fff; padding: 6px 12px; border-radius: 6px; cursor: pointer; }
        .actions button.delete { background: #dc3545; }
        .order-btn { margin-top: 20px; background: #28a745; border: none; color: #fff; padding: 10px 20px; border-radius: 8px; cursor: pointer; font-size: 16px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Basket Details</h2>

    <c:if test="${empty browserItemList}">
        <p style="text-align:center;">This basket is empty.</p>
    </c:if>

    <c:if test="${not empty browserItemList}">
        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Price ($) Per item</th>
                <th>In Stock</th>
                <th>Subtotal ($)</th>
                <th>Amount</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:set var="basketTotal" value="0" />
            <c:forEach var="item" items="${browserItemList}">
                <c:set var="lineTotal" value="${item.amount * item.price}" />
                <c:set var="basketTotal" value="${basketTotal + lineTotal}" />

                <tr>
                    <td>${item.name}</td>
                    <td>${item.description}</td>
                    <td>${item.price}</td>
                    <td>${item.quantity}</td>
                    <td>${lineTotal}</td>
                    <td>
                        <!-- update amount -->
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/persist">
                            <input type="hidden" name="basketId" value="${param.basketId}" />
                            <input type="hidden" name="productId" value="${item.productId}" />
                            <input type="number"
                                   name="amount"
                                   value="${item.amount}"
                                   min="0"
                                   max="${item.quantity}"
                                   onchange="this.form.submit()"/>
                        </form>
                    </td>
                    <td class="actions">
                        <!-- decrement -->
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/decrement" style="display:inline;">
                            <input type="hidden" name="basketId" value="${param.basketId}" />
                            <input type="hidden" name="productId" value="${item.productId}" />
                            <button type="submit">-</button>
                        </form>

                        <!-- increment -->
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/increment" style="display:inline;">
                            <input type="hidden" name="basketId" value="${param.basketId}" />
                            <input type="hidden" name="productId" value="${item.productId}" />
                            <button type="submit">+</button>
                        </form>

                        <!-- delete -->
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/manage-baskets/view/delete" style="display:inline;">
                            <input type="hidden" name="basketId" value="${param.basketId}" />
                            <input type="hidden" name="productId" value="${item.productId}" />
                            <button type="submit" class="delete" onclick="return confirm('Remove this item?')">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- show total -->
        <div style="margin-top:20px; font-weight:bold; text-align:right;">
            Total Payment: $<c:out value="${basketTotal}" />
        </div>

        <!-- order basket -->
        <form method="post" action="${pageContext.request.contextPath}/cabinet/manage-basket/view/order">
            <input type="hidden" name="basketId" value="${param.basketId}" />
            <button type="submit" class="order-btn">Order This Basket</button>
        </form>
    </c:if>


    <c:if test="${showInvalidPopup}">
        <div id="invalidModal" class="modal">
            <div class="modal-content">
                <h3>Some items need attention</h3>

                <c:if test="${not empty forConfirmationList}">
                    <p>These items exceed stock. Adjust amounts:</p>
                    <table>
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Requested</th>
                            <th>Available</th>
                            <th>Fix</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${forConfirmationList}">
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.amount}</td>
                                <td>${item.quantity}</td>
                                <td>
                                    <!-- reuse your persist form -->
                                    <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/persist">
                                        <input type="hidden" name="basketId" value="${param.basketId}" />
                                        <input type="hidden" name="productId" value="${item.productId}" />
                                        <input type="number" name="amount"
                                               value="${item.quantity}"
                                               min="0" max="${item.quantity}" />
                                        <button type="submit">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${not empty deletedList}">
                    <p>These items are out of stock and were removed:</p>
                    <ul>
                        <c:forEach var="item" items="${deletedList}">
                            <li><s>${item.name}</s></li>
                        </c:forEach>
                    </ul>
                </c:if>

                <button onclick="closeModal()">OK</button>
            </div>
        </div>

        <script>
            // simple modal show/hide
            document.getElementById("invalidModal").style.display = "block";
            function closeModal() {
                document.getElementById("invalidModal").style.display = "none";
                window.location.reload(); // reload page so user can try ordering again
            }
        </script>

        <style>
            .modal { display:none; position:fixed; z-index:1000; left:0; top:0;
                width:100%; height:100%; background:rgba(0,0,0,0.6);}
            .modal-content { background:#fff; margin:10% auto; padding:20px; width:60%;
                border-radius:10px; }
        </style>
    </c:if>

</div>

<a href="${pageContext.request.contextPath}/cabinet/manage-baskets">Back</a>
</body>
</html>
