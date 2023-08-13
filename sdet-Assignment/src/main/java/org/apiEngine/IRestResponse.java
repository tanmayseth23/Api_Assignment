package org.apiEngine;

import io.restassured.response.Response;

public interface IRestResponse<T> {

    T getBody();

    String getContent();

    int getStatusCode();

    String getStatusDescription();

    Response getResponse();

    boolean isSuccessful();
}
