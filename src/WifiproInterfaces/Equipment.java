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

public class Equipment {
	public long id;
	public String host_name; 
	public String ip;
	public String location_id;
	public String latitude;
	public String longitude;
	public String status;
	public String equipment_id;
	public long service_area_equipment_id; //
	public long site_circuit_id; //
	
	public String errorCode;
	public String errorMessage;
	private Logger LOG;
	public boolean isFound;
	
	public Equipment() {
		this.LOG = Logger.getLogger(Equipment.class.getName());
		isFound = false;
	}
	
	public void getById(int equipmentid) {
        errorCode = "Equipment-01";
        errorMessage = "Found unidentified error";
        this.id = equipmentid;
        
		Connection cntn = null;
		Statement stmt = null;
		ResultSet rsst = null;
		
		try {
			Class.forName(Utils.JdbcDriverName);
			cntn = DriverManager.getConnection(Utils.JdbcConnectionstring);
			stmt = cntn.createStatement();

			//String queryString = "select se.*";
			//queryString += " from site_equipment se";
			//queryString += " where se.id = " + equipmentid;
			
			String queryString = "select se.*, sae.item_id, sae.service_area_id, i.mac_address, i.serial_no, sc.site_id";
			queryString += " from item i, site_circuit sc, service_area_equipment sae, site_equipment se";
			queryString += " where se.service_area_equipment_id = sae.id and se.site_circuit_id = sc.id and sae.item_id = i.id";
			queryString += " and se.id = " + equipmentid;
		
			//select se.*, sae.item_id, sae.service_area_id, i.mac_address, i.serial_no, sc.site_id from item i, site_circuit sc, service_area_equipment sae, site_equipment se where se.service_area_equipment_id = sae.id and se.site_circuit_id = sc.id and sae.item_id = i.id and se.id = 1192;
			
			// service_area_equipment_id = 427 in service_area_equipment get service_area = 413
			//select * from site_equipment where id = 1192;
			// select * from service_area_equipment where id = 426;
			
			// select * from site where service_area_id = 413;
			// How site table find equipment???
			
			
System.out.println("queryString "+queryString);
			rsst = stmt.executeQuery(queryString);
			if(rsst.next()){
				this.id  = rsst.getLong("id");
				this.host_name = rsst.getString("host_name");
				this.ip = rsst.getString("ip");
				this.latitude = rsst.getString("latitude");
				this.longitude = rsst.getString("longitude");
				this.location_id = rsst.getString("site_id");
				this.status = rsst.getString("status");
				this.equipment_id = rsst.getString("equipment_id");
				this.service_area_equipment_id = rsst.getLong("service_area_equipment_id");
				this.site_circuit_id = rsst.getLong("site_circuit_id");
				isFound = true;
			}
			rsst.close();
			stmt.close();
			cntn.close();
            errorCode = "Equipment00";
            errorMessage = "No error";
		} catch(SQLException se) {
            errorCode = "Equipment01";
            errorMessage = se.getErrorCode() + " " + se.getMessage();
		} catch(Exception e) {
            errorCode = "Equipment02";
            errorMessage = e.getMessage();
		} finally {
			try {
				if (stmt!=null) stmt.close();
			} catch(SQLException se) { 
	            errorCode = "Equipment03";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
			try {
				if (cntn!=null) cntn.close();
			} catch(SQLException se) { 
	            errorCode = "Equipment04";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},EquipmentId: {3}",new Object[]{"Equipment",errorCode,errorMessage,equipmentid});
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getJson() {
        errorCode = "Equipment-11";
        errorMessage = "Found unidentified error";

		StringWriter out = new StringWriter();
		
		JSONObject obj=new JSONObject();
		
		obj.put("id",new Long(this.id));
		if (isFound) {
			obj.put("host_name",this.host_name);
			obj.put("ip",this.ip);
			obj.put("latitude",this.latitude);
			obj.put("longitude",this.longitude);
			obj.put("location_id",this.location_id);
			obj.put("equipment_id",this.equipment_id);
			obj.put("status",this.status);
			//obj.put("service_area_equipment_id",new Long(this.service_area_equipment_id));
			//obj.put("site_circuit_id",new Long(this.site_circuit_id));
		}
		
		String res = "";
		try {
			obj.writeJSONString(out);
			res = out.toString();
	        errorCode = "Equipment10";
	        errorMessage = "No error";
		} catch (Exception e) {
	        errorCode = "Equipment11";
	        errorMessage = e.getMessage();
		} finally {
this.LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},EquipmentId: {3}",new Object[]{"Equipment-getJson",errorCode,errorMessage,this.id});
		}
		return res;
	}
}
