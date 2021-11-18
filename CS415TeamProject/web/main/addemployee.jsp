<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="db" class="edu.jsu.mcis.cs415.tas_fa21_v2.TASDatabase" scope="session"/>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add New Employee</title>
    </head>
    <body>
        <h1>Add New Employee</h1>
        
        <%
            if ( (request.isUserInRole("admin")) || (request.isUserInRole("secretary")) ) {
        %>
        
        <form id="employeeadd" name="employeeadd" method="POST" action="addemployee2.jsp">
            <fieldset>
                <p>
                    <label for="firstname">First Name: </label>
                    <input type="text" name="firstname" id="firstname" required>
                </p>
                <p>
                    <label for="middlename">Middle Name / Initial: </label>
                    <input type="text" name="middlename" id="middlename" required>
                </p>
                <p>
                    <label for="lastname">Last Name: </label>
                    <input type="text" name="lastname" id="lastname" required>
                </p>
                <p>
                    <label for="homenumber">Home Telephone: </label>
                    <input type="tel" name="homenumber" id="homenumber" placeholder="123-456-7890" pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}">
                </p>
                <p>
                    <legend>Cell Phone: </legend>
                    <label for="cellname">Name: </label>
                    <input type="text" name="cellname" id="cellname">
                    <label for="cellnumber">Number: </label>
                    <input type="tel" name="cellnumber" id="cellnumber" placeholder="123-456-7890" pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}">
                </p>
                <p>
                    <legend>Emergency Contact: </legend>
                    <label for="emergencyname">Name: </label>
                    <input type="text" name="emergencyname" id="emergencyname">
                    <label for="cellnumber">Number: </label>
                    <input type="tel" name="emergencynumber" id="emergencynumber" placeholder="123-456-7890" pattern="[0-9]{3}-[0-9]{3}-[0-9]{4}">
                </p>
                <p>
                    <label for="active">Start Date: </label>
                    <input type="date" name="active" id="active" required>
                </p>
                <p>
                    <label for="employeetypeid">Employee Type: </label>
                    <%= db.getEmployeeTypesAsSelectList() %>
                </p>
                <p>
                    <label for="departmentid">Department: </label>
                    <%= db.getDepartmentsAsSelectList() %>
                </p>
                <p>
                    <label for="shiftid">Shift: </label>
                    <%= db.getShiftsAsSelectList() %>
                </p>
                <p>
                    <input type="submit" value="Submit">
                    <input type="reset" value="Reset">
                </p>
            </fieldset>
        </form>
        <br>
        
        <%
            }
        %>
        
        <p>Click <a href="<%= request.getContextPath() %>/main/index.jsp">here</a> to return to the main landing page.</p>
        
        <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        
    </body>
</html>
