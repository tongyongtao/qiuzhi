package com.tyt.qiuzhi.async;

import com.alibaba.fastjson.JSON;
import com.tyt.qiuzhi.util.JedisAdapter;
import com.tyt.qiuzhi.util.RedisKeyUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType,List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;



    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> types = entry.getValue().getSupportEventTypes();
                for (EventType type : types) {
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> brpop = jedisAdapter.brpop(0, key);

                    if(brpop != null){
                        for (String message : brpop) {
                            if (message.equals(key)){
                                continue;
                            }
                            EventModel eventModel = JSON.parseObject(message, EventModel.class);
                            if (!config.containsKey(eventModel.getType())){
                                logger.error("不能识别的事件");
                                continue;
                            }
                            for (EventHandler handler : config.get(eventModel.getType())){
                                handler.doHandle(eventModel);
                            }

                        }
                    }else {
                        String ping = jedisAdapter.ping();
                        if (!"PONG".equals(ping)){
                            logger.error("Redis ping failure!");
                            throw new RuntimeException("Redis ping failure!");
                        }
                        try {
                            Thread.sleep(1000*10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
