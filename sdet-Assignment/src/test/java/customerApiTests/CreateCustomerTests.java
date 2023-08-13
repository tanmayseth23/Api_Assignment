package customerApiTests;

import base.BaseTest;
import com.google.gson.Gson;
import io.restassured.http.Header;
import org.apiEngine.IRestResponse;
import org.model.request.Customer;
import org.model.response.CustomerResponse;
import org.model.response.ErrorResponse;
import org.service.CustomerApis;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.utils.RandomDataGeneratorUtil.*;

public class CreateCustomerTests extends BaseTest {

    @Test(groups = {"createApiTests", "positive"})
    void createCustomerWithValidData() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 200);
        Gson gson = new Gson();
        CustomerResponse customerResponse = gson.fromJson(res.getResponse().asString(), CustomerResponse.class);
        Assert.assertEquals(customerResponse.message, "customer created");
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithNoHeaders() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(new ArrayList<>(), customer);
        Assert.assertEquals(res.getStatusCode(), 403);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithMissingFirstHeader() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 403);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithMissingSecondHeader() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 403);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithInvalidHeaderValue() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "bot"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "bad bot, go away!");
            Assert.assertEquals(res.getStatusCode(), 400);
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithOneInvalidHeaderValues() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "unauthorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 403);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "request cannot be authenticated!");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithInvalidName() {
        String id = generateRandomNumber();
        String name = generateRandomString(55);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 500);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "CHECK constraint failed: length(name) <= 50");
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithInvalidNameAlphanumeric() {
        String id = generateRandomNumber();
        String name = "gvgvgj6677";
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "name has special characters");
            Assert.assertEquals(res.getStatusCode(), 400);
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithInvalidPhoneNumber() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(11);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "CHECK constraint failed: length(phone_number) = 10");
            Assert.assertEquals(res.getStatusCode(), 500);
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithInvalidPhoneNumberDigits() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(9);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Gson gson = new Gson();
        ErrorResponse errorResponse;
        try {
            errorResponse = gson.fromJson(res.getResponse().asString(), ErrorResponse.class);
            Assert.assertEquals(errorResponse.error, "CHECK constraint failed: length(phone_number) = 10");
            Assert.assertEquals(res.getStatusCode(), 500);
        } catch (Exception e) {
            throw new RuntimeException("The error response is wrong");
        }
    }

    @Test(groups = {"createApiTests", "positive"})
    void createCustomerWithValidDataAndNoName() {
        String id = generateRandomNumber();
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 200);
        Gson gson = new Gson();
        CustomerResponse customerResponse = gson.fromJson(res.getResponse().asString(), CustomerResponse.class);
        Assert.assertEquals(customerResponse.message, "customer created");
    }

    @Test(groups = {"createApiTests", "negative"})
    void createCustomerWithInvalidUrl() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        IRestResponse<CustomerResponse> res = CustomerApis.createCustomerWithWrongUrl(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 404);
        Assert.assertEquals(res.getResponse().asString(), "404 page not found");
    }
}
