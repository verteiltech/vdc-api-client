package com.verteil.apiclient.connector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verteil.air.v1.AirShoppingRQ;
import com.verteil.air.v1.AirShoppingRS;
import com.verteil.apiclient.config.ObjectMapperConfig;
import com.verteil.apiclient.connector.handler.JsonResponseBodyHandler;
import com.verteil.apiclient.connector.handler.NdcResponseBodyHandler;
import com.verteil.apiclient.constants.ApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class ApiConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiConnector.class);

    private HttpClient httpClient = prepareHttpClient();
    private ObjectMapper objectMapper = ObjectMapperConfig.getInstance().createMapper();

    public String doLogin(Login login) throws Exception {
        var response = httpClient.send(prepareLoginHttpRequest(login),
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public OauthToken doOauthLogin(Login login) throws Exception {
        var response = prepareOauthHttpClient().send(prepareOauthHttpRequest(login),
                new JsonResponseBodyHandler<>(OauthToken.class));
        LOGGER.info("Oauth Response {}", response.body());
        return response.body();
    }

    public AirShoppingRS doShopping(OauthToken oauthToken, RequestMetaData requestMetaData, AirShoppingRQ airShoppingRQ) throws Exception {
        var response = httpClient.send(prepareHttpRequest(oauthToken, requestMetaData, airShoppingRQ),
                new NdcResponseBodyHandler<>(AirShoppingRS.class));
        return response.body();
    }

    private HttpClient prepareHttpClient() {
        return HttpClient.newBuilder()
                .cookieHandler(new CookieManager())
                .build();
    }

    private HttpClient prepareOauthHttpClient() {
        return HttpClient.newBuilder()
                .build();
    }

    private HttpRequest prepareHttpRequest(OauthToken oauthToken, RequestMetaData requestMetaData, AirShoppingRQ airShoppingRQ) throws JsonProcessingException {
        return HttpRequest.newBuilder()
                .uri(URI.create(ApiConstants.URL))
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + oauthToken.getAccessToken()) // This is required for oauth flow
                .header(ApiConstants.SERVICE, requestMetaData.getService())
                .header(ApiConstants.THIRD_PARTY_ID, requestMetaData.getThirdPartyid())
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper
                        .writeValueAsString(airShoppingRQ)))
                .build();
    }

    private HttpRequest prepareLoginHttpRequest(Login login) throws JsonProcessingException {
        return HttpRequest.newBuilder()
                .uri(URI.create(ApiConstants.LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper
                        .writeValueAsString(login)))
                .build();
    }

    private HttpRequest prepareOauthHttpRequest(Login login) throws JsonProcessingException, URISyntaxException {
        String credentials = login.getUsername() + ":" + login.getPassword();
        return HttpRequest.newBuilder()
                .uri(URI.create(ApiConstants.OAUTH_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .headers("Authorization", "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes()))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
    }
}
