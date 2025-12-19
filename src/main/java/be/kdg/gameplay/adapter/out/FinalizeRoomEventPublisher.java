package be.kdg.gameplay.adapter.out;

import be.kdg.common.config.RabbitMQTopology;
import be.kdg.common.events.FinalizeRoomEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FinalizeRoomEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(FinalizeRoomEventPublisher.class);


    public FinalizeRoomEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventListener(FinalizeRoomEvent.class)
    public void publishMatchBeforeStartedEvent(FinalizeRoomEvent event) {
        logger.info("=== MatchEventListener TRIGGERED ===");
        logger.info("host player Name: {}", event.hostPlayerName());
        logger.info("host player ID: {}", event.hostPlayerId());
        logger.info("Exchange: " + RabbitMQTopology.EXCHANGE);
        logger.info("Routing key: " + RabbitMQTopology.ROUTING_KEY);

        rabbitTemplate.convertAndSend(
                RabbitMQTopology.EXCHANGE,
                RabbitMQTopology.ROUTING_KEY,
                event
        );

        System.out.println("=== Message sent to RabbitMQ ===");
    }
}