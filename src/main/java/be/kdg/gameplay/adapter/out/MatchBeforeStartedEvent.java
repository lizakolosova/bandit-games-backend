package be.kdg.gameplay.adapter.out;

import be.kdg.common.config.RabbitMQTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MatchBeforeStartedEvent {

    private final RabbitTemplate rabbitTemplate;

    public MatchBeforeStartedEvent(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        System.out.println("=== MatchEventListener INITIALIZED ===");
    }

    @EventListener(be.kdg.common.events.MatchBeforeStartedEvent.class)
    public void publishMatchBeforeStartedEvent(be.kdg.common.events.MatchBeforeStartedEvent event) {
        System.out.println("=== MatchEventListener TRIGGERED ===");
        System.out.println("host player Name: " + event.hostPlayerName());
        System.out.println("host player ID: " + event.hostPlayerId());
        System.out.println("Exchange: " + RabbitMQTopology.EXCHANGE);
        System.out.println("Routing key: " + RabbitMQTopology.ROUTING_KEY);

        rabbitTemplate.convertAndSend(
                RabbitMQTopology.EXCHANGE,
                RabbitMQTopology.ROUTING_KEY,
                event
        );

        System.out.println("=== Message sent to RabbitMQ ===");
    }
}