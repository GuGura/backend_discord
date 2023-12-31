package com.example.backend.config;

import com.example.backend.service.RedisSubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    /**
     * 단일 Topic 사용을 위한 Bean 설정
     */
    @Bean
    public ChannelTopic chatChannelTopic() {
        return new ChannelTopic("chatroom");
    }

    @Bean
    public ChannelTopic alarmChannelTopic() {
        return new ChannelTopic("alarm");
    }


    /**
     * redis pub/sub 메시지를 처리하는 listener 설정
     */
    @Bean
    public RedisMessageListenerContainer chatMessageListener(RedisConnectionFactory connectionFactory,
                                                             MessageListenerAdapter listenerAdapterForMessage,
                                                             ChannelTopic chatChannelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterForMessage, chatChannelTopic);
        return container;
    }

    @Bean
    public RedisMessageListenerContainer alarmMessageListener(RedisConnectionFactory connectionFactory,
                                                              MessageListenerAdapter listenerAdapterForAlarm,
                                                              ChannelTopic alarmChannelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterForAlarm, alarmChannelTopic);
        return container;
    }


    /**
     * 실제 메시지를 처리하는 subscriber 설정 추가
     */
    @Bean
    public MessageListenerAdapter listenerAdapterForMessage(RedisSubscriberService subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    @Bean
    public MessageListenerAdapter listenerAdapterForAlarm(RedisSubscriberService subscriber) {
        return new MessageListenerAdapter(subscriber, "sendAlarm");
    }


    /**
     * 어플리케이션에서 사용할 redisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }
}