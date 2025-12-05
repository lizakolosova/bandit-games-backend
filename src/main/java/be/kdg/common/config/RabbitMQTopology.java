package be.kdg.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String EXCHANGE = "gameplay.match";
    public static final String MATCH_STARTED_ROUTING_KEY = "match.started";
    public static final String MATCH_STARTED_QUEUE = "match.started.queue";

    public static final String CHESS_EXCHANGE = "gameExchange";

    public static final String CHESS_GAME_ENDED_QUEUE = "platform.chess.game.ended.queue";
    public static final String CHESS_ACHIEVEMENT_QUEUE = "platform.chess.achievement.queue";
    public static final String CHESS_GAME_CREATED_QUEUE = "platform.chess.game.created.queue";
    public static final String CHESS_GAME_UPDATED_QUEUE = "platform.chess.game.updated.queue";

    public static final String GAME_REGISTERED_ROUTING_KEY = "game.registered";

    public static final String PLATFORM_GAME_REGISTERED_QUEUE = "platform.game.registered.queue";

    public static final String GAMEPLAY_GAME_REGISTERED_QUEUE = "gameplay.game.registered.queue";

    public static final String PLAYER_GAME_REGISTERED_QUEUE = "player.game.registered.queue";

    public static final String GAME_ENDED_KEY = "game.ended";
    public static final String ACHIEVEMENT_KEY = "achievement.acquired";
    public static final String GAME_CREATED_KEY = "game.created";
    public static final String GAME_UPDATED_KEY = "game.player.names.updated";

    @Bean
    public TopicExchange gameplayExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue matchStartedQueue() {
        return QueueBuilder.durable(MATCH_STARTED_QUEUE).build();
    }

    @Bean
    public Binding matchStartedBinding() {
        return BindingBuilder.bind(matchStartedQueue())
                .to(gameplayExchange())
                .with(MATCH_STARTED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange chessGameExchange() {
        return new TopicExchange(CHESS_EXCHANGE, true, false);
    }

    @Bean
    public Queue chessGameEndedQueue() {
        return QueueBuilder.durable(CHESS_GAME_ENDED_QUEUE).build();
    }

    @Bean
    public Binding chessGameEndedBinding() {
        return BindingBuilder.bind(chessGameEndedQueue())
                .to(chessGameExchange())
                .with(GAME_ENDED_KEY);
    }

    @Bean
    public Queue chessAchievementQueue() {
        return QueueBuilder.durable(CHESS_ACHIEVEMENT_QUEUE).build();
    }

    @Bean
    public Binding chessAchievementBinding() {
        return BindingBuilder.bind(chessAchievementQueue())
                .to(chessGameExchange())
                .with(ACHIEVEMENT_KEY);
    }

    @Bean
    public Queue chessGameCreatedQueue() {
        return QueueBuilder.durable(CHESS_GAME_CREATED_QUEUE).build();
    }

    @Bean
    public Binding chessGameCreatedBinding() {
        return BindingBuilder.bind(chessGameCreatedQueue())
                .to(chessGameExchange())
                .with(GAME_CREATED_KEY);
    }

    @Bean
    public Queue chessGameUpdatedQueue() {
        return QueueBuilder.durable(CHESS_GAME_UPDATED_QUEUE).build();
    }

    @Bean
    public Binding chessGameUpdatedBinding() {
        return BindingBuilder.bind(chessGameUpdatedQueue())
                .to(chessGameExchange())
                .with(GAME_UPDATED_KEY);
    }

    @Bean
    public Queue platformGameRegisteredQueue() {
        return QueueBuilder.durable(PLATFORM_GAME_REGISTERED_QUEUE).build();
    }

    @Bean
    public Binding platformGameRegisteredBinding() {
        return BindingBuilder.bind(platformGameRegisteredQueue())
                .to(chessGameExchange())
                .with(GAME_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Queue gameplayGameRegisteredQueue() {
        return QueueBuilder.durable(GAMEPLAY_GAME_REGISTERED_QUEUE).build();
    }

    @Bean
    public Binding gameplayGameRegisteredBinding() {
        return BindingBuilder.bind(gameplayGameRegisteredQueue())
                .to(chessGameExchange())
                .with(GAME_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Queue playerGameRegisteredQueue() {
        return QueueBuilder.durable(PLAYER_GAME_REGISTERED_QUEUE).build();
    }

    @Bean
    public Binding playerGameRegisteredBinding() {
        return BindingBuilder.bind(playerGameRegisteredQueue())
                .to(chessGameExchange())
                .with(GAME_REGISTERED_ROUTING_KEY);
    }
}
