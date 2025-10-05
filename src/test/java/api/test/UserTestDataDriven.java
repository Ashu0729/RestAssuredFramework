package api.test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import api.endpoints.UserEndPoints;
import api.utilities.DataProviders;
import api.payload.UserPayload;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;

public class UserTestDataDriven {
	@AfterMethod
	public void waitBetweenTests() throws InterruptedException {
	// Wait for 4 seconds between tests
	Thread.sleep(2000);
	}

    @Test(dataProvider = "userDataFromExcel", dataProviderClass = DataProviders.class, priority = 1, enabled = true)
    public void testPostUser(String id, String username, String firstName, String lastName, String email, String password, String phone, String userStatus) throws InterruptedException {
        UserPayload userPayload = new UserPayload();
        userPayload.setId(Integer.parseInt(id));
        userPayload.setUsername(username);
        userPayload.setFirstName(firstName);
        userPayload.setLastName(lastName);
        userPayload.setEmail(email);
        userPayload.setPassword(password);
        userPayload.setPhone(phone);
        userPayload.setUserStatus(Integer.parseInt(userStatus));
        System.out.println("********* POST USER *********");
        Response response = UserEndPoints.postUser(userPayload);
        response.then().log().all();
        response.then().statusCode(200);
        Thread.sleep(10000);
    }

    @Test(dataProvider = "userDataFromExcel", dataProviderClass = DataProviders.class, priority = 2, enabled = true)
    public void testGetUser(String id, String username, String firstName, String lastName, String email, String password, String phone, String userStatus) {
        System.out.println("********* GET USER *********");
        Response response = UserEndPoints.getUser(username);
        response.then().log().all();
        response.then().statusCode(200)
                .body("id", equalTo(Integer.parseInt(id)))
                .body("username", equalTo(username))
                .body("firstName", equalTo(firstName))
                .body("lastName", equalTo(lastName))
                .body("email", equalTo(email))
                .body("password", equalTo(password))
                .body("phone", equalTo(phone))
                .body("userStatus", equalTo(Integer.parseInt(userStatus)));
    }

    @Test(dataProvider = "userDataFromExcel", dataProviderClass = DataProviders.class, priority = 3, enabled = false)
    public void testUpdateUser(String id, String username, String firstName, String lastName, String email, String password, String phone, String userStatus) {
        UserPayload userPayload = new UserPayload();
        userPayload.setId(Integer.parseInt(id));
        userPayload.setUsername(username);
        userPayload.setFirstName(firstName + "upd");
        userPayload.setLastName(lastName);
        userPayload.setEmail(email + "upd");
        userPayload.setPassword(password);
        userPayload.setPhone(phone);
        userPayload.setUserStatus(Integer.parseInt(userStatus));
        System.out.println("********* UPDATE USER *********");
        Response response = UserEndPoints.updateUser(userPayload, username);
        response.then().log().all();
        response.then().statusCode(200);
        
        // verify the updated values after update
        Response responseAfterUpdate = UserEndPoints.getUser(username);
        responseAfterUpdate.then().log().all();
        responseAfterUpdate.then().statusCode(200)
        		.body("firstName", equalTo(firstName + "upd"))
                .body("email", equalTo(email + "upd"));
    }

    @Test(dataProvider = "userDataFromExcel", dataProviderClass = DataProviders.class, priority = 4, enabled = false)
    public void testDeleteUser(String id, String username, String firstName, String lastName, String email, String password, String phone, String userStatus) {
        System.out.println("********* DELETE USER *********");
        Response response = UserEndPoints.deleteUser(username);
        response.then().log().all();
        response.then().statusCode(200);
    }
}