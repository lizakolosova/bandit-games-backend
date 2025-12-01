package be.kdg.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String EXCHANGE = "gameplay.match";
    public static final String ROUTING_KEY = "match.started";
    public static final String QUEUE = "match.started.queue";

    @Bean
    public TopicExchange gameplayExchange() {
        return new TopicExchange(EXCHANGE, true, false); // durable, not auto-delete
    }

    @Bean
    public Queue matchStartedQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding matchStartedBinding() {
        return BindingBuilder.bind(matchStartedQueue())
                .to(gameplayExchange())
                .with(ROUTING_KEY);
    }
}