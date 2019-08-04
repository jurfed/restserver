<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>parameter input</title>
</head>
<body>

<form action="${pageContext.servletContext.contextPath}/?webpage=1" method="POST">

    <br>
    <label><font size="+2">Please enter new parameters for saving</font></label>
    </br>

    <table border="1">
        <tr>
            <td>language</td>
            <td>sound</td>
            <td>credit</td>
        </tr>

            <tr valign="top">
                <td> <input type="text" name="language" value="${language}"> </td>
                <td> <input type="text" name="sound" value="${sound}"> </td>
                <td> <input type="text" name="credit" value="${credit}"> </td>
            </tr>

    </table>
    <br>
    <label><font size="+2">${message}</font></label>
    </br>

    <input type="hidden" name="webpage" value="1">
    <tr>
        <td><input type="submit" align="center" value="Submit"/></td>
    </tr>
</form>


</body>
</html>
