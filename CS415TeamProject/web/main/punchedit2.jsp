<%@page import="java.time.LocalDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    int employeeid = 0;
    LocalDate payperiod;
%>
<jsp:useBean id="db" class="edu.jsu.mcis.cs415.tas_fa21_v2.TASDatabase" scope="session"/>

<!DOCTYPE html>
<html>
    
    <head>
        
        <title>Edit Punches</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="scripts/jquery-3.6.0.min.js"></script>
        <script src="scripts/TAS.js"></script>
        
    </head>
    
    <body>
        
        <%
            employeeid = Integer.parseInt(request.getParameter("employeeid"));
            payperiod = LocalDate.parse(request.getParameter("payperiod"));
        %>

        <h1>Edit Punches #2</h1>
        
        <form id="punchedit" name="punchedit" method="GET" action="punchedit2.jsp">
            
            <fieldset>
                
                <legend>Edit Punches</legend>
                
                <input type="hidden" name="employeeid" id="employeeid" value="<%= employeeid %>">
                <input type="hidden" name="payperiod" id="payperiod" value="<%= payperiod %>">
                
                <div id="punchtable_target"></div>
                <div>Total Minutes: <span id="totalminutes_target"></span></div>
                <div>Total Absenteeism: <span id="totalabsenteeism_target"></span></div>
                
                <p>
                    <label for="deletepunchid">Selected Punch: </label>
                    <input type="text" name="deletepunchid" id="deletepunchid">
                </p>
                
                <p>
                    <input type="button" value="DELETE" onClick="TAS.onClickDelete();">
                </p>
                
            </fieldset>
            
        </form>
        
        <p>Click <a href="<%= request.getContextPath() %>/main/index.jsp">here</a> to return to the main landing page.</p>
        
        <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        
        <script type="text/javascript">
            
            $(TAS.init());
            
        </script>
        
    </body>
    
</html>