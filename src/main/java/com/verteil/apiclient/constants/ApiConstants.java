package com.verteil.apiclient.constants;

public interface ApiConstants {

    String username = "<USERNAME>";
    String password = "<PASSWORD>";

    String URL = "https://api.stage.verteil.com/entrygate/rest/request";
    String LOGIN_URL = "http://localhost:8081/entrygate/login?remember-me=on";
    String OAUTH_URL = "https://api.stage.verteil.com/oauth2/token?grant_type=client_credentials&scope=api";
    String SERVICE = "Service";
    String THIRD_PARTY_ID = "ThirdpartyId";
}
