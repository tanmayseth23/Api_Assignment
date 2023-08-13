package org.service;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apiEngine.IRestResponse;
import org.apiEngine.RestResponse;
import org.configs.ConfigReader;
import org.model.request.Customer;
import io.restassured.http.Header;
import org.model.response.CustomerResponse;

import java.util.List;

public class CustomerApis {

    public static final String ID = "id";
    public static final int SUCCESS_STATUS_CODE = 200;

    public static IRestResponse<Customer> getCustomer(String id, List<Header> headerList) {
        Headers headers = new Headers(headerList);
        Response res = RestAssured.given()
                .when()
                .queryParam(ID, id)
                .headers(headers)
                .log().all()
                .get(ConfigReader.getInstance().getCustomerApi())
                .then()
                .log().all()
                .extract().response();
        if (res.getStatusCode() == SUCCESS_STATUS_CODE) {
            return new RestResponse<>(Customer.class, res);
        } else {
            return new RestResponse(Error.class, res);
        }
    }

    public static IRestResponse<Customer> getCustomerWithInvalidUrl(String id, List<Header> headerList) {
        Headers headers = new Headers(headerList);
        Response res = RestAssured.given()
                .when()
                .queryParam(ID, id)
                .headers(headers)
                .log().all()
                .get(ConfigReader.getInstance().getWrongCustomerApi())
                .then()
                .log().all()
                .extract().response();
        return new RestResponse(Error.class, res);
    }

    public static IRestResponse<Customer> getCustomerSchemaValidation(String id, List<Header> headerList) {
        Headers headers = new Headers(headerList);
        Response res = RestAssured.given()
                .when()
                .queryParam(ID, id)
                .headers(headers)
                .log().all()
                .get(ConfigReader.getInstance().getCustomerApi())
                .then()
                .log().all()
                .assertThat().statusCode(SUCCESS_STATUS_CODE)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("getCustomerSchema.json"))
                .extract().response();
        return new RestResponse(Customer.class, res);
    }

    public static RestResponse createCustomer(List<Header> headerList, Customer customer) {
        Headers headers = new Headers(headerList);
        Gson gson = new Gson();
        Response res = RestAssured.given()
                .when()
                .headers(headers)
                .body(gson.toJson(customer))
                .log().all()
                .post(ConfigReader.getInstance().createCustomerApi())
                .then()
                .log().all()
                .extract().response();
        if (res.getStatusCode() == SUCCESS_STATUS_CODE) {
            return new RestResponse<>(CustomerResponse.class, res);
        } else {
            return new RestResponse<>(Error.class, res);
        }
    }

    public static IRestResponse<CustomerResponse> createCustomerWithWrongUrl(List<Header> headerList, Customer customer) {
        Headers headers = new Headers(headerList);
        Gson gson = new Gson();
        Response res = RestAssured.given()
                .when()
                .headers(headers)
                .body(gson.toJson(customer))
                .log().all()
                .post(ConfigReader.getInstance().invalidCreateCustomerApi())
                .then()
                .log().all()
                .extract().response();
        return new RestResponse(Error.class, res);
    }
}
