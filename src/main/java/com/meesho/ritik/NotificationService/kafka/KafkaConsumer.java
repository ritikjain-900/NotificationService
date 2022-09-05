package com.meesho.ritik.NotificationService.kafka;

import com.meesho.ritik.NotificationService.model.Blacklist;
import com.meesho.ritik.NotificationService.model.SmsRequests;
import com.meesho.ritik.NotificationService.model.elasticsearch.ElasticSearchSmsDocument;
import com.meesho.ritik.NotificationService.model.response.ThirdPartyApiResponse;
import com.meesho.ritik.NotificationService.repository.BlacklistRedisRepository;
import com.meesho.ritik.NotificationService.repository.BlacklistRepository;
import com.meesho.ritik.NotificationService.repository.ElasticSearchSmsRepository;
import com.meesho.ritik.NotificationService.repository.SmsRepository;
import com.meesho.ritik.NotificationService.service.ThirdPartyApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@EnableConfigurationProperties
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    public SmsRepository smsRepository;

    @Autowired
    public BlacklistRedisRepository blacklistRedisRepository;

    @Autowired
    public BlacklistRepository blacklistRepository;

    @Autowired
    public ThirdPartyApiService thirdPartyApiService;

    @Autowired
    public ElasticSearchSmsRepository elasticSearchSmsRepository;



    @KafkaListener(topics = "notification.send_sms", groupId = "myGroup")
    public void extract(String id){
        log.info("Received Id is: {}", id);
        SmsRequests sms = smsRepository.findById(Integer.parseInt(id)).get();
        log.info("Message extracted from database is : {}", sms);
        Blacklist blacklist = new Blacklist(sms.getPhoneNumber());

        if(blacklistRedisRepository.isMember(sms.getPhoneNumber())){

            log.info("updating message details");
            sms.setStatus("BLACKLISTED");
            sms.setFailureCode("Blacklist_404");
            sms.setFailureComments("Cannot send SMS as phone-number is blacklisted");

            log.info("saving updated sms after redis-blacklist check");
            smsRepository.save(sms);
        }
        else if(blacklistRepository.existsById(sms.getPhoneNumber())){

            log.info("Adding this phone-number in cache (redis)");
            blacklistRedisRepository.save(blacklist);
            log.info("Successfully added in cache (Redis)");

            log.info("updating message details");
            sms.setStatus("BLACKLISTED");
            sms.setFailureCode("Blacklist_404");
            sms.setFailureComments("Cannot send SMS as phone-number is blacklisted");

            log.info("saving updated sms after database-blacklist check");
            smsRepository.save(sms);

        }
        else{
            log.info("indexing details in ElasticSearch");
            ElasticSearchSmsDocument elasticSearchSmsDocument = new ElasticSearchSmsDocument();
            elasticSearchSmsDocument.setId(String.valueOf(sms.getId()));
            elasticSearchSmsDocument.setMessage(sms.getMessage());
            elasticSearchSmsDocument.setPhoneNumber(sms.getPhoneNumber());
            elasticSearchSmsDocument.setCreatedAt(sms.getCreatedAt());
            elasticSearchSmsRepository.save(elasticSearchSmsDocument);
            log.info("Saved in ElasticSearch");
            log.info("Document saved in elasticSearch is : {}",elasticSearchSmsRepository.findById(String.valueOf(sms.getId())).get());
            //send sms to 3rd party api

            //ThirdPartyApiResponse response = thirdPartyApiService.send(sms);
            //log.info("Response from ThirdPartyApi is : {}" , response);
            //log.info("Updating message details");
            //sms.setStatus(response.getThirdPartyApiResponseObjectList().get(0).getDescription());
            //smsRepository.save(sms);

        }
    }


}
