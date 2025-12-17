package be.kdg.player.adapter.out;

import be.kdg.player.port.out.SaveGameLibraryPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class GameLibraryJpaAdapter implements SaveGameLibraryPort {

    private final GameLibraryJpaRepository gameLibraryRepository;
    private final PlayerJpaRepository playerRepository;

    public GameLibraryJpaAdapter(GameLibraryJpaRepository gameLibraryRepository,
                                 PlayerJpaRepository playerRepository) {
        this.gameLibraryRepository = gameLibraryRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public void addPurchasedGame(UUID playerId, UUID gameId, String paymentIntentId) {
        PlayerJpaEntity player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        GameLibraryJpaEntity libraryEntry = new GameLibraryJpaEntity(
                UUID.randomUUID(),
                gameId,
                LocalDateTime.now(),
                null,
                0L,
                false,
                LocalDateTime.now(),
                paymentIntentId,
                player
        );

        gameLibraryRepository.save(libraryEntry);
        System.out.println(" Saved to DB - Player: " + playerId + ", Game: " + gameId + ", PaymentIntent: " + paymentIntentId);
    }
}