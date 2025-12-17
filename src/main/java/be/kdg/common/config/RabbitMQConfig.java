package be.kdg.common.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class RabbitMQConfig {

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @Value("${spring.rabbitmq.port:5673}")
    private int port;

    @Value("${spring.rabbitmq.username:user}")
    private String username;

    @Value("${spring.rabbitmq.password:password}")
    private String password;

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        // DEBUG OUTPUT
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║   Creating RabbitMQ ConnectionFactory    ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║ Host: " + host);
        System.out.println("║ Port: " + port);
        System.out.println("║ Username: " + username);
        System.out.println("╚═══════════════════════════════════════════╝");

        return factory;
    }

    @Bean
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonMessageConverter());
        return factory;
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}