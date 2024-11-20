package com.studleague.studleague.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {
    public static final String RESET_PASSWORD_QUEUE = "resetPasswordQueue";

    @Bean
    public Queue resetPasswordQueue() {
        return new Queue(RESET_PASSWORD_QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("exchange.direct");
    }

    @Bean
    public Binding binding(Queue resetPasswordQueue, DirectExchange exchange) {
        return BindingBuilder.bind(resetPasswordQueue).to(exchange).with("resetPasswordRoutingKey");
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("com.studleague.studleague.*", "java.util.*"));
        return converter;
    }
}
