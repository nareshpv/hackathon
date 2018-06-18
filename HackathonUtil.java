import java.io.FileReader;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class HackathonUtil {

	public static void updateCustomerInfo(String account,String type, String value) {
		String invalidData = validateCustomerInfo(type, value);
		if (invalidData == null) {
			JSONParser parser = new JSONParser();
			try {

				Object obj = parser.parse(new FileReader("E:\\customerInfo.json"));
				JSONArray jsonArray = new JSONArray();
				JSONArray jsonList = (JSONArray) obj;
				for (Object jsonObj : jsonList) {
					JSONObject jsonObject = (JSONObject) jsonObj;
					String acct = (String) jsonObject.get("account");

					if (account.equals(acct)) {
						jsonObject.put(type, value);
					}

					jsonArray.add(jsonObject);
				}
				// Create a new FileWriter object
				FileWriter fileWriter = new FileWriter("E:\\customerInfo.json");

				fileWriter.write(jsonArray.toJSONString());
				fileWriter.close();

				System.out.println("JSON Object Successfully written to the file!!");

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Entered one is : "+invalidData);
		}
	}

	private static String validateCustomerInfo(String type, String value) {
		if("phone".equals(type) && !isValidatePhoneNumber(value)) {
			return "Invalid Phone";
		}else if("email".equals(type) && !isValidateEmailId(value)){
			return "Invalid Email";
		}
		return null;
	}
	
	private static boolean isValidateEmailId(String value) {
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	private static boolean isValidatePhoneNumber(String phoneNo) {
		//validate phone numbers of format "1234567890"
		if (phoneNo.matches("\\d{10}")) return true;
		//validating phone number with -, . or spaces
		else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
		//validating phone number with extension length from 3 to 5
		else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
		//validating phone number where area code is in braces ()
		else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
		//return false if nothing matches the input
		else return false;
		
	}
	
	public static void sendSMS(String phoneNumber,String msg) {
		HttpURLConnection uc = null;
		try {
			String username = "pvnb4u@gmail.com";
			String hash = "1aiqjtG9mJs-gT8IoarhAugoS25ZKpf01GM7DbdILI";
			String test = "0";

			String sender = "TXTLCL"; // This is who the message appears to be from.

			String requestUrl = "http://api.textlocal.in/send/?" + "username=" + URLEncoder.encode(username, "UTF-8")
					+ "&hash=" + URLEncoder.encode(hash, "UTF-8") + "&message=" + URLEncoder.encode(msg, "UTF-8")
					+ "&sender=" + URLEncoder.encode(sender, "UTF-8") + "&numbers="
					+ URLEncoder.encode(phoneNumber, "UTF-8") + "&test=" + URLEncoder.encode(test, "UTF-8");

			URL url = new URL(requestUrl);
			uc = (HttpURLConnection) url.openConnection();

			System.out.println("Response: "+uc.getResponseMessage());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
		finally {
			uc.disconnect();
		}

	}

}
