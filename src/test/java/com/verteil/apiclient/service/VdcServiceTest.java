package com.verteil.apiclient.service;


import com.verteil.air.v1.AirShopReqAttributeQueryType;
import com.verteil.air.v1.AirShoppingRQ;
import com.verteil.air.v1.AirShoppingRS;
import com.verteil.air.v1.AnonymousTravelerType;
import com.verteil.air.v1.Departure;
import com.verteil.air.v1.FarePreferencesType;
import com.verteil.air.v1.FlightArrivalType;
import com.verteil.air.v1.FlightDepartureType;
import com.verteil.air.v1.TravelerCoreType;
import com.verteil.air.v1.Travelers;
import com.verteil.apiclient.connector.Login;
import com.verteil.apiclient.connector.RequestMetaData;
import com.verteil.apiclient.constants.ApiConstants;
import com.verteil.apiclient.constants.ApiServices;
import com.verteil.apiclient.constants.CarrierConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class VdcServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VdcServiceTest.class);

    private VdcService vdcService = new VdcService();

    @DisplayName("one way single")
    @Test
    void testAirShopping() throws Exception {
        var request = new AirShoppingRQ();
        request.setTravelers(prepareTravelers());
        request.setCoreQuery(prepareCoreQuery());
        request.setPreference(preparePreference());
        AirShoppingRS airShoppingRS = vdcService.airShopping(prepareLogin(), prepareRequestMetaData(), request);
       Assertions.assertThat(airShoppingRS.getDataLists()).isNotNull();
    }

    private Travelers prepareTravelers() {
        var travelers = new Travelers();
        var traveler = new Travelers.Traveler();
        var ptc = new TravelerCoreType.PTC();
        ptc.setValue("ADT");
        var anonymousTravelerType = new AnonymousTravelerType();
        anonymousTravelerType.setPTC(ptc);
        traveler.getAnonymousTraveler().add(anonymousTravelerType);
        travelers.getTraveler().add(traveler);
        return travelers;
    }

    private AirShoppingRQ.CoreQuery prepareCoreQuery() throws DatatypeConfigurationException {
        var coreQuery = new AirShoppingRQ.CoreQuery();

        var deptAirportCode = new FlightDepartureType.AirportCode();
        deptAirportCode.setValue("LHR");
        var deptDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDate.now().plus(20, ChronoUnit.DAYS).toString());
        var departure = new Departure();
        departure.setAirportCode(deptAirportCode);
        departure.setDate(deptDate);

        var arrivalAirportCode = new FlightArrivalType.AirportCode();
        arrivalAirportCode.setValue("FRA");

        var arrival = new FlightArrivalType();
        arrival.setAirportCode(arrivalAirportCode);

        var originDestination = new AirShopReqAttributeQueryType.OriginDestination();
        originDestination.setDeparture(departure);
        originDestination.setArrival(arrival);
        originDestination.setOriginDestinationKey("OD1");

        var airShopReqAttributeQueryType = new AirShopReqAttributeQueryType();
        airShopReqAttributeQueryType.getOriginDestination().add(originDestination);

        coreQuery.setOriginDestinations(airShopReqAttributeQueryType);
        return coreQuery;
    }

    private AirShoppingRQ.Preference preparePreference() {
        var preference = new AirShoppingRQ.Preference();

        var publType = new FarePreferencesType.Types.Type();
        publType.setCode("PUBL");
        var pvtType = new FarePreferencesType.Types.Type();
        pvtType.setCode("PVT");

        var types = new FarePreferencesType.Types();
        types.getType().add(publType);
        types.getType().add(pvtType);

        var farePreferencesType = new FarePreferencesType();
        farePreferencesType.setTypes(types);

        preference.setFarePreferences(farePreferencesType);
        return preference;
    }

    private Login prepareLogin() {
        Login login = new Login();
        login.setUsername(ApiConstants.username);
        login.setPassword(ApiConstants.password);
        return login;
    }

    private RequestMetaData prepareRequestMetaData() {
        RequestMetaData metaData = new RequestMetaData();
        metaData.setService(ApiServices.AIR_SHOPPING);
        metaData.setThirdPartyid(CarrierConstants.BRITISH_AIRWAYS);
        return metaData;
    }
}
