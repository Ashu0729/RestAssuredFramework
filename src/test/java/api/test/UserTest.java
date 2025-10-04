package api.test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndPoints;
import api.payload.UserPayload;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;

public class UserTest {
	Faker faker = new Faker();
	UserPayload userPayload = new UserPayload();
	
	@BeforeClass
	public void setupUserData() {
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		userPayload.setUserStatus(1);
	}
 
	@AfterMethod
	public void waitBetweenTests() throws InterruptedException {
	// Wait for 2 seconds between tests
	Thread.sleep(2000);
	}

	@Test(priority=1)
	public void testPostUser() {
		System.out.println("******************************************");
		Response response = UserEndPoints.postUser(userPayload);
		response.then().log().all();
		response.then().statusCode(200);
	}
	
	@Test(priority=2)
	public void testGetUser() {
		System.out.println("******************************************");
		Response response = UserEndPoints.getUser(userPayload.getUsername());
		response.then().log().all();
		response.then().statusCode(200);
	}
	
	@Test(priority=3)
	public void testUpdateUser() {
		//Update few values using Faker
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		System.out.println("******************************************");
		Response response = UserEndPoints.updateUser(userPayload, userPayload.getUsername());
		response.then().log().all();
		response.then().statusCode(200);

		//Checking the updated values after update
		System.out.println("**********************");
		Response responseAfterUpdate = UserEndPoints.getUser(userPayload.getUsername());
		responseAfterUpdate.then().log().all();
		responseAfterUpdate.then().statusCode(200)
							.body("id", equalTo(userPayload.getId()))
							.body("firstName", equalTo(userPayload.getFirstName()))
							.body("lastName", equalTo(userPayload.getLastName()))
							.body("email", equalTo(userPayload.getEmail()))
							.body("phone", equalTo(userPayload.getPhone()))
							.body("username", equalTo(userPayload.getUsername()))
							.body("userStatus", equalTo(userPayload.getUserStatus()));
	}
	
	@Test(priority=4)
	public void testDeleteUser() {
		System.out.println("******************************************");
		Response response = UserEndPoints.deleteUser(userPayload.getUsername());
		response.then().log().all();
		response.then().statusCode(400);
	}
}