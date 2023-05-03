package com.cpd.chatwrite.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProps rabbitProps;

    @Bean
    public Queue queue() {
        return new Queue(rabbitProps.getQueue());
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(rabbitProps.getExchange());
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(rabbitProps.getRoutingKey());
    }
}
