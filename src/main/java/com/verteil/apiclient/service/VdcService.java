package com.verteil.apiclient.service;

import com.verteil.air.v1.AirShoppingRQ;
import com.verteil.air.v1.AirShoppingRS;
import com.verteil.apiclient.connector.ApiConnector;
import com.verteil.apiclient.connector.Login;
import com.verteil.apiclient.connector.OauthToken;
import com.verteil.apiclient.connector.RequestMetaData;

public class VdcService {

    private ApiConnector apiConnector = new ApiConnector();

    public AirShoppingRS airShopping(Login login, RequestMetaData requestMetaData, AirShoppingRQ airShoppingRQ) {
        AirShoppingRS airShoppingRS = null;
        try {
            OauthToken oauthToken = apiConnector.doOauthLogin(login);
            //    apiConnector.doLogin(login);
            airShoppingRS = apiConnector.doShopping(oauthToken, requestMetaData, airShoppingRQ);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return airShoppingRS;
    }
}
