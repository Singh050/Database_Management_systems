<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - User Management</title>
    <style>
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 60%;
        }

        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        button {
            size: 15px;
            padding: 5px;
        }
    </style>
</head>
<body>
    <h1>List User</h1>
    <br/>
    <label for="firstCategory">The first category:</label>
    <input type="number" id="firstCategory" name="firstCategory" onchange="changeFirstCategory()">
    <br/>
    <label id="errorCategory1" style="visibility: hidden">Please enter this category</label>
    <br/>
    <br/>
    <label for="firstCategory">The second category:</label>
    <input type="number" id="secondCategory" name="secondCategory" onchange="changeSecondCategory()">
    <br/>
    <label id="errorCategory2" ></label>
    <br/>
    <br/>
    <button onclick="search()">Search</button>
    <button onclick="add()">Add</button>
    <br/>
    <br/>
    <table>
        <tr>
            <th>Customer ID</th>
            <th>Customer Name</th>
            <th>Customer Address</th>
            <th>Category</th>
        </tr>
        <c:if test="${customerList.size() > 0}">
            <c:forEach items="${customerList}" var="customer">
                <tr>
                    <td><c:out value="${customer.customerId}" /></td>
                    <td><c:out value="${customer.name}" /></td>
                    <td><c:out value="${customer.address}" /></td>
                    <td><c:out value="${customer.category}" /></td>
                </tr>
            </c:forEach>
        </c:if>
    </table>
    <script>
        function changeFirstCategory() {
            const firstCategory = document.getElementById("errorCategory1");
            firstCategory.style.visibility = "hidden";
        }

        function changeSecondCategory() {
            const secondCategory = document.getElementById("errorCategory2");
            secondCategory.style.visibility = "hidden";
        }

        function add() {
            const a = document.createElement("a");
            a.href = "${pageContext.request.contextPath}/users?type=add";
            a.click();
        }

        function search() {
            const firstCategory = document.getElementById("firstCategory");
            if (!firstCategory.value) {
                document.getElementById("errorCategory1").style.visibility = "visible";
                return;
            }
            const secondCategory = document.getElementById("secondCategory");
            if (!secondCategory.value) {
                const errorCategory2 = document.getElementById("errorCategory2");
                errorCategory2.textContent = "Please enter this category";
                errorCategory2.style.visibility = "visible";
                return;
            } else if (firstCategory.value >= secondCategory.value) {
                const errorCategory2 = document.getElementById("errorCategory2");
                errorCategory2.textContent = "The category 2 not is less than the category 1";
                errorCategory2.style.visibility = "visible";
                return;
            }
            const a = document.createElement("a");
            a.href = "${pageContext.request.contextPath}/users?type=search&category1=" + firstCategory.value + "&category2=" + secondCategory.value;
            a.click();
        }
    </script>
</body>
</html>