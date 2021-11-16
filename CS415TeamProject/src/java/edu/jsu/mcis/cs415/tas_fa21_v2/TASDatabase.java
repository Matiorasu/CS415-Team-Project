package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.Timestamp;

/**
 *
 * @author JSU
 * 
 */
public class TASDatabase {
    
    private Context envContext, initContext;
    private DataSource ds;
    private Connection conn;
    
    /* CONSTRUCTORS */
    
    // <editor-fold defaultstate="collapsed" desc="Constructor (for JDBC Pool "cs415_teamproject_dbpool"): Click on the + sign on the left to edit the code.">
    
    public TASDatabase() {
        
        try {
            
            envContext = new InitialContext();
            initContext  = (Context)envContext.lookup("java:/comp/env");
            ds = (DataSource)initContext.lookup("jdbc/cs415_teamproject_dbpool");
            conn = ds.getConnection();
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor (for Direct JDBC Connection): Click on the + sign on the left to edit the code.">
    
    public TASDatabase(String address, String username, String password) {
        
        this.openConnection(address, username, password);
        
    } // </editor-fold>
    
    /* GET METHODS */
    
    // <editor-fold defaultstate="collapsed" desc="getAbsenteeism(): Click on the + sign on the left to edit the code.">
    /**
     * Returns an {@link Absenteeism} object representing the employee's absenteeism history for the specified pay period.
     * @param badge The employee badge
     * @param payperiod The specified pay period
     * @return The {@link Absenteeism} object
     * @see Absenteeism
     */
    public Absenteeism getAbsenteeism(Badge badge, LocalDate payperiod) {
        
        Absenteeism a = null;
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        
        try {
        
            String query = "SELECT * FROM absenteeism WHERE badgeid = ? AND payperiod = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, badge.getId());
            pstmt.setDate(2, java.sql.Date.valueOf(payperiod.with(fieldUS, Calendar.SUNDAY)));
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    double percentage = resultset.getDouble("percentage");
                    a = new Absenteeism(badge, payperiod, percentage);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return a;
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getBadge(): Click on the + sign on the left to edit the code.">
    /**
     * Returns a {@link Badge} object representing the employee's badge information (currently a badge ID and the user's formatted name).
     * @param id The employee's badge ID
     * @return The {@link Badge} object
     * @see Badge
     */
    public Badge getBadge(String id) {
        
        Badge badge = null;
        
        try {
        
            String query = "SELECT * FROM badge WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    String description = resultset.getString("description");
                    badge = new Badge(id, description);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return badge;
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getDailyPunchList(): Click on the + sign on the left to edit the code.">
    /**
     * Returns an {@link java.util.ArrayList} of {@link Punch} objects representing the
     * punches logged by the employee during the specified pay period.  The
     * punches in the list are adjusted according to the specified shift rule
     * set.
     * @param badge The employee's badge
     * @param date A date which specifies the pay period
     * @param shift The specified shift rule set (for adjusting the punches)
     * @return An {@link ArrayList} containing adjusted {@link Punch} objects
     * @see Punch
     * @see Shift
     */
    public ArrayList<Punch> getDailyPunchList(Badge badge, LocalDate date, Shift shift) {
        
        ArrayList<Punch> dailyPunchList = null;
        
        try {
        
            String query = "SELECT * FROM punch WHERE badgeid = ? AND DATE(originaltimestamp) = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, badge.getId());
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                dailyPunchList = new ArrayList<>();
                
                ResultSet resultset = pstmt.getResultSet();
                
                while (resultset.next()) {
                    
                    int id = resultset.getInt("id");
                    int terminalid = resultset.getInt("terminalid");
                    LocalDateTime originaltimestamp = resultset.getTimestamp("originaltimestamp").toLocalDateTime();
                    int punchtypeid = resultset.getInt("punchtypeid");
                    
                    Punch punch = new Punch(id, terminalid, badge, originaltimestamp, punchtypeid);
                    
                    punch.adjust(shift);
                    
                    dailyPunchList.add(punch);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return dailyPunchList;
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getDepartment(): Click on the + sign on the left to edit the code.">
    /**
     * Returns an {@link Employee} object representing the employee's record.
     * @param id The employee's unique ID
     * @return The {@link Employee} object
     * @see Employee
     */
    public Department getDepartment(int id) {
        
        Department department = null;
        
        try {
        
            String query = "SELECT * FROM department WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    String description = resultset.getString("description");
                    int terminalid = resultset.getInt("terminalid");
                    department = new Department(id, description, terminalid);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return department;
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getEmployee(): Click on the + sign on the left to edit the code.">
        
    public Employee getEmployee(Badge badge) {
        
        Employee employee = null;
        
        try {
        
            String query = "SELECT employee.*, employeetype.description AS employeetypedescription FROM employee JOIN employeetype ON employee.employeetypeid = employeetype.id WHERE badgeid = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, badge.getId());
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    HashMap<String, Object> p = new HashMap<>();
                    
                    p.put("id", resultset.getInt("id"));
                    p.put("departmentid", resultset.getInt("departmentid"));
                    p.put("shiftid", resultset.getInt("shiftid"));
                    p.put("employeetypeid", resultset.getInt("employeetypeid"));

                    p.put("firstname", resultset.getString("firstname"));
                    p.put("middlename", resultset.getString("middlename"));
                    p.put("lastname", resultset.getString("lastname"));

                    p.put("badge", badge);
                    
                    p.put("active", resultset.getDate("active") == null ? null : resultset.getDate("active").toLocalDate());
                    p.put("inactive", resultset.getDate("inactive") == null ? null : resultset.getDate("inactive").toLocalDate());
                    
                    employee = new Employee(p);
                    
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return employee;
        
    }
    
    public Employee getEmployee(int id) {
        
        Employee employee = null;
        
        try {
        
            String query = "SELECT employee.badgeid FROM employee WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    String badgeid = resultset.getString("badgeid");
                    
                    employee = getEmployee(getBadge(badgeid));
                    
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return employee;
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getPayPeriodPunchList(): Click on the + sign on the left to edit the code.">
    
    public ArrayList<Punch> getPayPeriodPunchList(int employeeid, LocalDate payperiod) {
        
        Employee employee = getEmployee(employeeid);
        Badge badge = employee.getBadge();
        Shift shift = getShift(badge);
        
        ArrayList<Punch> punchlist = getPayPeriodPunchList(badge, payperiod, shift);

        System.err.println("Punch List Size: " + punchlist.size());
        
        return punchlist;
        
    }
    
    public ArrayList<Punch> getPayPeriodPunchList(Badge badge, LocalDate payperiod, Shift shift) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        
        ArrayList<Punch> payPeriodPunchList = null;
        
        try {
        
            String query = "SELECT * FROM punch WHERE badgeid = ? AND DATE(originaltimestamp) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) ORDER BY originaltimestamp";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, badge.getId());
            pstmt.setString(2, dtf.format(payperiod.with(fieldUS, Calendar.SUNDAY)));
            pstmt.setString(3, dtf.format(payperiod.with(fieldUS, Calendar.SATURDAY)));
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                payPeriodPunchList = new ArrayList<>();
                
                ResultSet resultset = pstmt.getResultSet();
                
                while (resultset.next()) {
                    
                    int id = resultset.getInt("id");
                    int terminalid = resultset.getInt("terminalid");
                    LocalDateTime originaltimestamp = resultset.getTimestamp("originaltimestamp").toLocalDateTime();
                    int punchtypeid = resultset.getInt("punchtypeid");
                    
                    Punch punch = new Punch(id, terminalid, badge, originaltimestamp, punchtypeid);
                    
                    punch.adjust(shift);
                    
                    payPeriodPunchList.add(punch);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return payPeriodPunchList;
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getPunch(): Click on the + sign on the left to edit the code.">
    /**
     * Returns a {@link Punch} object representing an individual employee punch.
     * @param id The unique ID of the punch
     * @return The {@link Punch} object
     * @see Punch
     */
    public Punch getPunch(int id) {
        
        Punch punch = null;
        
        try {
        
            String query = "SELECT * FROM punch WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    int terminalid = resultset.getInt("terminalid");
                    String badgeid = resultset.getString("badgeid");
                    LocalDateTime originaltimestamp = resultset.getTimestamp("originaltimestamp").toLocalDateTime();
                    int punchtypeid = resultset.getInt("punchtypeid");
                    
                    Badge badge = getBadge(badgeid);
                    
                    punch = new Punch(id, terminalid, badge, originaltimestamp, punchtypeid);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return punch;
        
    } // </editor-fold>
    
        // <editor-fold defaultstate="collapsed" desc="getLatestPunch(): Click on the + sign on the left to edit the code.">
    /**
     * Returns a String representing an individual employee punch for the current date and time.
     * @param badgeid The unique badgeid of the employee
     * @return The String object
     */
    public String getLatestPunch(Badge badgeid) {
        
        String latestPunch = null;

        try {
            
            String query = "SELECT * FROM punch WHERE badgeid = ? ORDER BY originaltimestamp";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, badgeid.getId());
            //pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().withNano(0)));
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                resultset.last(); //move to last row of resultset
                
                //if (resultset.next()) {
                    
                    int punchtypeid = resultset.getInt("punchtypeid");
                    
                    if (punchtypeid == 1){
                        latestPunch = "CLOCKED IN";
                    }
                    else {
                        latestPunch = "CLOCKED OUT";
                    }
                //}
            } 
        }
        catch (Exception e) { e.printStackTrace(); }

        return latestPunch;
           
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getShift(): Click on the + sign on the left to edit the code.">
    /**
     * Returns a {@link Shift} object representing an individual shift rule set.
     * (Only the default shift schedule is included.)
     * @param id The unique ID of the shift rule set
     * @return The {@link Shift} object
     * @see Shift
     */
    public Shift getShift(int id) {
        
        Shift s = null;
        
        try {
            
            String query = "SELECT description, dailyschedule.`interval`, dailyschedule.graceperiod, dailyschedule.dock, dailyschedule.lunchdeduct, dailyschedule.`start` AS `start`, dailyschedule.`stop` AS `stop`, dailyschedule.lunchstart AS lunchstart, dailyschedule.lunchstop AS lunchstop FROM (shift JOIN dailyschedule ON shift.dailyscheduleid = dailyschedule.id) WHERE shift.id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    HashMap<String, Object> p = new HashMap<>();
                    
                    p.put("id", id);

                    p.put("description", resultset.getString("description"));
                    p.put("interval", resultset.getInt("interval"));
                    p.put("graceperiod", resultset.getInt("graceperiod"));
                    p.put("dock", resultset.getInt("dock"));
                    p.put("lunchdeduct", resultset.getInt("lunchdeduct"));

                    p.put("start", resultset.getTime("start").toLocalTime());
                    p.put("stop", resultset.getTime("stop").toLocalTime());
                    p.put("lunchstart", resultset.getTime("lunchstart").toLocalTime());
                    p.put("lunchstop", resultset.getTime("lunchstop").toLocalTime());
                    
                    p.put("defaultschedule", new DailySchedule(p));
                    
                    s = new Shift(p);

                }

            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return s;
        
    }
    
    /**
     * Returns a {@link Shift} object representing an individual shift rule set.
     * (Only the default shift schedule is included.)
     * @param badge An employee badge
     * @return The {@link Shift} object representing the rule set for the
     * employee's assigned shift
     * @see Shift
     */
    public Shift getShift(Badge badge) {
        
        Shift shift = null;
        
        try {
        
            String query = "SELECT shiftid FROM employee WHERE badgeid = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, badge.getId());
            
            boolean hasresults = pstmt.execute();
            
            if ( hasresults ) {
                
                ResultSet resultset = pstmt.getResultSet();
                
                if (resultset.next()) {
                    
                    int id = resultset.getInt("shiftid");
                    shift = getShift(id);
                   
                }
                
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return shift;
        
    }
    
    /**
     * Returns a {@link Shift} object representing an individual shift rule set,
     * including any temporary or recurring schedule overrides for the specified
     * date.  Any overrides will replace the default shift schedule and are
     * applied in the following order: recurring overrides for all employees,
     * recurring overrides for the specified employee, temporary overrides for
     * all employees, and temporary overrides for the specified employee.
     * @param badge An employee badge
     * @param date The date which specifies the overrides that should be added
     * to the shift rule set.
     * @return The {@link Shift} object representing the rule set for the
     * employee's assigned shift, with overrides added
     * @see Shift
     * @see DailySchedule
     */
    public Shift getShift(Badge badge, LocalDate date) {
        
        Shift s = getShift(badge);
        
        if (s != null) {
            
            try {
            
                ArrayList<ResultSet> results = new ArrayList<>();
                
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                /* Get Recurring Overrides, All Employees */

                String query = "SELECT d.*, s.`day` FROM scheduleoverride s JOIN dailyschedule d ON s.dailyscheduleid = d.id WHERE s.`start` <= CAST(? AS DATE) AND s.`end` IS NULL AND s.badgeid IS NULL ORDER BY s.id";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, dtf.format(date));
                
                boolean hasresults = pstmt.execute();

                if ( hasresults ) {
                    results.add(pstmt.getResultSet());
                }
                
                /* Get Recurring Overrides, Current Employee */

                query = "SELECT d.*, s.`day` FROM scheduleoverride s JOIN dailyschedule d ON s.dailyscheduleid = d.id WHERE s.`start` <= CAST(? AS DATE) AND s.`end` IS NULL AND s.badgeid = ? ORDER BY s.id";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, dtf.format(date));
                pstmt.setString(2, badge.getId());
                
                hasresults = pstmt.execute();

                if ( hasresults ) {
                    results.add(pstmt.getResultSet());
                }
                
                /* Get Temporary Overrides, All Employees */

                query = "SELECT d.*, s.`day` FROM scheduleoverride s JOIN dailyschedule d ON s.dailyscheduleid = d.id WHERE s.`start` <= CAST(? AS DATE) AND s.`end` >= CAST(? AS DATE) AND s.badgeid IS NULL ORDER BY s.id";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, dtf.format(date));
                pstmt.setString(2, dtf.format(date));
                
                hasresults = pstmt.execute();

                if ( hasresults ) {
                    results.add(pstmt.getResultSet());
                }
                
                /* Get Temporary Overrides, Current Employee */

                query = "SELECT d.*, s.`day` FROM scheduleoverride s JOIN dailyschedule d ON s.dailyscheduleid = d.id WHERE s.`start` <= CAST(? AS DATE) AND s.`end` >= CAST(? AS DATE) AND s.badgeid = ? ORDER BY s.id";
                pstmt = conn.prepareStatement(query);
                pstmt.setString(1, dtf.format(date));
                pstmt.setString(2, dtf.format(date));
                pstmt.setString(3, badge.getId());
                
                hasresults = pstmt.execute();

                if ( hasresults ) {
                    results.add(pstmt.getResultSet());
                }
                
                /* Layer Schedule Overrides by Priority */
                
                for (ResultSet resultset : results) {
                    
                    while (resultset.next()) {
                        
                        HashMap<String, Object> p = new HashMap<>();
                        
                        p.put("interval", resultset.getInt("interval"));
                        p.put("graceperiod", resultset.getInt("graceperiod"));
                        p.put("dock", resultset.getInt("dock"));
                        p.put("lunchdeduct", resultset.getInt("lunchdeduct"));

                        p.put("start", resultset.getTime("start").toLocalTime());
                        p.put("stop", resultset.getTime("stop").toLocalTime());
                        p.put("lunchstart", resultset.getTime("lunchstart").toLocalTime());
                        p.put("lunchstop", resultset.getTime("lunchstop").toLocalTime());
                        
                        DailySchedule d = new DailySchedule(p);
                        
                        int day = resultset.getInt("day");
                        
                        s.setDailySchedule(day, d);
                        
                    }
                    
                }
                
            }
            catch (Exception e) { e.printStackTrace(); }
            
        }
        
        return s;
        
    } // </editor-fold>
    
    public ArrayList<EmployeePhone> getEmployeeContactInformation(int employeeid){

        ArrayList<EmployeePhone> contactinformation = null; 
        HashMap<String, String> p = null;
        
                try{
                    
                   String sql = "SELECT employeephone.*, employeephonetype.description FROM employeephone JOIN employeephonetype ON employeephone.employeephonetypeid = employeephonetype.id WHERE employeeid = ?";
                   PreparedStatement pstatement = this.conn.prepareStatement(sql);
                   pstatement.setInt(1, employeeid);
                   
                   boolean hasresults = pstatement.execute();
            
                    if ( hasresults ) {
                
                        contactinformation = new ArrayList<>();
                
                        ResultSet resultset = pstatement.getResultSet();
                        
                        while (resultset.next()) {
                            
                            p = new HashMap<>();
                    
                            String id = String.valueOf(resultset.getInt("id"));
                            String employeephonetypeid = String.valueOf(resultset.getInt("employeephonetypeid"));
                            String name = resultset.getString("name");
                            String number = resultset.getString("number");
                            String description = resultset.getString("description");
                            
                            p.put("id", id);
                            p.put("employeephonetypeid", employeephonetypeid);
                            p.put("employeeid", String.valueOf(employeeid));
                            p.put("name", name);
                            p.put("number", number);
                            p.put("description", description);
                            
                            EmployeePhone employeephone = new EmployeePhone(p);

                            contactinformation.add(employeephone);
                        }
                    }
                }
                catch(SQLException e){ e.printStackTrace(); }
                
                return contactinformation;
        
    }
    
    /* INSERT METHODS */

    // <editor-fold defaultstate="collapsed" desc="insertAbsenteeism(): Click on the + sign on the left to edit the code.">
    /**
     * Adds an absenteeism record for the specified employee and pay period.
     * @param absenteeism An {@link Absenteeism} object representing the
     * employee's absenteeism record.
     * @return A Boolean value indicating whether the record was successfully
     * added.
     * @see Absenteeism
     */
    public boolean insertAbsenteeism(Absenteeism absenteeism) {
        
        int result = 0;
        
        try {
            
            String query = "DELETE FROM absenteeism WHERE badgeid = ? AND payperiod = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, absenteeism.getBadge().getId());
            pstmt.setDate(2, java.sql.Date.valueOf(absenteeism.getPayperiod()));
            pstmt.executeUpdate();
        
            query = "INSERT INTO absenteeism (badgeid, payperiod, percentage) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, absenteeism.getBadge().getId());
            pstmt.setDate(2, java.sql.Date.valueOf(absenteeism.getPayperiod()));
            pstmt.setDouble(3, absenteeism.getPercentage());
            
            result = pstmt.executeUpdate();
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return (result == 1);
        
    } // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="insertPunch(): Click on the + sign on the left to edit the code.">
    /**
     * Adds a punch to the database.
     * @param punch A {@link Punch} object representing the new punch record
     * @return An integer containing the database ID assigned to the new punch
     * record
     * @see Punch
     */
    public int insertPunch(Punch punch) {
        
        int id = 0;
        
        try {
        
            String query = "INSERT INTO punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, punch.getTerminalid());
            pstmt.setString(2, punch.getBadge().getId());
            pstmt.setTimestamp(3, Timestamp.valueOf(punch.getOriginaltimestamp()));
            pstmt.setInt(4, punch.getPunchtype().ordinal());
            
            int result = pstmt.executeUpdate();
            if (result == 1) {
               ResultSet keys = pstmt.getGeneratedKeys();
               if (keys.next()) {
                   id = keys.getInt(1);
               }
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return id;
        
    } // </editor-fold>
    
    /* DELETE METHODS */
    
    public boolean deletePunch(int id) {
        
        int rows = 0;
        
        try {
            
            String query = "DELETE FROM punch WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rows = pstmt.executeUpdate();
            
        }
        catch (Exception e) { e.printStackTrace(); }

        return (rows == 1);
        
    }
    
    /* HTML METHODS */
    
    /**
     * Returns a list of employees in specified department(s) as an HTML
     * SELECT list.
     * @param departments A list of {@link Department} objects, indicating
     * which departments' employees should be included in the list.  Leave this
     * argument null to get all employees.
     * @return An HTML string containing the SELECT element, ready to be
     * embedded as a dynamic element in a JSP page.
     * @see Department
     */
    public String getEmployeesAsSelectList(ArrayList<Department> departments) {
        
        StringBuilder s = new StringBuilder();
        
        try {
            
            String query;
            PreparedStatement pstatement;
            boolean hasresults = false;
            
            if ( departments == null || (departments.isEmpty()) ) {
                
                query = "SELECT employee.id, employee.departmentid, badge.id AS badgeid, department.description AS departmentdescription, badge.description FROM ((employee JOIN badge ON employee.badgeid = badge.id) JOIN department ON employee.departmentid = department.id) ORDER BY lastname";
                pstatement = conn.prepareStatement(query);
                hasresults = pstatement.execute();
                
            }
            else {
                
                StringBuilder departmentidlist = new StringBuilder();
                
                for (int i = 0; i < departments.size(); ++i) {
                    if (i > 0)
                        departmentidlist.append(", ");
                    departmentidlist.append('?');
                }
                
                query = "SELECT employee.id, employee.departmentid, badge.id AS badgeid, department.description AS departmentdescription, badge.description FROM ((employee JOIN badge ON employee.badgeid = badge.id) JOIN department ON employee.departmentid = department.id) WHERE (departmentid IN (" + departmentidlist.toString() + ")) ORDER BY lastname";
                
                System.out.println(query);
                
                pstatement = conn.prepareStatement(query);
                
                for (int i = 0; i < departments.size(); ++i) {
                    
                    pstatement.setInt(i + 1, departments.get(i).getId());
                    System.out.println("Param: " + (i+1) + ": " + departments.get(i).getId());
                }
                
                hasresults = pstatement.execute();
                
            }
            
            if (hasresults) {
                
                ResultSet resultset = pstatement.getResultSet();
                
                s.append("<select name=\"employeeid\" size=\"1\" id=\"employeeid\">\n");
                s.append("<option selected value=\"0\">(please select an employee)</option>\n");
                
                while (resultset.next()) {
                    
                    s.append("<option value=\"");
                    s.append(resultset.getInt("id"));
                    s.append("\">");
                    s.append(resultset.getString("description")).append(" (");
                    s.append(resultset.getString("departmentdescription"));
                    s.append(") (#").append(resultset.getString("badgeid")).append(")");
                    s.append("</option>\n");
                    
                }
                
                s.append("</select>\n");
                
            }            
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return s.toString();
        
    }
    
    /* PRIVATE METHODS */
    
    // <editor-fold defaultstate="collapsed" desc="openConnection(): Click on the + sign on the left to edit the code.">
    
    private boolean openConnection(String address, String username, String password) {
        
        boolean result = false;
        
        try {
            
            String url = "jdbc:mysql://" + address + "/tas_fa21_v2?autoReconnect=true&useSSL=false&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=America/Chicago";
            
            conn = DriverManager.getConnection(url, username, password);
            
            result = !(conn.isClosed());
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return result;
        
    } // </editor-fold>
    
}