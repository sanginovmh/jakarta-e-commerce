<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Browse Products</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f8f9fa;
            margin: 0;
            padding: 20px;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        .container {
            width: 90%;
            margin: auto;
        }

        .basket-form {
            text-align: center;
            margin-bottom: 20px;
        }

        select, button, input[type=number] {
            padding: 6px 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #e0e0e0;
            padding: 10px;
            text-align: center;
        }

        th {
            background: #343a40;
            color: #fff;
        }

        tr:nth-child(even) {
            background: #f2f2f2;
        }

        .actions button {
            padding: 4px 8px;
            margin: 0 2px;
            cursor: pointer;
        }

        .order-btn {
            background: #28a745;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            margin-bottom: 10px;
        }
    </style>
    <script>
        function increment(id, max) {
            let input = document.getElementById('amount-' + id);
            let val = parseInt(input.value || 0);
            if (val < max) input.value = val + 1;
        }

        function decrement(id) {
            let input = document.getElementById('amount-' + id);
            let val = parseInt(input.value || 0);
            if (val > 0) input.value = val - 1;
        }

        function validateAmount(input, max) {
            if (input.value < 0) input.value = 0;
            if (input.value > max) input.value = max;
        }
    </script>
</head>
<body>

<div class="container">
    <h2>Browse Products</h2>

    <div class="basket-form">
        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse">
            <label for="basketId">Choose a Basket:</label>
            <select name="basketId" id="basketId" required>
                <option value="">-- Select Basket --</option>
                <c:forEach var="basket" items="${basketList}">
                    <option value="${basket.id}"
                            <c:if test="${basket.id == selectedBasketId}">selected</c:if>>
                            ${basket.name}
                    </option>
                </c:forEach>
            </select>
            <button type="submit">Load</button>
        </form>
    </div>

    <c:if test="${not empty browserItem}">
        <!-- Order basket form, separate -->
        <form method="post" action="${pageContext.request.contextPath}/cabinet/order-basket">
            <input type="hidden" name="basketId" value="${param.basketId}"/>
            <button type="submit" class="order-btn">Order This Basket</button>
        </form>

        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Price ($)</th>
                <th>In Stock</th>
                <th>Amount</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${browserItem}">
                <tr>
                    <td>${item.name}</td>
                    <td>${item.description}</td>
                    <td>${item.price}</td>
                    <td>${item.quantity}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/persist">
                            <input type="hidden" name="basketId" value="${param.basketId}"/>
                            <input type="hidden" name="productId" value="${item.productId}"/>
                            <input type="number"
                                   name="amount"
                                   value="${item.amount}"
                                   min="0"
                                   max="${item.quantity}"
                                   onchange="this.form.submit()"/>
                        </form>
                    </td>
                    <td class="actions">
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/decrement"
                              style="display:inline;">
                            <input type="hidden" name="basketId" value="${param.basketId}"/>
                            <input type="hidden" name="productId" value="${item.productId}"/>
                            <button type="submit">-</button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/cabinet/browse/increment"
                              style="display:inline;">
                            <input type="hidden" name="basketId" value="${param.basketId}"/>
                            <input type="hidden" name="productId" value="${item.productId}"/>
                            <button type="submit">+</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>


    <a href="${pageContext.request.contextPath}/cabinet">Back</a>

</div>

</body>
</html>
