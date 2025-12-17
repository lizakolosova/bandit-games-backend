CREATE SCHEMA IF NOT EXISTS kdg_platform;
CREATE SCHEMA IF NOT EXISTS kdg_player;
CREATE SCHEMA IF NOT EXISTS kdg_gameplay;

-- Games table
CREATE TABLE IF NOT EXISTS kdg_platform.games (
                                                  uuid UUID PRIMARY KEY,
                                                  name VARCHAR(255) NOT NULL,
                                                  rules TEXT,
                                                  picture_url VARCHAR(500),
                                                  game_url VARCHAR(500) NOT NULL,
                                                  category VARCHAR(100),
                                                  developed_by VARCHAR(255),
                                                  created_at DATE NOT NULL,
                                                  average_minutes INTEGER,
                                                  approved BOOLEAN NOT NULL DEFAULT false
);

-- Achievement Definitions table
CREATE TABLE IF NOT EXISTS kdg_platform.achievement_definitions (
                                                                    uuid UUID PRIMARY KEY,
                                                                    name VARCHAR(255) NOT NULL,
                                                                    description TEXT,
                                                                    game_id UUID NOT NULL,
                                                                    CONSTRAINT fk_achievement_game FOREIGN KEY (game_id)
                                                                        REFERENCES kdg_platform.games(uuid)
                                                                        ON DELETE CASCADE
);

-- Player table
CREATE TABLE IF NOT EXISTS kdg_player.player (
                                                 uuid UUID PRIMARY KEY,
                                                 username VARCHAR(255) NOT NULL,
                                                 email VARCHAR(255) NOT NULL,
                                                 picture_url VARCHAR(500),
                                                 created_at TIMESTAMP NOT NULL
);

-- Game Projection table
CREATE TABLE IF NOT EXISTS kdg_player.game_projection (
                                                          game_id UUID PRIMARY KEY,
                                                          name VARCHAR(255),
                                                          picture_url VARCHAR(500),
                                                          category VARCHAR(100),
                                                          rules TEXT,
                                                          achievement_count INTEGER,
                                                          average_minutes INTEGER,
                                                          developed_by VARCHAR(255),
                                                          price DECIMAL(10, 2)
);

-- Achievement Projection table
CREATE TABLE IF NOT EXISTS kdg_player.achievement_projection (
                                                                 achievement_id UUID PRIMARY KEY,
                                                                 name VARCHAR(255),
                                                                 description TEXT,
                                                                 game_id UUID NOT NULL,
                                                                 CONSTRAINT fk_achievement_proj_game FOREIGN KEY (game_id)
                                                                     REFERENCES kdg_player.game_projection(game_id)
                                                                     ON DELETE CASCADE
);

-- Player Game Library table
CREATE TABLE IF NOT EXISTS kdg_player.player_game_library (
                                                              id UUID PRIMARY KEY,
                                                              player_id UUID NOT NULL,
                                                              game_id UUID,
                                                              added_at TIMESTAMP,
                                                              last_played_at TIMESTAMP,
                                                              total_playtime_seconds BIGINT,
                                                              favourite BOOLEAN,
                                                              purchased_at TIMESTAMP,
                                                              stripe_payment_intent_id VARCHAR(255),
                                                              matches_played INTEGER DEFAULT 0,
                                                              games_won INTEGER DEFAULT 0,
                                                              games_lost INTEGER DEFAULT 0,
                                                              games_draw INTEGER DEFAULT 0,
                                                              CONSTRAINT fk_library_player FOREIGN KEY (player_id)
                                                                  REFERENCES kdg_player.player(uuid)
                                                                  ON DELETE CASCADE
);

-- Player Achievement table
CREATE TABLE IF NOT EXISTS kdg_player.player_achievement (
                                                             uuid UUID PRIMARY KEY,
                                                             player_id UUID NOT NULL,
                                                             achievement_id UUID,
                                                             game_id UUID,
                                                             unlocked_at TIMESTAMP,
                                                             CONSTRAINT fk_player_achievement_player FOREIGN KEY (player_id)
                                                                 REFERENCES kdg_player.player(uuid)
                                                                 ON DELETE CASCADE
);

-- Player Friends table (ElementCollection)
CREATE TABLE IF NOT EXISTS kdg_player.player_friends (
                                                         player_id UUID NOT NULL,
                                                         friend_id UUID,
                                                         since TIMESTAMP,
                                                         CONSTRAINT fk_player_friends_player FOREIGN KEY (player_id)
                                                             REFERENCES kdg_player.player(uuid)
                                                             ON DELETE CASCADE
);

-- Friendship Request table
CREATE TABLE IF NOT EXISTS kdg_player.friendship_request (
                                                             uuid UUID PRIMARY KEY,
                                                             sender_id UUID,
                                                             receiver_id UUID,
                                                             status VARCHAR(50),
                                                             created_at TIMESTAMP
);

-- Game View Projection table
CREATE TABLE IF NOT EXISTS kdg_gameplay.game_view_projection (
                                                                 game_id UUID PRIMARY KEY,
                                                                 name VARCHAR(255)
);

-- Game Room table
CREATE TABLE IF NOT EXISTS kdg_gameplay.game_room (
                                                      uuid UUID PRIMARY KEY,
                                                      game_id UUID,
                                                      host_player_name VARCHAR(255),
                                                      invited_player_name VARCHAR(255),
                                                      host_player_id UUID,
                                                      invited_player_id UUID,
                                                      game_room_type VARCHAR(50),
                                                      status VARCHAR(50),
                                                      created_at TIMESTAMP,
                                                      invitation_status VARCHAR(50)
);

-- Match table
CREATE TABLE IF NOT EXISTS kdg_gameplay.match (
                                                  match_id UUID PRIMARY KEY,
                                                  game_id UUID,
                                                  status VARCHAR(50),
                                                  started_at TIMESTAMP,
                                                  finished_at TIMESTAMP,
                                                  total_moves INTEGER DEFAULT 0,
                                                  end_reason VARCHAR(255),
                                                  winner_player_id UUID
);

-- Match Players table (ElementCollection with @OrderColumn)
CREATE TABLE IF NOT EXISTS kdg_gameplay.match_players (
                                                          match_id UUID NOT NULL,
                                                          player_id UUID,
                                                          player_order INTEGER,
                                                          CONSTRAINT fk_match_players_match FOREIGN KEY (match_id)
                                                              REFERENCES kdg_gameplay.match(match_id)
                                                              ON DELETE CASCADE
);