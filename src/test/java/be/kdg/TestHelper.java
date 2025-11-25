package be.kdg;

import be.kdg.platform.adapter.out.AchievementDefinitionJpaRepository;
import be.kdg.platform.adapter.out.GameJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

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
}

