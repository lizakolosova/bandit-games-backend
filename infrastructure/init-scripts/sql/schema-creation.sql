CREATE SCHEMA kdg_platform;
CREATE SCHEMA kdg_player;
CREATE SCHEMA kdg_gameplay;
ALTER TABLE kdg_gameplay.match_players
    ADD COLUMN player_order INT NOT NULL DEFAULT 0;