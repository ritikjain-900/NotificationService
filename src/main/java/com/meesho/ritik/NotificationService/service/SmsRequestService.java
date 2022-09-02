package com.meesho.ritik.NotificationService.service;

import com.meesho.ritik.NotificationService.controller.NotificationServiceController;
import com.meesho.ritik.NotificationService.kafka.KafkaProducer;
import com.meesho.ritik.NotificationService.model.Blacklist;
import com.meesho.ritik.NotificationService.model.SmsRequests;
import com.meesho.ritik.NotificationService.model.dto.*;
import com.meesho.ritik.NotificationService.model.elasticsearch.ElasticSearchSmsDocument;
import com.meesho.ritik.NotificationService.model.request.AllSmsElasticRequestBody;
import com.meesho.ritik.NotificationService.model.request.BlacklistRequestBody;
import com.meesho.ritik.NotificationService.model.request.SendSmsRequestBody;
import com.meesho.ritik.NotificationService.model.request.TextSearchRequestBody;
import com.meesho.ritik.NotificationService.model.response.GetSmsSuccessResponse;
import com.meesho.ritik.NotificationService.model.response.SendSmsErrorResponse;
import com.meesho.ritik.NotificationService.model.response.SendSmsSuccessResponse;
import com.meesho.ritik.NotificationService.repository.BlacklistRedisRepository;
import com.meesho.ritik.NotificationService.repository.BlacklistRepository;
import com.meesho.ritik.NotificationService.repository.ElasticSearchSmsRepository;
import com.meesho.ritik.NotificationService.repository.SmsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SmsRequestService {

    private static final Logger log = LoggerFactory.getLogger(SmsRequestService.class);

    @Autowired
    public SmsRepository smsRepository;

    @Autowired
    public KafkaProducer kafkaProducer;

    @Autowired
    public BlacklistRepository blacklistRepository;

    @Autowired
    public BlacklistRedisRepository blacklistRedisRepository;

    @Autowired
    public ElasticSearchSmsRepository elasticSearchSmsRepository;

    public SendSmsSuccessResponse sendSmsService(SendSmsRequestBody sendSmsRequestBody){
        log.info("Request body: {}", sendSmsRequestBody);
        //validateRequest(sendSmsRequestBody);
        SmsRequests sms = new SmsRequests();
        sms.setPhoneNumber(sendSmsRequestBody.getPhoneNumber());
        sms.setMessage(sendSmsRequestBody.getMessage());
        sms.setStatus("NOT-BLOCKED");
        SmsRequests savedSms = smsRepository.save(sms);
        String id = Integer.toString(savedSms.getId());
        kafkaProducer.publish(id);
        SendSmsSuccessResponse sendSmsSuccessResponse = new SendSmsSuccessResponse();
        sendSmsSuccessResponse.setRequestId(sms.getId());
        sendSmsSuccessResponse.setComments("Successfully Sent");
        return sendSmsSuccessResponse;
    }

    public SendSmsSuccessResponse addToBlacklistService(BlacklistRequestBody blacklistRequestBody){
        log.info("Request body: {}", blacklistRequestBody);
        Blacklist blacklist = new Blacklist();
        //BlacklistRedis blacklistRedis = new BlacklistRedis();
        List<String> phoneNumbers = blacklistRequestBody.getPhoneNumbers();
        for(String number : phoneNumbers)
        {
            blacklist.setPhoneNumber(number);
            //blacklistRedis.setPhoneNumber(number);
            log.info("saving in database");
            blacklistRepository.save(blacklist);
            log.info("saved in database");

            log.info("saving in redis");
            blacklistRedisRepository.save(blacklist);
            log.info("saved in redis");

        }
        SendSmsSuccessResponse sendSmsSuccessResponse = new SendSmsSuccessResponse();
        sendSmsSuccessResponse.setComments("Successfully blacklisted");
        return sendSmsSuccessResponse;
    }

    public BlacklistResponseDto getBlacklistedNumbersService(){
        log.info("getting all blacklisted numbers from database");
        List<Blacklist> blacklisetdNumbers = blacklistRepository.findAll();
        List<String> numbers = new ArrayList<>();
        for(Blacklist blacklist : blacklisetdNumbers)
        {
            numbers.add(blacklist.getPhoneNumber());
        }
        BlacklistResponseDto blacklistResponseDto = new BlacklistResponseDto(numbers);
        return blacklistResponseDto;
    }

    public BlacklistResponseDto getBlacklistedNumbersFromRedisService(){
        log.info("getting all blacklisted numbers from redis");
        List<Blacklist> blacklisetdNumbers = blacklistRedisRepository.findAll();
        List<String> numbers = new ArrayList<>();
        for(Blacklist blacklistRedis : blacklisetdNumbers)
        {
            numbers.add(blacklistRedis.getPhoneNumber());
        }
        BlacklistResponseDto blacklistResponseDto = new BlacklistResponseDto(numbers);
        return blacklistResponseDto;
    }

    public SendSmsSuccessResponse deleteFromBlacklistService(String phoneNumber){
        log.info("Deleting from database");
        blacklistRepository.deleteById(phoneNumber);
        log.info("Successfully deleted from database");
        log.info("Deleting from Redis (Cache)");
        blacklistRedisRepository.delete(phoneNumber);
        log.info("Successfully deleted from Redis (Cache)");
        SendSmsSuccessResponse sendSmsSuccessResponse = new SendSmsSuccessResponse();
        sendSmsSuccessResponse.setComments("Successfully deleted " + phoneNumber + " from blacklist");
        return sendSmsSuccessResponse;
    }

    public GetSmsResponseDto getSmsByIdService(int id){
        Optional<SmsRequests> sms = smsRepository.findById(id);
        SmsRequests sms1 = sms.get();
        GetSmsSuccessResponse getSmsSuccessResponse = GetSmsSuccessResponse.builder()
                .failureComments(sms1.getFailureComments())
                .createdAt(sms1.getCreatedAt())
                .id(sms1.getId())
                .failureCode(sms1.getFailureCode())
                .message(sms1.getMessage())
                .phoneNumber(sms1.getPhoneNumber())
                .status(sms1.getStatus())
                .updatedAt(sms1.getUpdatedAt())
                .build();
        GetSmsResponseDto getSmsResponseDto = GetSmsResponseDto.builder()
                .data(getSmsSuccessResponse)
                .build();
        return getSmsResponseDto;
    }

    public AllSmsResponseDto getAllSmsService(){
        List<SmsRequests> smsRequestsList = smsRepository.findAll();
        AllSmsResponseDto allSmsResponseDto = AllSmsResponseDto.builder().data(smsRequestsList).build();
        return allSmsResponseDto;
    }

    public long DateConversion(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date =null;
        try{
            date = simpleDateFormat.parse(dateString);
        }
        catch(Exception e){
            log.error(String.valueOf(e));
        }
        return date.getTime();
    }

    public AllSmsElasticResponseDto getAllSmsElasticService(AllSmsElasticRequestBody allSmsElasticRequestBody){

        long startEpoch = DateConversion(allSmsElasticRequestBody.getStartDate());
        long endEpoch = DateConversion(allSmsElasticRequestBody.getEndDate());
        Page<ElasticSearchSmsDocument> elasticSearchSmsDocumentPage = elasticSearchSmsRepository.findAllByCreatedAtBetween(startEpoch, endEpoch, PageRequest.of(allSmsElasticRequestBody.getOffset(),allSmsElasticRequestBody.getLimit()));
        Set<AllSmsElasticDtoUtil> data = new HashSet<>();
        for(ElasticSearchSmsDocument elasticSearchSmsDocument : elasticSearchSmsDocumentPage){
            data.add(AllSmsElasticDtoUtil.builder().message(elasticSearchSmsDocument.getMessage()).phoneNumber(elasticSearchSmsDocument.getPhoneNumber()).build());
        }
        boolean hasNext = elasticSearchSmsDocumentPage.getTotalPages()>elasticSearchSmsDocumentPage.getPageable().getPageSize();
        return AllSmsElasticResponseDto.builder().hasNext(hasNext).data(data).build();
    }

    public AllSmsElasticResponseDto getAllSmsWithGivenTextService(TextSearchRequestBody textSearchRequestBody){
        Page<ElasticSearchSmsDocument> elasticSearchSmsDocumentPage = elasticSearchSmsRepository.findAllByMessage(textSearchRequestBody.getMessage(), PageRequest.of(0,20));
        Set<AllSmsElasticDtoUtil> data = new HashSet<>();
        for(ElasticSearchSmsDocument elasticSearchSmsDocument : elasticSearchSmsDocumentPage){
            data.add(AllSmsElasticDtoUtil.builder().message(elasticSearchSmsDocument.getMessage()).phoneNumber(elasticSearchSmsDocument.getPhoneNumber()).build());
        }
        boolean hasNext = elasticSearchSmsDocumentPage.getTotalPages()>elasticSearchSmsDocumentPage.getPageable().getPageSize();
        return AllSmsElasticResponseDto.builder().data(data).hasNext(hasNext).build();
    }
}
