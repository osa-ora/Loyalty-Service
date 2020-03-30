package osa.ora;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LoyaltyResourceTest {

    @Test
    public void testGetBalance() {
        given()
          .when().get("/loyalty/balance/100")
          .then()
             .statusCode(200)
             .body(is("id"));
    }

}