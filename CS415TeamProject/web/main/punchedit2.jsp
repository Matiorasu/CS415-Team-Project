<%@page import="java.time.LocalDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    int employeeid = 0;
    LocalDate payperiod;
    String day = "";
    String month = "";
    String year = "";
    int in_hour;
    int in_second;
    int in_minute;
    int out_hour;
    int out_second;
    int out_minute;
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
                    
                <p>
                    <label for="date">Date: YY/MM/DD</label>
                    <input type="text" name="date_year" id="date" value="<%= year%>"> / 
                    <input type="text" name="date_month" id="date "  value="<%= month%>"> /
                    <input type="text" name="date_day" id="date "  value="<%= day%>">
                    <input type="button" value="Use Current Date" onclick="TAS.onClickCurrentDate();">
                </p>    
                    
                    <!-- Input fields for the new clock-in time -->
                    <label for="newinhour">New Clock-In Time: </label>
                    <input type="number" id="newinhour" name="newinhour" min="1" max="12" value="<%=in_hour%>">
                    <label for="newinmin"> : </label>
                    <input type="number" id="newinmin" name="newinmin" min="0" max="59" value="<%=in_minute%>">
                    <label for="newinsec"> : </label>
                    <input type="number" id="newinsec" name="newinsec" min="0" max="59" value="<%=in_second%>">
                    <label for="timeselect"> </label> <!-- may need to generate dynamically -->
                    <select name="timeselect" id="timeselect"> 
                        <option value="am">AM</option>
                        <option value="pm">PM</option>
                    </select>
                    <input type="button" value="Use Current Time" onclick="TAS.onClickCurrentTime();">
                    
                    <br>
                    <!-- Input fields for new clock-out time -->
                    <label for="newouthour">New Clock-Out Time: </label>
                    <input type="number" id="newouthour" name="newouthour" min="1" max="12" value="<%=out_hour%>">
                    <label for="newoutmin"> : </label>
                    <input type="number" id="newoutmin" name="newoutmin" min="0" max="59" value="<%=out_minute%>">
                    <label for="newoutsec"> : </label>
                    <input type="number" id="newoutsec" name="newoutsec" min="0" max="59" value="<%=out_second%>">
                    <label for="timeselect"> </label> <!-- may need to generate dynamically -->
                    <select name="timeselect" id="timeselect"> 
                        <option value="am">AM</option>
                        <option value="pm">PM</option>
                    </select>
                    <input type="button" value="Use Current Time" onclick="TAS.onClickCurrentTime();">
                </p>
                
                <p>
                    <input type="submit" value="DELETE" onClick="TAS.onClickDelete();">
                    <input type="submit" value="CLOCK IN" onsubmit="(e) => TAS.onClockIn(e)">
                    <input type="submit" value="CLOCK OUT" onsubmit="(e) => TAS.onClockOut(e)">
                    
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