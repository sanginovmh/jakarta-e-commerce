<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage Products</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fa;
        }

        .navbar {
            background-color: #343a40;
            padding: 15px 20px;
            color: white;
        }

        .navbar h1 {
            margin: 0;
            font-size: 20px;
        }

        .container {
            padding: 40px;
            max-width: 1000px;
            margin: auto;
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

        /* Add product form */
        .add-form {
            display: grid;
            grid-template-columns: repeat(5, 1fr) auto;
            gap: 10px;
            margin-bottom: 30px;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }

        .add-form input {
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .add-form button {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px 18px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }

        .add-form button:hover {
            background-color: #218838;
        }

        /* Table */
        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: left;
            font-size: 14px;
        }

        th {
            background-color: #007bff;
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
            font-weight: bold;
        }

        .actions a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>Seller Panel</h1>
</div>

<div class="container">
    <h2>Your Products</h2>

    <c:if test="${not empty message}">
        <div class="message error">
            <c:out value="${message}"/>
        </div>
    </c:if>

    <form class="add-form" action="${pageContext.request.contextPath}/cabinet/manage-products/add" method="post">
        <input type="text" name="name" placeholder="Name" required>
        <input type="text" name="description" placeholder="Description" required>
        <input type="number" name="price" placeholder="Price" step="0.01" required>
        <input type="number" name="quantity" placeholder="Quantity" required>
        <button type="submit">+ Add Product</button>
    </form>

    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Price ($)</th>
            <th>Quantity</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${productList}">
            <tr>
                <td><c:out value="${p.name}"/></td>
                <td><c:out value="${p.price}"/></td>
                <td><c:out value="${p.quantity}"/></td>
                <td><c:out value="${p.description}"/></td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/cabinet/manage-products/edit?id=${p.id}">Edit</a>
                    <a href="${pageContext.request.contextPath}/cabinet/manage-products/delete?id=${p.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty productList}">
            <tr>
                <td colspan="5">No products found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
<a href="${pageContext.request.contextPath}/cabinet">Back</a>

</body>
</html>
