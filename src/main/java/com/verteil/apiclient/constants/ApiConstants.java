package com.verteil.apiclient.constants;

public interface ApiConstants {

    String username = "<API USERNAME>";
    String password = "<API PASSWORD>";

    String URL = "http://localhost:8081/entrygate/rest/request";
    String LOGIN_URL = "http://localhost:8081/entrygate/login?remember-me=on";

    String OAUTH_URL = "http://localhost:9000/oauth2/token?grant_type=client_credentials&scope=api";
    String SERVICE = "Service";
    String THIRD_PARTY_ID = "ThirdpartyId";
}
