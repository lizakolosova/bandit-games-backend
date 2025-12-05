package be.kdg.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String EXCHANGE = "gameplay.match";
    public static final String ROUTING_KEY = "match.before.started";
    public static final String QUEUE = "match.before.started.queue";

    @Bean
    public TopicExchange gameplayExchange() {
        return new TopicExchange(EXCHANGE, true, false); // durable, not auto-delete
    }

    @Bean
    public Queue matchBeforeStartedQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding matchBeforeStartedBinding() {
        return BindingBuilder.bind(matchBeforeStartedQueue())
                .to(gameplayExchange())
                .with(ROUTING_KEY);
    }
}