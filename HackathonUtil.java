import java.io.FileReader;
import java.io.FileWriter;
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
					String email = (String) jsonObject.get("email");
					String phone = (String) jsonObject.get("phone");
					String acct = (String) jsonObject.get("account");

					// Add the values to the jsonObject
					jsonObject.put("email", email);
					jsonObject.put("phone", phone);
					jsonObject.put("account", acct);

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

}
