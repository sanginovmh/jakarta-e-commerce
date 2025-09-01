<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home Page</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fa;
        }

        .navbar {
            background-color: #28a745;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            color: white;
            margin: 0;
            font-size: 20px;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
            font-size: 14px;
        }

        .navbar a:hover {
            text-decoration: underline;
        }

        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 60px 20px;
        }

        .container h2 {
            color: #333;
            font-weight: normal;
        }

        .links {
            margin-top: 20px;
        }

        .links a {
            display: inline-block;
            margin: 0 10px;
            padding: 12px 20px;
            background: #28a745;
            color: white;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
        }

        .links a:hover {
            background: #218838;
        }
    </style>
</head>
<body>

<div class="navbar">
    <h1>MyApp</h1>
    <div>
        <a href="${pageContext.request.contextPath}/auth/signup">Register</a>
        <a href="${pageContext.request.contextPath}/auth/signin">Sign in</a>
    </div>
</div>

<div class="container">
    <h2>Welcome to the Home Page!</h2>
    <div class="links">
        <a href="${pageContext.request.contextPath}/auth/signup">Register</a>
        <a href="${pageContext.request.contextPath}/auth/signin">Sign in</a>
    </div>
</div>

</body>
</html>
