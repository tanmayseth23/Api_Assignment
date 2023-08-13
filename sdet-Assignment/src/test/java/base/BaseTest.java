package base;

import io.restassured.RestAssured;
import org.apiEngine.EndPoints;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    public static String idForGetApis;

    @BeforeSuite
    void setup() {
        RestAssured.baseURI = EndPoints.BASE_URL;
    }


}
