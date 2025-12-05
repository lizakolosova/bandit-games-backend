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
    }

    @EventListener(MatchStartedEvent.class)
    public void publishMatchStarted(MatchStartedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQTopology.EXCHANGE,
                RabbitMQTopology.MATCH_STARTED_ROUTING_KEY,
                event
        );
    }
}