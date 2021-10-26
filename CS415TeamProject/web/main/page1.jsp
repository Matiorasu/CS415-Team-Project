<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <head>
        
        <title>Main Page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="scripts/jquery-3.6.0.min.js"></script>
        
    </head>
    
    <body>
        
        <h1>Placeholder Page #1</h1>
        
        <p>
            <a href="<%= request.getContextPath() %>/main/page2.jsp">Placeholder #2</a>
        </p>
        
        <p>Click <a href="<%= request.getContextPath() %>/main/index.jsp">here</a> to return to the main landing page.</p>
        
        <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        
    </body>
    
</html>