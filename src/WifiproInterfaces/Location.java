package WifiproInterfaces;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

public class Location {
	public long id;
	public String finish_date; //
	public String finish_user_name; // finish_user_id
	public String install_worker_name; //install_worker_id
	public String latitude;
	public String longitude;
	public String location_name;
	public int number_of_access_point;
	public int number_of_modem_router;
	public int number_of_switch;
	public String remark;
	public long site_id; //service_area_id;
	public String start_date;
	public String start_user_name; //start_user_id
	public String status;
	public String update_date; //
	public String update_user_name; //update_user_id
	
	public String errorCode;
	public String errorMessage;
	private Logger LOG;
	public boolean isFound;
	
	public Location() {
		this.LOG = Logger.getLogger(Location.class.getName());
		isFound = false;
	}
	
	public void getById(int locationid) {
        errorCode = "Location-01";
        errorMessage = "Found unidentified error";
        this.id = locationid;
        
		Connection cntn = null;
		Statement stmt = null;
		ResultSet rsst = null;
		
		try {
			Class.forName(Utils.JdbcDriverName);
			cntn = DriverManager.getConnection(Utils.JdbcConnectionstring);
			stmt = cntn.createStatement();

			String queryString = "select s.*, u1.username finish_user_name, u2.username install_worker_name, u3.username start_user_name, u4.username update_user_name";
			queryString += " from site s left join user u1 on s.finish_user_id = u1.id";
			queryString += " left join user u2 on s.install_worker_id = u2.id";
			queryString += " left join user u3 on s.start_user_id = u3.id";
			queryString += " left join user u4 on s.update_user_id = u4.id";
			queryString += " where s.id = " + locationid;
			
System.out.println("queryString "+queryString);
			rsst = stmt.executeQuery(queryString);
			if(rsst.next()){
				this.id  = rsst.getLong("id");
				this.finish_date = new SimpleDateFormat(Utils.DateTimeFormat).format(rsst.getTimestamp("finish_date"));
				this.finish_user_name = rsst.getString("finish_user_name");
				this.install_worker_name = rsst.getString("install_worker_name");
				this.latitude = rsst.getString("latitude");
				this.longitude = rsst.getString("longitude");
				this.location_name = rsst.getString("location_name");
				this.number_of_access_point = rsst.getInt("number_of_access_point");
				this.number_of_modem_router = rsst.getInt("number_of_modem_router");
				this.number_of_switch = rsst.getInt("number_of_switch");
				this.remark = rsst.getString("remark");
				this.site_id = rsst.getLong("service_area_id");
				this.start_date = new SimpleDateFormat(Utils.DateTimeFormat).format(rsst.getTimestamp("start_date"));
				this.start_user_name = rsst.getString("start_user_name");
				this.status = rsst.getString("status");
				this.update_date = new SimpleDateFormat(Utils.DateTimeFormat).format(rsst.getTimestamp("update_date"));
				this.update_user_name = rsst.getString("update_user_name");
				isFound = true;
			}
			rsst.close();
			stmt.close();
			cntn.close();
            errorCode = "Location00";
            errorMessage = "No error";
		} catch(SQLException se) {
            errorCode = "Location01";
            errorMessage = se.getErrorCode() + " " + se.getMessage();
		} catch(Exception e) {
            errorCode = "Location02";
            errorMessage = e.getMessage();
		} finally {
			try {
				if (stmt!=null) stmt.close();
			} catch(SQLException se) { 
	            errorCode = "Location03";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
			try {
				if (cntn!=null) cntn.close();
			} catch(SQLException se) { 
	            errorCode = "Location04";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},LocationId: {3}",new Object[]{"Location",errorCode,errorMessage,locationid});
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getJson() {
        errorCode = "Location-11";
        errorMessage = "Found unidentified error";

		StringWriter out = new StringWriter();
		
		JSONObject obj=new JSONObject();
			
		obj.put("id",new Long(this.id));
		if (isFound) {
			obj.put("finish_date",this.finish_date);
			obj.put("finish_user_name",this.finish_user_name);
			obj.put("install_worker_name",this.install_worker_name);
			obj.put("latitude",this.latitude);
			obj.put("longitude",this.longitude);
			obj.put("location_name",this.location_name);
			obj.put("number_of_access_point",new Integer(this.number_of_access_point));
			obj.put("number_of_modem_router",new Integer(this.number_of_modem_router));
			obj.put("number_of_switch",new Integer(this.number_of_switch));
			obj.put("remark",this.remark);
			obj.put("site_id",new Long(this.site_id));
			obj.put("start_date",this.start_date);
			obj.put("start_user_name",this.start_user_name);
			obj.put("status",this.status);
			obj.put("update_date",this.update_date);
			obj.put("update_user_name",this.update_user_name);
		}
		String res = "";
		try {
			obj.writeJSONString(out);
			res = out.toString();
	        errorCode = "Location10";
	        errorMessage = "No error";
		} catch (Exception e) {
	        errorCode = "Location11";
	        errorMessage = e.getMessage();
		} finally {
this.LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},LocationId: {3}",new Object[]{"Location-getJson",errorCode,errorMessage,this.id});
		}
		return res;
	}
}
