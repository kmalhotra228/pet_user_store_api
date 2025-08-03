package payloads;

import com.github.javafaker.Faker;

public class UserPayload {
	
	static Faker faker = new Faker();
	static User userPayload = new User();
	
	public static User setUserPayload() {
		
		
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		return userPayload;
	}
	
	public static User setUserPayload(String userID, String userName,String fName,String lName,String useremail, String pwd,String phoneNumber) {
		
		
		
		userPayload.setId(Integer.parseInt(userID.trim()));
		userPayload.setUsername(userName);
		userPayload.setFirstName(fName);
		userPayload.setLastName(lName);
		userPayload.setEmail(useremail);
		userPayload.setPassword(pwd);
		userPayload.setPhone(phoneNumber);
		
		return userPayload;
	}
	
	
	public static User setUpdateUserPayload() {
		
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		return userPayload;
	}
	
	public static User setUpdateUserPayload(String fname, String lname) {
		
		userPayload.setFirstName(fname);
		userPayload.setLastName(lname);
		
		return userPayload;
	}

	
	

}
