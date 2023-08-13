package customerApiTests;

import base.BaseTest;
import com.google.gson.Gson;
import io.restassured.http.Header;
import org.apiEngine.IRestResponse;
import org.apiEngine.RestResponse;
import org.model.request.Customer;
import org.model.response.CustomerResponse;
import org.model.response.ErrorResponse;
import org.service.CustomerApis;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.utils.RandomDataGeneratorUtil.*;

public class EndToEndTests extends BaseTest {

    @Test(groups = {"e2e", "dataSetup"})
    void e2E_TestWithCreateCustomerAndGetCustomer() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        RestResponse res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 200);
        Gson gson = new Gson();
        CustomerResponse customerResponse = gson.fromJson(res.getResponse().asString(), CustomerResponse.class);
        Assert.assertEquals(customerResponse.message, "customer created");
        int timeoutInSeconds = 60; // Maximum time to wait
        int pollingIntervalInSeconds = 5; // Interval between checks
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(timeoutInSeconds)) {
            IRestResponse<Customer> response = CustomerApis.getCustomer(id, headerList);
            if (response.getStatusCode() == 200)// should be 201
            {
                Gson gson1 = new Gson();
                Customer customer1 = gson1.fromJson(response.getResponse().asString(), Customer.class);
                Assert.assertEquals(customer1.getName(), name);
                Assert.assertEquals(customer1.getPhone_number(), phone_number);
                idForGetApis = id;
                break;
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(pollingIntervalInSeconds));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test(groups = "e2e")
    void e2E_TestWithCreateCustomerAndCreatingDuplicateRecord() {
        String id = generateRandomNumber();
        String name = generateRandomString(10);
        String phone_number = generateRandomPhoneNumber(10);
        Customer customer = new Customer(id, name, phone_number);
        List<Header> headerList = Stream.of(
                        new Header("x-session-token", "authorized-user"),
                        new Header("user-agent", "test"))
                .collect(Collectors.toList());
        RestResponse res = CustomerApis.createCustomer(headerList, customer);
        Assert.assertEquals(res.getStatusCode(), 200);
        Gson gson = new Gson();
        CustomerResponse customerResponse = gson.fromJson(res.getResponse().asString(), CustomerResponse.class);
        Assert.assertEquals(customerResponse.message, "customer created");
        int timeoutInSeconds = 60; // Maximum time to wait
        int pollingIntervalInSeconds = 5; // Interval between checks
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(timeoutInSeconds)) {
            IRestResponse<Customer> response = CustomerApis.getCustomer(id, headerList);
            if (response.getStatusCode() == 200)// should be 201
            {
                Gson gson1 = new Gson();
                Customer customer1 = gson1.fromJson(response.getResponse().asString(), Customer.class);
                Assert.assertEquals(customer1.getName(), name);
                Assert.assertEquals(customer1.getPhone_number(), phone_number);
                RestResponse response1 = CustomerApis.createCustomer(headerList, customer);
                System.out.println(response1.getResponse().asString());
                Gson gson2 = new Gson();
                ErrorResponse errorResponse;
                try {
                    errorResponse = gson2.fromJson(response1.getResponse().asString(), ErrorResponse.class);
                    Assert.assertEquals(errorResponse.error, "UNIQUE constraint failed: customers.id");
                    Assert.assertEquals(response1.getStatusCode(), 500);
                } catch (Exception e) {
                    throw new RuntimeException("The error response is wrong");
                }
                break;
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(pollingIntervalInSeconds));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
