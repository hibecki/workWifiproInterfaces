package WifiproInterfaces;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/EventsQuery")
public class EventsQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EventsQuery() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger LOG = Logger.getLogger(EventsQuery.class.getName());
        String errorCode = "EventsQuery-01";
        String errorMessage = "Found unidentified error";
		String logString = "No queued events";
        
		Connection cntn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rsst = null;
		
		URL url;
		URLConnection urlcon;
		
		try {
			String statusQuery = "status != 'Succeed'";
			String statusGet = request.getParameter("status");
			if ((statusGet != null) && (!statusGet.trim().equals(""))) {
				statusQuery = "status = '"+ statusGet +"'";
			}
			String queryString = "select * from nmsnotify where " + statusQuery +"  order by id asc";
//System.out.println("queryString-" + queryString);			
			Class.forName(Utils.JdbcDriverName);
			cntn = DriverManager.getConnection(Utils.JdbcConnectionstring);
			stmt = cntn.createStatement();
			rsst = stmt.executeQuery(queryString);
			pstmt = cntn.prepareStatement("update nmsnotify set status = 'Picked' where id = ?");

			int id;String object;String action;String data;String status;String urlstringPrefix = "";String urlstring="";
			
			while(rsst.next()){
				id  = rsst.getInt("id");
				object = rsst.getString("object");
				action = rsst.getString("action");
				data = rsst.getString("data");
				status = rsst.getString("status");

				if (object.equals("PROJECT")) {
					urlstring = "ProjectNotify";
				} else if (object.equals("SITE")) {
					urlstring = "SiteNotify";					
				} else if (object.equals("LOCATION")) {
					urlstring = "LocationNotify";
				} else if (object.equals("EQUIPMENT")) {
					urlstring = "EquipmentNotify";
				} else {
					urlstring = "Invalid Object";
				}
				
				pstmt.setInt(1, id);
				pstmt.executeUpdate();
				
				urlstringPrefix = request.getScheme() + "://" + request.getServerName() +  ":" + request.getServerPort() + request.getContextPath();

				url = new URL(urlstringPrefix+"/"+urlstring+"?id="+id+"&action="+action+"&data="+data+"&status="+status);
				urlcon = url.openConnection();
				urlcon.setConnectTimeout(100);
				urlcon.setReadTimeout(100);
				logString = "{"+urlstring+","+action+","+data+","+urlcon.getDate()+"}";
//LOG.log(Level.INFO,"{0}-{1}-{2}-{3}",new Object[]{"EventsQuery",urlstring,action,data});
			}
			rsst.close();

			stmt.close();
			pstmt.close();
			cntn.close();
            errorCode = "EventsQuery00";
            errorMessage = "No error";
		} catch(SQLException se) {
            errorCode = "EventsQuery01";
            errorMessage = se.getErrorCode() + " " + se.getMessage();
		} catch (MalformedURLException e) { 
            errorCode = "EventsQuery02";
            errorMessage = e.getMessage();
		} catch (IOException e) {
            errorCode = "EventsQuery03";
            errorMessage = e.getMessage();
		} catch(Exception e) {
            errorCode = "EventsQuery04";
            errorMessage = e.getMessage();
		} finally {
			try {
				if (stmt!=null) stmt.close();
				if (pstmt!=null) pstmt.close();
			} catch(SQLException se) { 
	            errorCode = "EventsQuery05";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
			try {
				if (cntn!=null) cntn.close();
			} catch(SQLException se) { 
	            errorCode = "EventsQuery06";
	            errorMessage = se.getErrorCode() + " " + se.getMessage();
			}
//logString = logString.substring(0, logString.length() - 1);
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},Events: {3}",new Object[]{"EventsQuery",errorCode,errorMessage,logString});
//response.getWriter().append("Ok").append(request.getContextPath());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
