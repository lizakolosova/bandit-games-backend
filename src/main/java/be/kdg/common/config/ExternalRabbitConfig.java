package be.kdg.common.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalRabbitConfig {

    @Bean
    public ConnectionFactory externalConnectionFactory(@Value("${external.rabbitmq.host}") String host,
            @Value("${external.rabbitmq.username}") String user, @Value("${external.rabbitmq.password}") String pass,
                                                       @Value("${external.rabbitmq.port}") int port) {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(pass);
        factory.setPort(port);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory externalRabbitListenerContainerFactory(ConnectionFactory externalConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(externalConnectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}

