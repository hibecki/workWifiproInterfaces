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

public class Site {
	public long id;
	public String name;
	public String address;
	public String area_code;
	public String area_type_name; //+
	public String charge;
	public String comment;
	public String contact_name;
	public String contact_tel_number;
	public String create_date;
	public String create_user_name; //create_user_id
	public String customer_amount;
	public String latitude;
	public String longitude;
	public long organization_id; //
	public int organization_level; 
	public String organization_name; //
	public long project_id;
	public long post_address_id; //
	public String post_address_name; 
	public String post_address_zipcode; 
	public String remark;
	public String rival;
	public String signal_more;
	public String signal_old;
	public String status;
	public String update_date;
	public String update_user_name; //update_user_id
	public String worker_name; //worker_id
	
//`other_providers` tinyblob,

	public String errorCode;
	public String errorMessage;
	private Logger LOG;
	public boolean isFound;
	
	public Site() {
		this.LOG = Logger.getLogger(Site.class.getName());
		isFound = false;
	}
	
	public void getById(int siteid) {
        errorCode = "Site-01";
        errorMessage = "Found unidentified error";
        this.id = siteid;
        
		Connection cntn = null;
		Statement stmt = null;
		ResultSet rsst = null;
		
		try {
			Class.forName(Utils.JdbcDriverName);
			cntn = DriverManager.getConnection(Utils.JdbcConnectionstring);
			stmt = cntn.createStatement();

			String queryString = "select sa.*,at.name area_type_name";
			queryString += ", pa.name post_address_name, pa.zipcode post_address_zipcode";
			queryString += ", u1.username create_user_name, u2.username update_user_name, u3.username worker_name";			
			queryString += ", o.level organization_level, pa.name organization_name";
			queryString += " from area_type at, post_address pa, organization o,";
			queryString += " service_area sa left join user u1 on sa.create_user_id = u1.id";
			queryString += " left join user u2 on sa.update_user_id = u2.id";
			queryString += " left join user u3 on sa.worker_id = u3.id";
			queryString += " where sa.id = " + siteid + " and sa.area_type_id = at.id";
			queryString += " and sa.post_address_id = pa.id";
			queryString += " and sa.organization_id = o.id";
			
			/*
			String queryString = "select sa.*,at.name area_type_name";
			queryString += ", pa.name post_address_name, pa.zipcode post_address_zipcode";
			queryString += ", u1.username create_user_name, u2.username update_user_name, u3.username worker_name";			
			queryString += ", o.level organization_level, pa.name organization_name";
			queryString += " from service_area sa, area_type at, post_address pa, user u1, user u2, user u3, organization o";
			queryString += " where sa.id = " + siteid + " and sa.area_type_id = at.id";
			queryString += " and sa.post_address_id = pa.id";
			queryString += " and s.create_user_id = u1.id";
			queryString += " and s.update_user_id = u2.id";
			queryString += " and s.worker_id = u3.id";
			queryString += " and s.organization_id = o.id";
			*/
			
System.out.println("queryString "+queryString);
			rsst = stmt.executeQuery(queryString);
			if(rsst.next()){
				this.id  = rsst.getLong("id");
				this.name = rsst.getString("name");
				this.address = rsst.getString("address");
				this.area_code = rsst.getString("area_code");
				this.area_type_name = rsst.getString("area_type_name");
				this.charge = rsst.getString("charge");
				this.comment = rsst.getString("comment");
				this.contact_name = rsst.getString("contact_name");
				this.contact_tel_number = rsst.getString("contact_tel_number");
				this.create_date = new SimpleDateFormat(Utils.DateTimeFormat).format(rsst.getTimestamp("create_date"));
				
				this.create_user_name = rsst.getString("create_user_name"); //
				this.customer_amount = rsst.getString("customer_amount");
				this.latitude = rsst.getString("latitude");
				this.longitude = rsst.getString("longitude");
				this.organization_id = rsst.getLong("organization_id"); //
				this.organization_level = rsst.getInt("organization_level");
				this.organization_name = rsst.getString("organization_name");
				
				this.project_id = rsst.getLong("project_id");
				this.post_address_id = rsst.getLong("post_address_id"); //
				this.post_address_name = rsst.getString("post_address_name");
				this.post_address_zipcode = rsst.getString("post_address_zipcode");
				this.remark = rsst.getString("remark");
				this.rival = rsst.getString("rival");
				this.signal_more = rsst.getString("signal_more");
				this.signal_old = rsst.getString("signal_old");
				this.status = rsst.getString("status");
				this.update_date = new SimpleDateFormat(Utils.DateTimeFormat).format(rsst.getTimestamp("update_date"));
				
				this.update_user_name = rsst.getString("update_user_name"); //
				this.worker_name = rsst.getString("worker_name"); //
				isFound = true;
			}
			rsst.close();
			stmt.close();
			cntn.close();
            errorCode = "Site00";
            errorMessage = "No error";
		} catch(SQLException se) {
            errorCode = "Site01";
            errorMessage = se.getErrorCode() + " " + se.getMessage();
		} catch(Exception e) {
            errorCode = "Site02";
            errorMessage = e.getMessage();
		} finally {
			try {
				if (stmt!=null) stmt.close();
			} catch(SQLException se) { 
	            errorCode = "Site03";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
			try {
				if (cntn!=null) cntn.close();
			} catch(SQLException se) { 
	            errorCode = "Site04";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},SiteId: {3}",new Object[]{"Site",errorCode,errorMessage,siteid});
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getJson() {
        errorCode = "Site-11";
        errorMessage = "Found unidentified error";

		StringWriter out = new StringWriter();
		
		JSONObject obj=new JSONObject();
			
		obj.put("id",new Long(this.id));
		if (isFound) {
			obj.put("name",this.name);
			obj.put("address",this.address);
			obj.put("area_code",this.area_code);
			obj.put("area_type_name",this.area_type_name); //+
			obj.put("charge",this.charge);
			obj.put("comment",this.comment);
			obj.put("contact_name",this.contact_name);
			obj.put("contact_tel_number",this.contact_tel_number);
			obj.put("create_date",this.create_date);
			obj.put("create_user_name",this.create_user_name); //
			obj.put("customer_amount",this.customer_amount);
			obj.put("latitude",this.latitude);
			obj.put("longitude",this.longitude);
			//obj.put("organization_id",new Long(this.organization_id)); //
			obj.put("organization_level",new Integer(this.organization_level)); 
			obj.put("organization_name",this.organization_name);
		
			obj.put("project_id",new Long(this.project_id));
			//obj.put("post_address_id",new Long(this.post_address_id)); //
			obj.put("post_address_name",this.post_address_name);
			obj.put("post_address_zipcode",this.post_address_zipcode);
			obj.put("remark",this.remark);
			obj.put("rival",this.rival);
			obj.put("signal_more",this.signal_more);
			obj.put("signal_old",this.signal_old);
			obj.put("status",this.status);
			obj.put("update_date",this.update_date);
			obj.put("update_user_name",this.update_user_name); //
			obj.put("worker_name",this.worker_name); //
		}
		String res = "";
		try {
			obj.writeJSONString(out);
			res = out.toString();
	        errorCode = "Site10";
	        errorMessage = "No error";
		} catch (Exception e) {
	        errorCode = "Site11";
	        errorMessage = e.getMessage();
		} finally {
this.LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},SiteId: {3}",new Object[]{"Site-getJson",errorCode,errorMessage,this.id});
		}
		return res;
	}
}
