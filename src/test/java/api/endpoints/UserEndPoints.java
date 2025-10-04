package api.endpoints;
import static io.restassured.RestAssured.given;
import api.payload.UserPayload;
import io.restassured.response.Response;

//Created to DEFINE the Endpoints/ EndpointMethods / CRUD Methods Implementation of User Module
public class UserEndPoints {
	
	
	public static Response postUser(UserPayload payload){	
		Response response = given()
			.contentType("application/json")
			.accept("application/json")
			.body(payload)
			.log().all() // Log all request details
		.when()
			.post(Roots.postUserURL);		
		return response;	
	}
	
	public static Response getUser(String usernamevalue){
		Response response = given()
			.pathParam("username", usernamevalue)
			.accept("application/json")
			.log().all() // Log all request details
		.when()
			.get(Roots.getUserURL);			
		return response;		
	}
	
	public static Response updateUser(UserPayload payload, String usernamevalue){
		Response response = given()
			.pathParam("username", usernamevalue)
			.contentType("application/json")
			.accept("application/json")
			.body(payload)
			.log().all() // Log all request details
		.when()
			.put(Roots.updateUserURL);
			
		return response;		
	}
	
	public static Response deleteUser(String usernamevalue){
		Response response = given()
			.pathParam("username", usernamevalue)
			.accept("application/json")
			.log().all() // Log all request details
		.when()
			.delete(Roots.deleteUserURL);			
		return response;		
	}
}