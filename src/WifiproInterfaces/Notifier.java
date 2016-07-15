package WifiproInterfaces;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Notifier {
	public String urlString;
	public String errorCode;
	public String errorMessage;
	public String result;
	private Logger LOG;

	public Notifier() {
		this.LOG = Logger.getLogger(Notifier.class.getName());
	}
	
	public Notifier(String urlstring) {
		this.LOG = Logger.getLogger(Notifier.class.getName());
		urlString = urlstring;
	}
	
	public void setUrl(String urlstring) {
		urlString = urlstring;
	}
	
	public String post(String name, String data, String method, String id) { //remove String name after can make sure that NMS doesnt need it
        errorCode = "Notifier-01";
        errorMessage = "Found unidentified error";
        
        result = "No result";
        HttpURLConnection urlcon = null;
        DataOutputStream output = null;
        InputStream input = null;
        BufferedReader reader = null;
        String postdata = "";
        
        try {
/*
            postdata = name + "=" + URLEncoder.encode(data,Utils.CharacterEncoding);
            URL url = new URL(urlString);
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
*/
        	
            
            //postdata = name + "=" + URLEncoder.encode(data,Utils.CharacterEncoding);
            postdata = data;
            if (!method.equals("POST")) {urlString += "/" + id;}
LOG.log(Level.INFO,"{0}-method: {1},url: {2}",new Object[]{"Notifier",method,urlString});
            URL url = new URL(urlString);
            urlcon = (HttpURLConnection)url.openConnection();
            //urlcon.setRequestMethod("POST");
            urlcon.setRequestMethod(method);
            urlcon.setRequestProperty("Content-Type", "application/json;charset=" + Utils.CharacterEncoding);
            urlcon.setUseCaches(false);
            
            if (!method.equals("DELETE")) {
                urlcon.setDoInput(true);
                urlcon.setDoOutput(true);
                
                OutputStream os = urlcon.getOutputStream();
                os.write(postdata.getBytes());
    //System.out.println(postdata);
                os.flush();
                
                input = urlcon.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input,Utils.CharacterEncoding));
                result = reader.readLine();
            } else {
            	result = Integer.toString(urlcon.getResponseCode());
            	//result = result + "-" + urlcon.getContent().toString();
            	if (result.equals("200")) {result = "{`result`:`success`}".replace('`', '"');}
            	else {result = "Response code: "+result;}
            }

            
/*

HttpURLConnection con = (HttpURLConnection) object.openConnection();
con.setDoOutput(true);
con.setDoInput(true);
con.setRequestProperty("Content-Type", "application/json");
con.setRequestProperty("Accept", "application/json");
con.setRequestMethod("POST");

JSONObject cred   = new JSONObject();
JSONObject auth   = new JSONObject();
JSONObject parent = new JSONObject();

cred.put("username","adm");
cred.put("password", "pwd");

auth.put("tenantName", "adm");
auth.put("passwordCredentials", cred);

parent.put("auth", auth);

OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
wr.write(parent.toString());
wr.flush();

*/
            
/*
 HttpURLConnection httpcon = (HttpURLConnection) ((new URL("a url").openConnection()));
httpcon.setDoOutput(true);
httpcon.setRequestProperty("Content-Type", "application/json");
httpcon.setRequestProperty("Accept", "application/json");
httpcon.setRequestMethod("POST");
httpcon.connect();

byte[] outputBytes = "{'value': 7.5}".getBytes("UTF-8");
OutputStream os = httpcon.getOutputStream();
os.write(outputBytes);

os.close();            
 */

            errorCode = "Notifier00";
            errorMessage = "No error";
        } catch (MalformedURLException e) { 
            errorCode = "Notifier03";
            errorMessage = e.toString();
        } catch (IOException e) {
            errorCode = "Notifier04";
            errorMessage = e.toString();result = errorMessage;
        } finally {
            if(reader!=null){try{reader.close();}catch(Exception e){errorCode="Notifier96";errorMessage=e.toString();}reader=null;}
            if(output!=null){try{output.close();}catch(Exception e){errorCode="Notifier97";errorMessage=e.toString();}output=null;}
            if(input!=null){try{input.close();}catch(Exception e){errorCode="Notifier98";errorMessage=e.toString();}input=null;}
            if(urlcon!=null){try{urlcon.disconnect();}catch(Exception e){errorCode="Notifier99";errorMessage=e.toString();}urlcon=null;}
LOG.log(Level.INFO,"{0}-errorCode: {1},errorMessage: {2}, result:|{3}|",new Object[]{"Notifier",errorCode,errorMessage,result});
        }
        return result;
	}

}
