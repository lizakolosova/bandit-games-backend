package be.kdg;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.platform.adapter.out.AchievementDefinitionJpaRepository;
import be.kdg.platform.adapter.out.GameJpaRepository;
import be.kdg.player.domain.Player;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TestHelper {
    @Autowired
    private GameJpaRepository games;
    @Autowired
    private AchievementDefinitionJpaRepository achievements;
    @Autowired
    private  DataSource dataSource;


    @PostConstruct
    public void createSchemas() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS kdg_platform");
            stmt.execute("CREATE SCHEMA IF NOT EXISTS kdg_player");

        } catch (Exception e) {
            throw new RuntimeException("Failed to create schemas", e);
        }
    }

    public void cleanUp() {
        games.deleteAll();
        achievements.deleteAll();
    }

    public Player createDummyPlayer(UUID id) {
        return new Player(
                PlayerId.of(id),
                "username-" + id.toString().substring(0, 5),
                id.toString() + "@mail.com",
                "pic.png",
                LocalDateTime.now()
        );
    }
}

