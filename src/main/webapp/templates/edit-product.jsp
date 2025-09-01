<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Product</title>
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
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
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

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: bold;
            margin-top: 10px;
            margin-bottom: 5px;
        }

        input, textarea {
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 6px;
            outline: none;
        }

        input:focus, textarea:focus {
            border-color: #007bff;
        }

        button {
            margin-top: 20px;
            padding: 12px;
            font-size: 15px;
            background-color: #007bff;
            border: none;
            color: white;
            border-radius: 6px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>Seller Panel</h1>
</div>

<div class="container">
    <h2>Edit Product</h2>

    <c:if test="${not empty message}">
        <div class="message error">
            <c:out value="${message}"/>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/cabinet/manage-products/edit" method="post">
        <input type="hidden" name="id" value="${product.id}"/>

        <label for="name">Product Name</label>
        <input type="text" id="name" name="name" value="${product.name}" required/>

        <label for="description">Description</label>
        <textarea id="description" name="description" rows="4" required>${product.description}</textarea>

        <label for="price">Price</label>
        <input type="number" id="price" name="price" step="0.01" value="${product.price}" required/>

        <label for="quantity">Quantity</label>
        <input type="number" id="quantity" name="quantity" value="${product.quantity}" required/>

        <button type="submit">Save Changes</button>
    </form>
</div>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
<a href="${pageContext.request.contextPath}/cabinet/manage-products">Back</a>


</body>
</html>
