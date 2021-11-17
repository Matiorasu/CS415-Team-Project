package edu.jsu.mcis.cs415.teamproject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.jsu.mcis.cs415.tas_fa21_v2.*;
import java.util.ArrayList;

/**
 *
 * @author Tiffany
 */
public class EmployeeServlet extends HttpServlet {


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            // Get Reference to TASDatabase Bean
            TASDatabase db = (TASDatabase)request.getSession().getAttribute("db");
            
            // Get "employeeid" parameter
            int employeeid = Integer.parseInt(request.getParameter("employeeid"));

            // Get Employee 
            Employee employee = db.getEmployee(db.getEmployee(employeeid).getBadge());
            
            // Get Department 
            Department department = db.getDepartment(db.getEmployee(employeeid).getDepartmentid());
            
            // Get Shift
            Shift shift = db.getShift(employee.getBadge());
            
            // Get EmployeePhone list as ArrayList
            ArrayList<EmployeePhone> employeephone = db.getEmployeeContactInformation(employeeid);
            
            // Get latest CLOCKED IN or CLOCKED OUT punch value
            String latestPunch = db.getLatestPunch(employee.getBadge());

            // Create JSON object
            String jsonString = TAS.getEmployeeAsJSON(employee, department, shift, employeephone, latestPunch);
            
            // Send employee information to client 
            out.println(jsonString);
            
        }
        catch (Exception e) { e.printStackTrace(); }
  
    }

    
    
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    
    
    
        /**
     * Handles the HTTP <code>PUT</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    
    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Employee servlet for retrieving employee records, adding new employees, and updating existing employee information. Accepts GET, POST, PUT methods.";
    }// </editor-fold>

}
