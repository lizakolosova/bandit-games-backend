package be.kdg.platform.core;

import static org.junit.jupiter.api.Assertions.*;


import be.kdg.common.events.chess.AchievementEntry;
import be.kdg.common.exception.InvalidRowException;
import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.command.RegisterChessGameProjectionCommand;
import be.kdg.platform.port.out.AddGamePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ChessPlatformProjectorImplTest {

    private AddGamePort addGamePort;
    private PlatformEventPublisher eventPublisher;
    private ChessPlatformProjectorImpl sut;

    @BeforeEach
    void setup() {
        addGamePort = mock(AddGamePort.class);
        eventPublisher = mock(PlatformEventPublisher.class);
        sut = new ChessPlatformProjectorImpl(addGamePort, eventPublisher);
    }

    @Test
    void shouldCreateAndSaveGame_WhenValidCommand() {
        // Arrange
        UUID id = UUID.randomUUID();
        var achievements = List.of(
                new AchievementEntry("CODE1", "Description 1"),
                new AchievementEntry("CODE2", "Description 2")
        );
        var cmd = new RegisterChessGameProjectionCommand(
                id,
                "https://frontend",
                "https://picture",
                achievements,
                LocalDateTime.now()
        );

        // Act
        sut.project(cmd);

        // Assert
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(addGamePort).add(captor.capture());

        Game saved = captor.getValue();
        assertEquals(id, saved.getGameId().uuid());
        assertEquals(2, saved.getAchievements().size());
    }

    @Test
    void shouldPublishDomainEvents() {
        // Arrange
        var cmd = new RegisterChessGameProjectionCommand(
                UUID.randomUUID(), "f", "p", List.of(), LocalDateTime.now()
        );

        // Act
        sut.project(cmd);

        // Assert
        verify(eventPublisher).publishEvents(any());
    }

    @Test
    void shouldThrowWhenCommandIsNull() {
        // Arrange

        // Act + Assert
        assertThrows(NullPointerException.class, () -> sut.project(null));
    }

    @Test
    void shouldAcceptNullAchievementsList() {
        // Arrange
        var cmd = new RegisterChessGameProjectionCommand(
                UUID.randomUUID(), "f", "p", null, LocalDateTime.now()
        );

        // Act
        sut.project(cmd);

        // Assert
        verify(addGamePort).add(any(Game.class));
    }

    @Test
    void shouldThrowWhenAchievementHasNullCode() {
        // Arrange
        var cmd = new RegisterChessGameProjectionCommand(
                UUID.randomUUID(),
                "f",
                "p",
                List.of(new AchievementEntry(null, "desc")),
                LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(InvalidRowException.class, () -> sut.project(cmd));
    }

    @Test
    void shouldPropagateExceptionWhenAddGamePortFails() {
        // Arrange
        doThrow(new RuntimeException("DB error")).when(addGamePort).add(any());
        var cmd = new RegisterChessGameProjectionCommand(
                UUID.randomUUID(), "f", "p", List.of(), LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(RuntimeException.class, () -> sut.project(cmd));
    }

    @Test
    void shouldNotSaveGameWhenEventPublisherFails() {
        // Arrange
        doThrow(new RuntimeException("Kafka error")).when(eventPublisher).publishEvents(any());
        var cmd = new RegisterChessGameProjectionCommand(
                UUID.randomUUID(), "f", "p", List.of(), LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(RuntimeException.class, () -> sut.project(cmd));
        verify(addGamePort, never()).add(any());
    }

    @Test
    void shouldThrowWhenFrontendUrlIsNull() {
        // Arrange
        var cmd = new RegisterChessGameProjectionCommand(
                UUID.randomUUID(), null, "picture", List.of(), LocalDateTime.now()
        );

        // Act + Assert
        assertThrows(NullPointerException.class, () -> sut.project(cmd));
    }
}