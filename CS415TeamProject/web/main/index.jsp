<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="db" class="edu.jsu.mcis.cs415.tas_fa21_v2.TASDatabase" scope="session"/>

<!DOCTYPE html>
<html>
    
    <head>
        
        <title>Main Page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="scripts/jquery-3.6.0.min.js"></script>
        
    </head>
    
    <body>
        
        <p>Congratulations, you have logged in successfully!</p>
        
        <%
            if ( (request.isUserInRole("admin")) || (request.isUserInRole("secretary")) ) {
        %>
        <p>
            <a href="<%= request.getContextPath() %>/main/punchedit1.jsp">Edit Punches</a>
        </p>
        <%
            }
        %>
        
        <p>
            <a href="<%= request.getContextPath() %>/main/page1.jsp">Placeholder #1</a>
            <a href="<%= request.getContextPath() %>/main/page2.jsp">Placeholder #2</a>
        </p>
        
        <p>Your username is: <%= request.getRemoteUser() %></p>
        
        <p>Your host name/address is: <%= request.getRemoteHost() %></p>
        
        <p>Your context path is: <%= request.getContextPath() %></p>
        
        <%
            if ( request.isUserInRole("admin") ) {
        %>

        <p>You have been assigned the ADMINISTRATOR role.</p>

        <%
            }
            else if ( request.isUserInRole("secretary") ) {
        %>

        <p>You have been assigned the SECRETARY role.</p>

        <%
            }
            else if ( request.isUserInRole("supervisor") ) {
        %>

        <p>You have been assigned the SUPERVISOR role.</p>
        
        <%
            }
        %>
        
        <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        
    </body>
    
</html>