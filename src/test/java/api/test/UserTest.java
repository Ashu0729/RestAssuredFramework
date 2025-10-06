package api.test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndPoints;
import api.payload.UserPayload;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import api.utilities.SchemaValidatorUtil;

public class UserTest {
	Faker faker = new Faker();
	UserPayload userPayload = new UserPayload();
	private static final Logger logger = LogManager.getLogger(UserTest.class);
	
	@BeforeClass
	public void setupUser() {
		logger.debug("Setting Random values using Faker");
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

	@Test(priority=1, enabled = true)
	public void testPostUser() throws InterruptedException {
		logger.info("*********** Starting - testPostUser *******************************");
		Response response = UserEndPoints.postUser(userPayload);
		response.then().log().all();
		response.then().statusCode(200);
		// Schema validation (add post_user_schema.json if available)
		// SchemaValidatorUtil.validateResponseSchema(response, "schemas/post_user_schema.json");
		Thread.sleep(10000);
		logger.info("*********** Ending - testPostUser *******************************");

	}
	
	@Test(priority=2, enabled = true)
	public void testGetUser() {
		logger.info("*********** Starting - testGetUser *******************************");
		Response response = UserEndPoints.getUser(userPayload.getUsername());
		response.then().log().all();
		response.then().statusCode(200);
		// Schema validation
		SchemaValidatorUtil.validateResponseSchema(response, "schemas/get_user_schema.json");
		logger.info("*********** Ending - testGetUser *******************************");

	}
	
	@Test(priority=3, enabled = false)
	public void testUpdateUser() {
		//Update few values using Faker
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		logger.info("*********** Starting - testUpdateUser *******************************");
		Response response = UserEndPoints.updateUser(userPayload, userPayload.getUsername());
		response.then().log().all();
		response.then().statusCode(200);

		logger.info("*********** Validating the Values after Updating the user *******************************");
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
		logger.info("*********** Ending - testUpdateUser *******************************");

	}
	
	@Test(priority=4, enabled = false)
	public void testDeleteUser() {
		logger.info("*********** Starting - testDeleteUser *******************************");
		Response response = UserEndPoints.deleteUser(userPayload.getUsername());
		response.then().log().all();
		response.then().statusCode(400);
		logger.info("*********** Ending - testDeleteUser *******************************");
	}
}