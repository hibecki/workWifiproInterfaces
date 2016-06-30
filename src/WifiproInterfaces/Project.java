package WifiproInterfaces;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

public class Project {
	public long id;
	public String name;
	public String errorCode;
	public String errorMessage;
	private Logger LOG;
	public boolean isFound;
	
	public Project() {
		this.LOG = Logger.getLogger(Project.class.getName());
		isFound = false;
	}
	
	public void getById(int projectid) {
        errorCode = "Project-01";
        errorMessage = "Found unidentified error";
        this.id = projectid;
        
		Connection cntn = null;
		Statement stmt = null;
		ResultSet rsst = null;
		
		try {
			Class.forName(Utils.JdbcDriverName);
			cntn = DriverManager.getConnection(Utils.JdbcConnectionstring);
			stmt = cntn.createStatement();
			rsst = stmt.executeQuery("select * from project where id = " + projectid);
			if(rsst.next()){
				this.id  = rsst.getLong("id");
				this.name = rsst.getString("name");
				isFound = true;
			}
			rsst.close();
			stmt.close();
			cntn.close();
            errorCode = "Project00";
            errorMessage = "No error";
		} catch(SQLException se) {
            errorCode = "Project01";
            errorMessage = se.getErrorCode() + " " + se.getMessage();
		} catch(Exception e) {
            errorCode = "Project02";
            errorMessage = e.getMessage();
		} finally {
			try {
				if (stmt!=null) stmt.close();
			} catch(SQLException se) { 
	            errorCode = "Project03";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
			try {
				if (cntn!=null) cntn.close();
			} catch(SQLException se) { 
	            errorCode = "Project04";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},ProjectId: {3}",new Object[]{"Project",errorCode,errorMessage,projectid});
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getJson() {
        errorCode = "Project-11";
        errorMessage = "Found unidentified error";

		StringWriter out = new StringWriter();
		
		JSONObject obj=new JSONObject();
		obj.put("id",new Long(this.id));
		if (isFound) {
			obj.put("name",this.name);			
		}

		String res = "";
		try {
			obj.writeJSONString(out);
			res = out.toString();
	        errorCode = "Project10";
	        errorMessage = "No error";
		} catch (Exception e) {
	        errorCode = "Project11";
	        errorMessage = e.getMessage();
		} finally {
this.LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},ProjectId: {3}",new Object[]{"Project-getJson",errorCode,errorMessage,this.id});
		}
		return res;
	}
}
