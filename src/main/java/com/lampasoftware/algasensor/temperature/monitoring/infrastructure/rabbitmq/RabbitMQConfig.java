package com.lampasoftware.algasensor.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PROCESS_TEMPERATURE_V_1_Q = "temperature-monitoring.process-temperature.v1.q";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queue(){
        return QueueBuilder.durable(PROCESS_TEMPERATURE_V_1_Q).build();
    }

    public FanoutExchange exchange(){
        // The exchange is originally declared in the temperature-processing that is the producer.
        // Therefore, in this microservice is not necessary to declare this as a spring bean.
        // This method is just created to organize the code, as the binding method needs the exchange reference.
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e").build();
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange());
    }

}
