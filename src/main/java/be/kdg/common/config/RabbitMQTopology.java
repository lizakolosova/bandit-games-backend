package be.kdg.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String EXCHANGE = "gameplay.match";
    public static final String CHESS_EXCHANGE = "gameExchange";

    public static final String CHESS_GAME_ENDED_QUEUE = "platform.chess.game.ended.queue";
    public static final String CHESS_ACHIEVEMENT_QUEUE = "platform.chess.achievement.queue";
    public static final String CHESS_GAME_CREATED_QUEUE = "platform.chess.game.created.queue";
    public static final String CHESS_GAME_UPDATED_QUEUE = "platform.chess.game.updated.queue";

    public static final String GAME_REGISTERED_ROUTING_KEY = "game.registered";

    public static final String PLATFORM_GAME_REGISTERED_QUEUE = "platform.game.registered.queue";

    public static final String GAME_ENDED_KEY = "game.ended";
    public static final String ACHIEVEMENT_KEY = "achievement.acquired";
    public static final String GAME_CREATED_KEY = "game.created";
    public static final String GAME_UPDATED_KEY = "game.players.updated";

    public static final String ROUTING_KEY = "match.before.started";
    public static final String QUEUE = "match.before.started.queue";

    public static final String TTT_GAME_STARTED_QUEUE = "match.started.queue";
    public static final String TTT_MOVE_MADE_QUEUE    = "move.made.queue";
    public static final String TTT_GAME_ENDED_QUEUE   = "match.ended.queue";
    public static final String TTT_ACHIEVEMENT_QUEUE  = "achievement.achieved.queue";

    @Bean
    public TopicExchange gameplayExchange() {
        return new TopicExchange(EXCHANGE, true, false);
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
    public Queue matchBeforeStartedQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public Binding matchBeforeStartedBinding() {
        return BindingBuilder.bind(matchBeforeStartedQueue())
                .to(gameplayExchange())
                .with(ROUTING_KEY);
    }
    @Bean
    public Queue tttGameStartedQueue() {
        return QueueBuilder.durable(TTT_GAME_STARTED_QUEUE).build();
    }
    @Bean
    public Binding tttGameStartedBinding() {
        return BindingBuilder.bind(tttGameStartedQueue())
                .to(gameplayExchange())
                .with("match.started");
    }

    @Bean
    public Queue tttMoveMadeQueue() {
        return QueueBuilder.durable(TTT_MOVE_MADE_QUEUE).build();
    }
    @Bean
    public Binding tttMoveMadeBinding() {
        return BindingBuilder.bind(tttMoveMadeQueue())
                .to(gameplayExchange())
                .with("move.made");
    }

    @Bean
    public Queue tttGameEndedQueue() {
        return QueueBuilder.durable(TTT_GAME_ENDED_QUEUE).build();
    }
    @Bean
    public Binding tttGameEndedBinding() {
        return BindingBuilder.bind(tttGameEndedQueue())
                .to(gameplayExchange())
                .with("match.ended");
    }

    @Bean
    public Queue tttAchievementQueue() {
        return QueueBuilder.durable(TTT_ACHIEVEMENT_QUEUE).build();
    }
    @Bean
    public Binding tttAchievementBinding() {
        return BindingBuilder.bind(tttAchievementQueue())
                .to(gameplayExchange())
                .with("achievement.achieved");
    }
}
