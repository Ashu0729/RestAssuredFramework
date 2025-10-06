package api.utilities;

import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaValidatorUtil {
    /**
     * Validates the response body against a JSON schema in the classpath.
     * @param response The RestAssured response object
     * @param schemaPath The path to the schema file relative to classpath (e.g., "schemas/get_user_schema.json")
     */
    public static void validateResponseSchema(Response response, String schemaPath) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath));
    }
}
