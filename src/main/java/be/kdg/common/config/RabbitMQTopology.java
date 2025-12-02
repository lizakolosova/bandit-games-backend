package be.kdg.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    // for sending match event
    public static final String EXCHANGE = "gameplay.match";
    public static final String ROUTING_KEY = "match.started";
    public static final String QUEUE = "match.started.queue";

    // Chess game exchange and queues
    public static final String CHESS_EXCHANGE = "gameExchange";
    public static final String CHESS_GAME_ENDED_QUEUE = "platform.chess.game.ended.queue";
    public static final String CHESS_ACHIEVEMENT_QUEUE = "platform.chess.achievement.queue";
    public static final String CHESS_GAME_REGISTERED_QUEUE = "platform.chess.game.registered.queue";
    public static final String CHESS_GAME_CREATED_QUEUE = "platform.chess.game.created.queue";
    public static final String CHESS_GAME_UPDATED_QUEUE = "platform.chess.game.updated.queue";



    @Bean
    public TopicExchange gameplayExchange() {
        return new TopicExchange(EXCHANGE, true, false);
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

    // Chess game exchange (external)
    @Bean
    public TopicExchange chessGameExchange() {
        return new TopicExchange(CHESS_EXCHANGE, true, false);
    }

    // Queue for game ended events
    @Bean
    public Queue chessGameEndedQueue() {
        return QueueBuilder.durable(CHESS_GAME_ENDED_QUEUE).build();
    }

    @Bean
    public Binding chessGameEndedBinding() {
        return BindingBuilder.bind(chessGameEndedQueue())
                .to(chessGameExchange())
                .with("game.ended");
    }

    // Queue for achievement events
    @Bean
    public Queue chessAchievementQueue() {
        return QueueBuilder.durable(CHESS_ACHIEVEMENT_QUEUE).build();
    }

    @Bean
    public Binding chessAchievementBinding() {
        return BindingBuilder.bind(chessAchievementQueue())
                .to(chessGameExchange())
                .with("achievement.acquired");
    }

    @Bean
    public Queue chessGameRegisteredQueue() {
        return QueueBuilder.durable(CHESS_GAME_REGISTERED_QUEUE).build();
    }

    @Bean
    public Binding chessGameRegisteredBinding() {
        return BindingBuilder.bind(chessGameRegisteredQueue())
                .to(chessGameExchange())
                .with("game.registered");
    }
    @Bean
    public Queue chessGameCreatedQueue() {
        return QueueBuilder.durable(CHESS_GAME_CREATED_QUEUE).build();
    }

    @Bean
    public Queue chessGameUpdatedQueue() {
        return QueueBuilder.durable(CHESS_GAME_UPDATED_QUEUE).build();
    }
    @Bean
    public Binding chessGameCreatedBinding() {
        return BindingBuilder.bind(chessGameCreatedQueue())
                .to(chessGameExchange())
                .with("game.created");
    }

    @Bean
    public Binding chessGameUpdatedBinding() {
        return BindingBuilder.bind(chessGameUpdatedQueue())
                .to(chessGameExchange())
                .with("game.player.names.updated");
    }
}