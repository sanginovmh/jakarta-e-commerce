<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin – Manage Products</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fa;
        }

        .navbar {
            background-color: #212529;
            padding: 15px 20px;
            color: white;
        }

        .navbar h1 {
            margin: 0;
            font-size: 20px;
        }

        .container {
            padding: 40px;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
            font-weight: normal;
        }

        .message {
            margin-bottom: 20px;
            padding: 12px 16px;
            border-radius: 6px;
            font-size: 14px;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: left;
            font-size: 14px;
        }

        th {
            background-color: #495057;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .actions a {
            margin-right: 10px;
            text-decoration: none;
            color: #007bff;
        }
        .actions a:hover {
            text-decoration: underline;
        }

        .danger {
            color: #dc3545 !important;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>Admin Panel – Manage Products</h1>
</div>

<div class="container">
    <h2>All Products</h2>

    <c:if test="${not empty message}">
        <div class="message success">
            <c:out value="${message}"/>
        </div>
    </c:if>

    <table>
        <thead>
        <tr>
            <th>Seller</th>
            <th>Name</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${productList}">
            <tr>
                <td><c:out value="${p.user.fullName}"/></td>
                <td><c:out value="${p.name}"/></td>
                <td>$<c:out value="${p.price}"/></td>
                <td><c:out value="${p.quantity}"/></td>
                <td><c:out value="${p.description}"/></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/cabinet/manage-products/delete?id=${p.id}" class="danger">Delete</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty productList}">
            <tr>
                <td colspan="7">No products found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
<a href="${pageContext.request.contextPath}/cabinet">Back</a>

</body>
</html>
