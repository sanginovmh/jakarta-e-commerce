<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My Baskets</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f8f9fa;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            margin: 20px 0;
            color: #333;
        }

        .container {
            width: 85%;
            margin: auto;
            background: #fff;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            border: 1px solid #e0e0e0;
            padding: 10px 12px;
            text-align: center;
        }

        th {
            background: #343a40;
            color: #fff;
        }

        tr:nth-child(even) {
            background: #f2f2f2;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 10px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-yes {
            background: #28a745;
            color: #fff;
        }

        .badge-no {
            background: #dc3545;
            color: #fff;
        }

        .actions form {
            display: inline;
        }

        .actions button {
            background: #007bff;
            border: none;
            color: #fff;
            padding: 6px 12px;
            border-radius: 6px;
            cursor: pointer;
        }

        .actions button.delete {
            background: #dc3545;
        }

        .msg {
            text-align: center;
            margin: 10px 0;
            color: green;
            font-weight: bold;
        }
    </style>
</head>
<body>

<h2>Manage My Baskets</h2>

<div class="container">

    <!-- Add New Basket -->
    <form action="${pageContext.request.contextPath}/cabinet/manage-baskets/add" method="post"
          style="margin-bottom:20px; text-align:center;">
        <input type="text" name="name" placeholder="Enter basket name" required
               style="padding:8px 12px; border-radius:6px; border:1px solid #ccc; width:40%;"/>
        <button type="submit"
                style="background:#28a745; border:none; color:#fff; padding:8px 16px; border-radius:6px; cursor:pointer; margin-left:8px;">
            Add Basket
        </button>
    </form>

    <c:if test="${not empty message}">
        <div class="msg">
            <c:out value="${message}"/>
        </div>
    </c:if>

    <c:if test="${empty basketList}">
        <p style="text-align:center;">No active baskets found.</p>
    </c:if>

    <c:if test="${not empty basketList}">
        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Ordered</th>
                <th>User</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="basket" items="${basketList}">
                <tr onclick="window.location='${pageContext.request.contextPath}/cabinet/manage-baskets/view?basketId=${basket.id}'" style="cursor:pointer;">
                    <td>
                        <a href="${pageContext.request.contextPath}/cabinet/manage-baskets/view?basketId=${basket.id}">
                                ${basket.name}
                        </a>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${basket.ordered}">
                                <span class="badge badge-yes">Yes</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-no">No</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${basket.user.fullName}</td>
                    <td class="actions">
                        <!-- delete button stays as form -->
                        <form action="${pageContext.request.contextPath}/cabinet/manage-baskets/delete" method="post">
                            <input type="hidden" name="basketId" value="${basket.id}"/>
                            <button type="submit" class="delete"
                                    onclick="return confirm('Delete this basket?')">Delete
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<a href="${pageContext.request.contextPath}/cabinet">Back</a>


</body>
</html>
