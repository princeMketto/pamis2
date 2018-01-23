package pams.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Ephraim Swilla <ephraim@inetstz.com>
 * @version 2.1.0
 *
 */
public class SmsClass {

    final Integer smspro = 1;

    /**
     * --------------------------------------------------------------------------
     *
     * @var String API_KEY
     * --------------------------------------------------------------------------
   
     */
    final String API_KEY = "25543033426";
    /**
     * --------------------------------------------------------------------------
     *
     * @var String API_SECRET
     * --------------------------------------------------------------------------
 
     */
    final String API_SECRET = "00b39ae2de01dfae11e16eccb02dda9d7b7093bb";

    /**
     * -------------------------------------------------------------------------
     *
     * @var String name;
     * -------------------------------------------------------------------------
     *
     * A name that will appear in text SMS when user receives SMS. This will
     * only be valid if you set variable smspro=1
     *
     */
    final String name = "PAMIS";

    /**
     * @param phone_number
     * @param message
     * @return JSON Object
     * @throws java.lang.Exception
     */
    public String send_sms(String phone_number, String message) throws Exception {
	return this.curl(this.writeBufferSms(phone_number, message));
    }

    private String curl(String param) throws Exception {
	String returns = null;
	URL url = new URL("http://karibusms.com/api");
	URLConnection conn = url.openConnection();
	conn.setDoOutput(true);
	BufferedReader reader;
	try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
	    writer.write(param);
	    writer.flush();
	    String line;
	    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    while ((line = reader.readLine()) != null) {
		returns = line;
	    }
	}
	reader.close();
	return returns;
    }

    /**
     *
     * @return JSON Object
     * @throws Exception
     */
    public String get_statistics() throws Exception {
	return this.curl(this.writeBufferStatistic());
    }

    /**
     *
     * @param phone_number
     * @param message
     * @return String
     */
    private String writeBufferSms(String phone_number, String message) {
	return "api_key=" + API_KEY + "&karibusmspro=" + smspro + "&message=" + message + "&phone_number=" + phone_number + "&name=" + name + "&api_secret=" + API_SECRET;
    }

    /**
     *
     * @return String
     */
    private String writeBufferStatistic() {
	return "api_key=" + API_KEY + "&tag=get_statistics&api_secret=" + API_SECRET;
    }
   /* public static void main(String[] args) throws Exception {
    	SmsClass sms = new SmsClass();
    	String x = sms.send_sms("255764788862", "Testing from java api");
    	System.out.print(x);
        }*/
}

