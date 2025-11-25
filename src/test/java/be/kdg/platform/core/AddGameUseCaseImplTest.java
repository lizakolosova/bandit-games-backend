package be.kdg.platform.core;

import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.AddGameCommand;
import be.kdg.platform.port.out.AddGamePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddGameUseCaseImplTest {

    @Mock
    private AddGamePort addGamePort;

    @Mock
    private PlatformEventPublisher eventPublisher;

    @InjectMocks
    private AddGameUseCaseImpl useCase;

    @Test
    void shouldCreateGamePersistItAndPublishEvents() {
        // arrange
        String name = "Test Game";
        String rules = "Some rules";
        String pictureUrl = "http://example.com/pic.png";
        String gameUrl = "http://example.com/game";
        String category = "Puzzle";
        String developedBy = "Test Studio";
        LocalDate createdAt = LocalDate.now();
        int averageMinutes = 60;

        AddGameCommand command = new AddGameCommand(
                name,
                rules,
                pictureUrl,
                gameUrl,
                category,
                developedBy,
                createdAt,
                averageMinutes
        );

        when(addGamePort.add(any(Game.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

        // act
        Game result = useCase.createGame(command);

        // assert
        verify(addGamePort).add(gameCaptor.capture());
        verify(eventPublisher).publishEvents(any());

        Game savedGame = gameCaptor.getValue();
        assertNotNull(savedGame);

        assertEquals(name, savedGame.getName());
        assertEquals(rules, savedGame.getRules());
        assertEquals(pictureUrl, savedGame.getPictureUrl());
        assertEquals(gameUrl, savedGame.getGameUrl());
        assertEquals(category, savedGame.getCategory());
        assertEquals(developedBy, savedGame.getDevelopedBy());
        assertEquals(createdAt, savedGame.getCreatedAt());
        assertEquals(averageMinutes, savedGame.getAverageMinutes());
        assertSame(savedGame, result);
    }
}