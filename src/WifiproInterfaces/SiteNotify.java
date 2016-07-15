package WifiproInterfaces;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/SiteNotify")
public class SiteNotify extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SiteNotify() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger LOG = Logger.getLogger(SiteNotify.class.getName());
        String errorCode = "SiteNotify-01";
        String errorMessage = "Found unidentified error";
        String logString = "No logstring";
        String method = "POST";
        
		int id;String action;String data;String status;
		id = Integer.parseInt(request.getParameter("id"));
		action = request.getParameter("action");
		data = request.getParameter("data");
		status = request.getParameter("status");
		
		String urlstring ="";
		if (action.equals("INSERT")) {
			urlstring = Utils.NMSUrlSiteInsert;
			logString = "INSERT";
			method = "POST";
		} else if (action.equals("UPDATE")) {
			urlstring = Utils.NMSUrlSiteUpdate;
			logString = "UPDATE";
			method = "PUT";
		} else if (action.equals("DELETE")) {
			urlstring = Utils.NMSUrlSiteDelete;
			logString = "DELETE";
			method = "DELETE";
		}
		logString += " " + urlstring;
		
		Site s = new Site();
		s.getById(Integer.parseInt(data));
		
		Notifier n = new Notifier();
		n.setUrl(urlstring);
		String result = n.post("data",s.getJson(),method, data); //remove "data" later after can make sure NMS doesnt need it
//System.out.println(result);
//System.out.println(status + " " + Integer.toString(id));
		logString += " submitted data:" + s.getJson();
		logString += " returned data:" + result;
		Queue q = new Queue();
		String returnResult = "";
		if ((result != null) && (!result.trim().isEmpty())) {
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(result);
				//JSONArray j = (JSONArray)obj;
				JSONObject j = (JSONObject)obj;
				returnResult = j.get("result").toString();
//System.out.println("returnResult = " + returnResult + ", status = " + status);
				if (returnResult.trim().toLowerCase().equals("success")) {
					q.setSucceedById(id,result);
				} else {
					q.setResult(id, status, result);
				}
	            errorCode = "SiteNotify00";
	            errorMessage = "No error";
			} catch (ParseException e) {
		        errorCode = "SiteNotify01";
		        errorMessage = "Parsing returned JSON error! position: " + e.getPosition() + " " + e.getMessage();
				q.setResult(id, status, result);
			}
		} else {
			q.setResult(id, status, result);
		}
		
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},Events: {3}",new Object[]{"SiteNotify",errorCode,errorMessage,logString});

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
