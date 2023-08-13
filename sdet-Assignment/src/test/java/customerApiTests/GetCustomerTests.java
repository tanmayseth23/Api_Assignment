package customerApiTests;

import base.BaseTest;
import com.google.gson.Gson;
import io.restassured.http.Header;
import org.apiEngine.IRestResponse;
import org.model.request.Customer;
import org.model.response.ErrorResponse;
import org.service.CustomerApis;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GetCustomerTests extends BaseTest {

    @Test(groups = {"getApiTests", "positive"}, dependsOnGroups = "dataSetup")
    void getCustomerSchemaValidation() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomerSchemaValidation(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 200);
    }

    @Test(groups = {"getApiTests", "positive"}, dependsOnGroups = "dataSetup")
    void getCustomerWithValidId() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 200);
        Gson gson = new Gson();
        Customer customer = gson.fromJson(res.getContent(), Customer.class);
        System.out.println("customer" + customer.toString());
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithNoHeaders() {
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, new ArrayList<>());
        Assert.assertEquals(res.getStatusCode(), 403);
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithMissingFirstHeader() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 401);
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithMissingSecondHeader() {
        List<Header> headerList = Stream.of(
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 403);
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithInvalidHeaderValue() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "bot"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 400);
        Gson gson = new Gson();
        ErrorResponse errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
        Assert.assertEquals(errorResponse.error, "bad bot, go away!");
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithOneInvalidHeader() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "unauthorized-user"),
                        new Header("user-agent", "test67556"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 401);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithDifferentSecondHeaderValue() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test7667"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 200);
        Gson gson = new Gson();
        Customer customer = gson.fromJson(res.getContent(), Customer.class);
        System.out.println("customer" + customer.toString());
    }

    @Test(groups = {"getApiTests", "negative"})
    void getCustomerWithInvalidParameter() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer("test", headerList);
        Assert.assertEquals(res.getStatusCode(), 400);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "error while fetching customer");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"getApiTests", "negative"})
    void getCustomerWithNoParameter() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer("", headerList);
        Assert.assertEquals(res.getStatusCode(), 400);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "error while fetching customer");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"getApiTests", "negative"}, dependsOnGroups = "dataSetup")
    void getCustomerWithInvalidUrl() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomerWithInvalidUrl(idForGetApis, headerList);
        Assert.assertEquals(res.getStatusCode(), 404);
        Assert.assertEquals(res.getResponse().asString(), "404 page not found");
    }

    @Test(groups = {"getApiTests", "negative"})
    void getCustomerWithInvalidCustomer() {
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<Customer> res = CustomerApis.getCustomer("999999999", headerList);
        Assert.assertEquals(res.getStatusCode(), 400);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "error while fetching customer");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

}
