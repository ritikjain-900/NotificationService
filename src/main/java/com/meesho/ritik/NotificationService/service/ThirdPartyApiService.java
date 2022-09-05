package com.meesho.ritik.NotificationService.service;

import com.meesho.ritik.NotificationService.model.SmsRequests;
import com.meesho.ritik.NotificationService.model.request.thirdPartyApi.Channels;
import com.meesho.ritik.NotificationService.model.request.thirdPartyApi.Destination;
import com.meesho.ritik.NotificationService.model.request.thirdPartyApi.Sms;
import com.meesho.ritik.NotificationService.model.request.thirdPartyApi.ThirdPartyApiRequest;
import com.meesho.ritik.NotificationService.model.response.ThirdPartyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ThirdPartyApiService {

    private static final Logger log = LoggerFactory.getLogger(ThirdPartyApiService.class);

    private String url = "https://api.imiconnect.in/resources/v1/messaging";

    private String key = "93ceffda-5941-11ea-9da9-025282c394f2";

    RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("key", key);
        return headers;
    }

    public ThirdPartyApiResponse send(SmsRequests smsRequests){

        try{
            ThirdPartyApiRequest request = new ThirdPartyApiRequest();
            request.setDeliveryChannel("sms");
            request.setChannels(new Channels(new Sms(smsRequests.getMessage())));
            Destination destination = Destination.builder().correlationId(String.valueOf(smsRequests.getId())).
                    msisdn(Arrays.asList(smsRequests.getPhoneNumber())).build();
            request.setDestinations(Arrays.asList(destination));
            HttpEntity<ThirdPartyApiRequest> httpEntity = new HttpEntity<>(request, getHeaders());

            log.info("HttpEntity : {}", httpEntity);

            //ResponseEntity<String> response = restTemplate.exchange( url, HttpMethod.POST, httpEntity, String.class);
            ResponseEntity<ThirdPartyApiResponse> response = restTemplate.exchange( url, HttpMethod.POST, httpEntity, ThirdPartyApiResponse.class);

            return response.getBody();
        }
        catch (Exception e){
            log.error(String.valueOf(e));
            //ThirdPartyApiResponse thirdPartyApiResponse = ThirdPartyApiResponse.builder().error("Unable to send sms via Third Party API").build();
            //return thirdPartyApiResponse;
            //return "Unable to send sms via Third Party API";
            return null;
        }
    }
}
