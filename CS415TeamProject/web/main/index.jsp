<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="db" class="edu.jsu.mcis.cs415.tas_fa21_v2.TASDatabase" scope="session"/>

<!DOCTYPE html>
<html>
    <head>
        <title>Time and Attendance System</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="scripts/jquery-3.6.0.min.js"></script>
    </head>

    <body>
        <h1>Main Menu</h1>
        <h3>Welcome to the Time and Attendance System!</h3>
        
        <%
            if ( (request.isUserInRole("admin")) || (request.isUserInRole("secretary")) ) {
        %>
        
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
            <a href="<%= request.getContextPath() %>/main/punchedit1.jsp"><b>Punch Editing</b></a><br>
            Use this module to manually clock in or clock out an employee, or to
            view the employee's punch history for the week.
        </p>
        <p>
            <a href="<%= request.getContextPath() %>/main/page2.jsp"><b>Add New Employee</b></a><br>
            Use this module to add a new employee to the system.
        </p>
        <p>
            <a href="<%= request.getContextPath() %>/main/page1.jsp"><b>Print Badge Labels</b></a><br>
            Use this module to print personalized bar code labels for use on employee badges.
        </p>
        <p>
            <a href="<%= request.getContextPath() %>/main/page2.jsp"><b>Employee Information</b></a><br>
            Use this module to view or change the current name, department, employee 
            type, shift class, or active/inactive status of an employee.
        </p>
        
        <%
            }
        %>
        
        <%
            if ( (request.isUserInRole("admin")) || (request.isUserInRole("supervisor")) ) {
        %>
        
        <p>
            <a href="<%= request.getContextPath() %>/main/page1.jsp"><b>Daily Report</b></a><br>
            Use this module to produce a report, categorized by department and employee type, 
            that indicates which employees are present or absent on a given time and date.
        </p>
        <p>
            <a href="<%= request.getContextPath() %>/main/page2.jsp"><b>Process Attendance Reports</b></a><br>
            Use this module to produce weekly attendance reports. These include "Time Cards,"
            which indicate the hours and the percentage of scheduled hours worked by 
            each employee, and the "Hours Summary," which indicates the total number 
            of hours worked by all employees.
        </p>
        <p>
            <a href="<%= request.getContextPath() %>/main/page1.jsp"><b>Weekly Schedule Planner</b></a><br>
            Use this module to add or change employees' weekly work schedules.
        </p>
        
        <%
            }
        %>
        
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
