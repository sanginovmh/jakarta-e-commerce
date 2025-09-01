<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Panel</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fa;
        }

        .navbar {
            background-color: #343a40;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
        }

        .navbar h1 {
            margin: 0;
            font-size: 20px;
        }

        .navbar .user {
            font-size: 14px;
            color: #ccc;
        }

        .container {
            padding: 40px;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
        }

        h2 {
            margin-bottom: 20px;
            font-weight: normal;
            color: #333;
        }

        .nav-links {
            display: flex;
            gap: 15px;
        }

        .nav-links a {
            display: inline-block;
            padding: 12px 18px;
            background-color: #28a745;
            color: white;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
            transition: background 0.2s;
        }

        .nav-links a:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>Admin Panel</h1>
    <div class="user">Welcome, <c:out value="${fullName}"/></div>
</div>

<div class="container">
    <h2>Dashboard</h2>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/cabinet/manage-users">Manage Users</a>
        <a href="${pageContext.request.contextPath}/cabinet/manage-products">Manage Products</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</div>
<a href="${pageContext.request.contextPath}/logout">Logout</a>


</body>
</html>
