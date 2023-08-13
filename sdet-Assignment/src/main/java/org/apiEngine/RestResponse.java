package org.apiEngine;

import io.restassured.response.Response;

public class RestResponse<T> implements IRestResponse<T> {

    private T data;
    private Response response;

    public RestResponse(Class<T> t, Response response) {
        try {
            this.data = t.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("There should be a default constructor in the Response POJO");
        }
        this.response = response;
    }

    public T getBody() {
        data = (T) response.getBody().as(data.getClass());
        return data;
    }

    public String getContent() {
        return response.getBody().asString();
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public boolean isSuccessful() {
        int code = response.getStatusCode();
        return code == 200 || code == 201 || code == 202 || code == 203 || code == 204 ||
                code == 205;
    }

    public String getStatusDescription() {
        return response.getStatusLine();
    }

    public Response getResponse() {
        return response;
    }
}
