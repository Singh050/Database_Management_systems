<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - User Management</title>
    <style>
        button {
            size: 15px;
            padding: 5px;
        }
    </style>
</head>
<body>
    <h1>Add User</h1>
    <br/>
    <br/>
    <form action="${pageContext.request.contextPath}/users" method="POST">
        <label for="name">Customer Name:</label>
        <input type="text" id="name" name="name" required>
        <br />
        <br />
        <label for="address">Customer Address:</label>
        <input type="text" id="address" name="address" required>
        <br />
        <br />
        <label for="category">Category:</label>
        <input type="number" id="category" name="category" required>
        <br />
        <br />
        <button type="submit">Add</button>
    </form>
</body>
</html>