<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Sign Up</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      margin: 0;
      background: #f5f7fa;
    }

    .container {
      width: 340px;
      background: #fff;
      padding: 25px;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 20px;
      font-weight: normal;
      color: #333;
    }

    form {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    input {
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 14px;
    }

    input:focus {
      border-color: #28a745;
      outline: none;
    }

    button {
      padding: 12px;
      background-color: #28a745;
      color: white;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 15px;
    }

    button:hover {
      background-color: #218838;
    }

    .message {
      margin-top: 15px;
      padding: 10px;
      border-radius: 6px;
      text-align: center;
      font-size: 14px;
    }

    .error {
      background: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .success {
      background: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>Create Account</h2>

  <c:if test="${message != null}">
    <div class="message error">
      <c:out value="${message}"/>
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/auth/signup" method="post">
    <input type="text" name="fullName" placeholder="Enter full name" required>
    <input type="email" name="email" placeholder="Enter email" required>
    <input type="password" name="password" placeholder="Enter password" required>
    <button type="submit">Register</button>
  </form>
</div>
<a href="${pageContext.request.contextPath}/auth/signin">Log in</a>


</body>
</html>
