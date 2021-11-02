<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="db" class="edu.jsu.mcis.cs415.tas_fa21_v2.TASDatabase" scope="session"/>

<!DOCTYPE html>
<html>
    
    <head>
        
        <title>Edit Punches</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="scripts/jquery-3.6.0.min.js"></script>
        
    </head>
    
    <body>
        
        <h1>Edit Punches #1</h1>
        
        <form id="punchedit" name="punchedit" method="GET" action="punchedit2.jsp">
            
            <fieldset>
                
                <legend>Edit Punches</legend>
                
                <p>
                    <label for="employeeid">Select Employee:</label>
                    <%= db.getEmployeesAsSelectList(null) %>
                </p>
                
                <p>
                    <label for="payperiod">Select Pay Period:</label>
                    <input type="date" name="payperiod" id="payperiod" value="2018-08-15"></input>
                </p>
                
                <p>
                    <input type="submit" value="Submit">
                </p>
                
            </fieldset>
            
        </form>
        
        <p>Click <a href="<%= request.getContextPath() %>/main/index.jsp">here</a> to return to the main landing page.</p>
        
        <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        
    </body>
    
</html>