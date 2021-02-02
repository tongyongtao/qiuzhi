package com.tyt.qiuzhi.async;

import com.alibaba.fastjson.JSONObject;
import com.tyt.qiuzhi.util.JedisAdapter;
import com.tyt.qiuzhi.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String key = RedisKeyUtil.getEventQueueKey();
            String json = JSONObject.toJSONString(eventModel);
            jedisAdapter.lpush(key,json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
