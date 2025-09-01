<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage Users</title>
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

        .container {
            padding: 40px;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
            font-weight: normal;
        }

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
            background-color: #28a745;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>Admin Panel</h1>
</div>

<div class="container">
    <h2>Managing Users</h2>
    <table>
        <thead>
        <tr>
            <th>Full Name</th>
            <th>Email</th>
            <th>Enabled</th>
            <th>Role</th>
            <th>Delete</th>
            <th>Clear products</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${userList}">
            <tr>
                <td><c:out value="${u.fullName}"/></td>
                <td><c:out value="${u.email}"/></td>
                <td><c:out value="${u.enabled}"/></td>
                <td>
                    <form action="${pageContext.request.contextPath}/cabinet/manage-users/update-role" method="post">
                        <input type="hidden" name="id" value="${u.id}"/>
                        <select name="role" onchange="this.form.submit()">
                            <c:forEach var="r" items="${roles}">
                                <option value="${r}" <c:if test="${r == u.role}">selected</c:if>>
                                        ${r}
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </td>
                <td><a href="${pageContext.request.contextPath}/cabinet/manage-users/delete?id=${u.id}">Delete</a></td>
                <td><a href="${pageContext.request.contextPath}/cabinet/manage-users/clear-products?id=${u.id}">Clear</a></td>
            </tr>
        </c:forEach>

        <c:if test="${message != null}">
            <div class="message error">
                <c:out value="${message}"/>
            </div>
        </c:if>
        </tbody>
    </table>
</div>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
<a href="${pageContext.request.contextPath}/cabinet">Back</a>


</body>
</html>
