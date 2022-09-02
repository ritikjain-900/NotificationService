package com.meesho.ritik.NotificationService.repository;

import com.meesho.ritik.NotificationService.model.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class BlacklistRedisRepository {

    //BELOW CODE IS USED WHEN WE DON'T EXTEND THIS CLASS FROM CrudRepository
    public static final String HASH_KEY = "BlacklistRedisSet";

    @Autowired
    public RedisTemplate<String, Object> template;
    
    private String ritik;

    public void save(Blacklist blacklist){

        template.opsForSet().add(HASH_KEY, blacklist);
    }

    public List<Blacklist> findAll(){
        Set<Object> objects = template.opsForSet().members(HASH_KEY);
        List<Blacklist> blacklists = new ArrayList<>();
        for(Object obj : objects)
        {
            blacklists.add((Blacklist) obj);
        }
        return blacklists;
    }

    public Blacklist delete(String phoneNumber){
        Blacklist blacklist = new Blacklist(phoneNumber);
        template.opsForSet().remove(HASH_KEY, blacklist);
        return blacklist;
    }

    public boolean isMember(String phoneNumber){
        Blacklist blacklist = new Blacklist(phoneNumber);
        return template.opsForSet().isMember(HASH_KEY, blacklist);
    }

}
