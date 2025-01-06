<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registration Success</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 400px;
            margin: auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
        }
        p {
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Registration Successful!</h2>
        <p>Thank you, <strong>${name}</strong>! You have registered successfully with the email <strong>${email}</strong>.</p>
        <p><a href="register.jsp">Register another user</a></p>
    </div>
</body>
</html>