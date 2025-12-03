package be.kdg.gameplay.adapter.out;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.MatchStartedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MatchEventListener {

    private final RabbitTemplate rabbitTemplate;

    public MatchEventListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        System.out.println("=== MatchEventListener INITIALIZED ===");
    }

    @EventListener(MatchStartedEvent.class)
    public void publishMatchStarted(MatchStartedEvent event) {
        System.out.println("=== MatchEventListener TRIGGERED ===");
        System.out.println("Event: " + event.matchId());
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