<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>My Orders</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background: #f8f9fa;
            margin: 0;
            padding: 20px;
            color: #333;
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
            color: #222;
        }

        .basket {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            margin-bottom: 30px;
            overflow: hidden;
        }

        .basket-header {
            background: #343a40;
            color: #fff;
            padding: 15px 20px;
            font-size: 16px;
            font-weight: bold;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .basket-header span {
            font-weight: normal;
            font-size: 14px;
            opacity: 0.9;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background: #f1f3f5;
            color: #444;
            text-transform: uppercase;
            font-size: 13px;
            letter-spacing: 0.5px;
            padding: 12px;
            border-bottom: 2px solid #dee2e6;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #eee;
            text-align: center;
        }

        tbody tr:hover {
            background: #f9f9f9;
        }

        .total-row td {
            font-weight: bold;
            background: #f8f9fa;
        }

        .total-label {
            text-align: right;
            padding-right: 20px;
        }

        a.back-link {
            display: inline-block;
            text-decoration: none;
            margin-top: 20px;
            color: #fff;
            background: #007bff;
            padding: 10px 18px;
            border-radius: 8px;
            transition: background 0.2s;
        }

        a.back-link:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
<h1>Your Orders</h1>

<c:if test="${empty orderList}">
    <p style="text-align:center;">You have no orders yet.</p>
</c:if>

<c:forEach var="order" items="${orderList}">
    <div class="basket">
        <div class="basket-header">
            <div>Order from: <c:out value="${order.basket.name}"/></div>
            <span>Placed on
                <fmt:formatDate value="${order.basket.date}" pattern="yyyy-MM-dd HH:mm"/>
            </span>
        </div>

        <table>
            <thead>
            <tr>
                <th>Product</th>
                <th>Price</th>
                <th>Amount</th>
                <th>Line Total</th>
            </tr>
            </thead>
            <tbody>
            <c:set var="orderTotal" value="0"/>
            <c:forEach var="item" items="${order.orderItemList}">
                <c:set var="lineTotal" value="${item.price * item.amount}"/>
                <tr>
                    <td>${item.name}</td>
                    <td><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="$"/></td>
                    <td>${item.amount}</td>
                    <td><fmt:formatNumber value="${lineTotal}" type="currency" currencySymbol="$"/></td>
                </tr>
                <c:set var="orderTotal" value="${orderTotal + lineTotal}"/>
            </c:forEach>
            <tr class="total-row">
                <td colspan="3" class="total-label">Order Total:</td>
                <td><fmt:formatNumber value="${orderTotal}" type="currency" currencySymbol="$"/></td>
            </tr>
            </tbody>
        </table>
    </div>
</c:forEach>

<div style="text-align:center;">
    <a href="${pageContext.request.contextPath}/cabinet" class="back-link">Back to Cabinet</a>
</div>
</body>
</html>
