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

@WebServlet("/ProjectNotify")
public class ProjectNotify extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProjectNotify() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger LOG = Logger.getLogger(ProjectNotify.class.getName());
        String errorCode = "ProjectNotify-01";
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
			urlstring = Utils.NMSUrlProjectInsert;
			logString = "INSERT";
			method = "POST";
		} else if (action.equals("UPDATE")) {
			urlstring = Utils.NMSUrlProjectUpdate;
			logString = "UPDATE";
			method = "PUT";
		} else if (action.equals("DELETE")) {
			urlstring = Utils.NMSUrlProjectDelete;
			logString = "DELETE";
			method = "DELETE";
		}
		logString += " " + urlstring;
		
		Project p = new Project();
		p.getById(Integer.parseInt(data));
		
		Notifier n = new Notifier();
		n.setUrl(urlstring);
		String result = n.post("data",p.getJson(),method, data); //remove "data" later after can make sure NMS doesnt need it
//System.out.println(result);
//System.out.println(status + " " + Integer.toString(id));
		logString += " submitted data:" + p.getJson();
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
	            errorCode = "ProjectNotify00";
	            errorMessage = "No error";
			} catch (ParseException e) {
		        errorCode = "ProjectNotify01";
		        errorMessage = "Parsing returned JSON error! position: " + e.getPosition() + " " + e.getMessage();
				q.setResult(id, status, result);
			}
		} else {
			q.setResult(id, status, result);
		}
		
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2},Events: {3}",new Object[]{"ProjectNotify",errorCode,errorMessage,logString});

/*

        String historyId = request.getParameter("historyId");
        HttpURLConnection urlcon =null;
        DataOutputStream output = null;
        InputStream input = null;
        BufferedReader reader = null;
        String postdata = "historyId=" + URLEncoder.encode(historyId,Utils.CharacterEncoding);	
        String errorCode = "HistoryRCaseByID-01";
        String errorMessage = "Found unidentified error";
        JSONObject json;
        JSONArray jsonA = new JSONArray();
        JSONArray jsonZ = new JSONArray();
        try {
            URL url = new URL("http://" + Utils.SingleAppIP + ":" + Utils.SingleAppPort + "/SingleApp/HistoryRCaseByID");
            urlcon = (HttpURLConnection)url.openConnection();
            urlcon.setRequestMethod("POST");
            urlcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + Utils.CharacterEncoding);
            urlcon.setRequestProperty("Content-Length", "" + Integer.toString(postdata.getBytes().length));
            urlcon.setUseCaches(false);
            urlcon.setDoInput(true);
            urlcon.setDoOutput(true);
            output = new DataOutputStream(urlcon.getOutputStream());
            output.writeBytes(postdata);
            output.flush();
			
            input = urlcon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input,Utils.CharacterEncoding));
            String result = reader.readLine();
            if (result != null) {
                JSONParser parser = new JSONParser();
                try {
                    jsonZ = (JSONArray)parser.parse(result);
                    errorCode = "HistoryRCaseByID00";
                    errorMessage = "No error";
                } catch (ParseException e) {
                    errorCode = "HistoryRCaseByID99";
                    errorMessage = e.toString();
                }
            } else {
                errorCode = "HistoryRCaseByID01";
                errorMessage = "No data";
            }
        } catch (MalformedURLException e) { 
            errorCode = "HistoryRCaseByID99";
            errorMessage = e.toString();
        } catch (IOException e) {
            errorCode = "HistoryRCaseByID99";
            errorMessage = e.toString();
        } finally {
            if(reader!=null){try{reader.close();}catch(Exception e){errorCode="HistoryRCaseByID99";errorMessage=e.toString();}reader=null;}
            if(output!=null){try{output.close();}catch(Exception e){errorCode="HistoryRCaseByID99";errorMessage=e.toString();}output=null;}
            if(input!=null){try{input.close();}catch(Exception e){errorCode="HistoryRCaseByID99";errorMessage=e.toString();}input=null;}
            if(urlcon!=null){try{urlcon.disconnect();}catch(Exception e){errorCode="HistoryRCaseByID99";errorMessage=e.toString();}urlcon=null;}
        }
        
        //json = new JSONObject();
        //json.put("error_code",errorCode);
        //json.put("error_message",errorMessage);
        //jsonA.add(json);
        //jsonZ.add(0,jsonA);

        response.setContentType("text/html");
        response.setCharacterEncoding(Utils.CharacterEncoding);
        PrintWriter out = response.getWriter();
        jsonZ.writeJSONString(out);
        out.flush();out.close();
LOG.log(Level.INFO,"jsonZ:{0}",new Object[]{jsonZ.toJSONString()});
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2}",new Object[]{"HistoryRCaseByID",errorCode,errorMessage});
    }

        
*/

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
