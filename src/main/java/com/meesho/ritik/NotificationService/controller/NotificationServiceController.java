package com.meesho.ritik.NotificationService.controller;

import com.meesho.ritik.NotificationService.kafka.KafkaProducer;
import com.meesho.ritik.NotificationService.model.Blacklist;
import com.meesho.ritik.NotificationService.model.SmsRequests;
import com.meesho.ritik.NotificationService.model.dto.AllSmsElasticResponseDto;
import com.meesho.ritik.NotificationService.model.dto.AllSmsResponseDto;
import com.meesho.ritik.NotificationService.model.dto.BlacklistResponseDto;
import com.meesho.ritik.NotificationService.model.dto.GetSmsResponseDto;
import com.meesho.ritik.NotificationService.model.request.AllSmsElasticRequestBody;
import com.meesho.ritik.NotificationService.model.request.BlacklistRequestBody;
import com.meesho.ritik.NotificationService.model.request.SendSmsRequestBody;
import com.meesho.ritik.NotificationService.model.request.TextSearchRequestBody;
import com.meesho.ritik.NotificationService.model.response.GetSmsSuccessResponse;
import com.meesho.ritik.NotificationService.model.response.SendSmsErrorResponse;
import com.meesho.ritik.NotificationService.model.response.SendSmsResponse;
import com.meesho.ritik.NotificationService.model.response.SendSmsSuccessResponse;
import com.meesho.ritik.NotificationService.repository.BlacklistRedisRepository;
import com.meesho.ritik.NotificationService.repository.BlacklistRepository;
import com.meesho.ritik.NotificationService.repository.SmsRepository;
import com.meesho.ritik.NotificationService.service.SmsRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class NotificationServiceController {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceController.class);

    @Autowired
    public SmsRequestService smsRequestService;

    @PostMapping("/sms/send")
    public ResponseEntity<SendSmsResponse> sendSms(@RequestBody SendSmsRequestBody sendSmsRequestBody){
        try{
            SendSmsSuccessResponse sendSmsSuccessResponse = smsRequestService.sendSmsService(sendSmsRequestBody);
            return new ResponseEntity<>(SendSmsResponse.builder().data(sendSmsSuccessResponse).build(), HttpStatus.OK);
        }
        catch(Exception e){
            SendSmsErrorResponse sendSmsErrorResponse = new SendSmsErrorResponse();
            return new ResponseEntity<>(SendSmsResponse.builder().error(sendSmsErrorResponse).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/blacklist")
    public ResponseEntity<SendSmsResponse> addToBlacklist(@RequestBody BlacklistRequestBody blacklistRequestBody){
        try{
            SendSmsSuccessResponse sendSmsSuccessResponse = smsRequestService.addToBlacklistService(blacklistRequestBody);
            return new ResponseEntity<>(SendSmsResponse.builder().data(sendSmsSuccessResponse).build(), HttpStatus.OK);
        }
        catch(Exception e){
            log.error(String.valueOf(e));
            SendSmsErrorResponse sendSmsErrorResponse = new SendSmsErrorResponse("Unable to add to blacklist");
            return new ResponseEntity<>(SendSmsResponse.builder().error(sendSmsErrorResponse).build(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/blacklist")
    public ResponseEntity<BlacklistResponseDto> getBlacklistedNumbers(){
        BlacklistResponseDto blacklistResponseDto = smsRequestService.getBlacklistedNumbersService();
        return new ResponseEntity<>(blacklistResponseDto, HttpStatus.OK);
    }

    @GetMapping("/blacklist/redis")
    public ResponseEntity<BlacklistResponseDto> getBlacklistedNumbersFromRedis(){
        BlacklistResponseDto blacklistResponseDto = smsRequestService.getBlacklistedNumbersFromRedisService();
        return new ResponseEntity<>(blacklistResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/blacklist/delete/{phone_number}")
    public ResponseEntity<SendSmsResponse> deleteFromBlacklist(@PathVariable(value = "phone_number") String phone_number){
        log.info("deleting {} from blacklist", phone_number);
        try{
            SendSmsSuccessResponse sendSmsSuccessResponse = smsRequestService.deleteFromBlacklistService(phone_number);
            return new ResponseEntity<>(SendSmsResponse.builder().data(sendSmsSuccessResponse).build(), HttpStatus.OK);
        }
        catch(Exception e){
            SendSmsErrorResponse sendSmsErrorResponse = new SendSmsErrorResponse("Unable to delete " + phone_number + " from blacklist");
            return new ResponseEntity<>(SendSmsResponse.builder().error(sendSmsErrorResponse).build(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sms/{id}")
    public ResponseEntity<GetSmsResponseDto> getSmsById(@PathVariable(value = "id") int id){
        log.info("Getting details of sms with id : {}", id);
        try{
            GetSmsResponseDto getSmsResponseDto = smsRequestService.getSmsByIdService(id);
            return new ResponseEntity<>(getSmsResponseDto, HttpStatus.OK);
        }
        catch(Exception e){
            SendSmsErrorResponse sendSmsErrorResponse = SendSmsErrorResponse.builder()
                    .code("INVALID_REQUEST")
                    .message("request_id not found")
                    .build();
            return new ResponseEntity<>(GetSmsResponseDto.builder().error(sendSmsErrorResponse).build(), HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/sms")
    public ResponseEntity<AllSmsResponseDto> getAllSms(){
        log.info("Getting details of all sms from database");
        AllSmsResponseDto allSmsResponseDto = smsRequestService.getAllSmsService();
        return new ResponseEntity<>(allSmsResponseDto, HttpStatus.OK);
    }

    @GetMapping("/phone_number/search")
    public ResponseEntity<AllSmsElasticResponseDto> getAllSmsElastic(@RequestBody AllSmsElasticRequestBody allSmsElasticRequestBody){
        log.info("Getting all sms from elastic-search between start-date and end-date");
        try{
            AllSmsElasticResponseDto allSmsElasticResponseDto = smsRequestService.getAllSmsElasticService(allSmsElasticRequestBody);
            return new ResponseEntity<>(allSmsElasticResponseDto, HttpStatus.OK);
        }
        catch(Exception e){
            log.error(String.valueOf(e));
            AllSmsElasticResponseDto allSmsElasticResponseDto = AllSmsElasticResponseDto.builder().error("Unable to get messages from start-date to end-date").build();
            return new ResponseEntity<>(allSmsElasticResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/text/search")
    public ResponseEntity<AllSmsElasticResponseDto> getAllSmsWithGivenText(@RequestBody TextSearchRequestBody textSearchRequestBody){
        log.info("Getting all sms from elastic-search with given text : {}", textSearchRequestBody);
        try{
            AllSmsElasticResponseDto allSmsElasticResponseDto = smsRequestService.getAllSmsWithGivenTextService(textSearchRequestBody);
            return new ResponseEntity<>(allSmsElasticResponseDto, HttpStatus.OK);
        }
        catch(Exception e){
            log.error(String.valueOf(e));
            AllSmsElasticResponseDto allSmsElasticResponseDto = AllSmsElasticResponseDto.builder().error("Unable to get messages from text search").build();
            return new ResponseEntity<>(allSmsElasticResponseDto, HttpStatus.NOT_FOUND);
        }
    }



     //  private void validateRequest(SendSmsRequestBody sendSmsRequestBody){
    //     if(Objects.isNull(sendSmsRequestBody.getPhoneNumber())){
     //       throw new NullPointerException("Test Exception");
      //  }
    //}
}

//11:00:58	CREATE TABLE `blacklist` (   `phone_number` varchar(45) DEFAULT NULL,   PRIMARY KEY (`phone_number`) ) ENGINE=InnoDB DEFAULT CHARSET=latin1	Error Code: 1171. All parts of a PRIMARY KEY must be NOT NULL; if you need NULL in a key, use UNIQUE instead	0.0032 sec
