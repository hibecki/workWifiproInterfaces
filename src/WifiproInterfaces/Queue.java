package WifiproInterfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//--status = Queued, Picked, Succeed, Notified1, Notified2, Notified3, .., Failed


public class Queue {
	public int id;
	public String errorCode;
	public String errorMessage;
	private Logger LOG;
	
	public Queue() {
		this.LOG = Logger.getLogger(Queue.class.getName());
	}
	
	public void setQueuedById(int id) { update(id,"Queued",""); }
	public void setPickedById(int id) { update(id,"Picked",""); }
	public void setSucceedById(int id,String result) { update(id,"Succeed",result); }
	public void setNotified1ById(int id,String result) { update(id,"Notified1",result); }
	public void setNotified2ById(int id,String result) { update(id,"Notified2",result); }
	public void setNotified3ById(int id,String result) { update(id,"Notified3",result); }
	public void setFailedById(int id,String result) { update(id,"Failed",result); }
	
	private void update(int id, String status, String result) {
        errorCode = "Queue-01";
        errorMessage = "Found unidentified error";
        
		Connection cntn = null;
		PreparedStatement pstmt = null;
//System.out.println(id + " " + status + " " + result);
		try {
			Class.forName(Utils.JdbcDriverName);
			cntn = DriverManager.getConnection(Utils.JdbcConnectionstring);
			pstmt = cntn.prepareStatement("update nmsnotify set status = ?, result = ? where id = ?");
			
			pstmt.setString(1, status);
			pstmt.setString(2, result);
			pstmt.setInt(3, id);
			pstmt.executeUpdate();
			
			pstmt.close();
			cntn.close();
            errorCode = "Queue00";
            errorMessage = "No error";
		} catch(SQLException se) {
            errorCode = "Queue01";
            errorMessage = se.getErrorCode() + " " + se.getMessage();
		} catch(Exception e) {
            errorCode = "Queue02";
            errorMessage = e.getMessage();
		} finally {
			try {
				if (pstmt!=null) pstmt.close();
			} catch(SQLException se) { 
	            errorCode = "Queue03";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
			try {
				if (cntn!=null) cntn.close();
			} catch(SQLException se) { 
	            errorCode = "Queue04";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2}",new Object[]{"Queue",errorCode,errorMessage});
		}
	}
	
	public void setResult(int id, String status, String result) {
		if (status.equals("Queued")) {
			setNotified1ById(id,result);
		} else if(status.equals("Notified1")) {
			setNotified2ById(id,result);
		} else if(status.equals("Notified2")) {
			setNotified3ById(id,result);				
		} else if(status.equals("Notified3")) {
			setFailedById(id,result);
		} else {
			setFailedById(id,result);			
		}
	}
}
