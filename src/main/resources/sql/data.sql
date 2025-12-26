
-- Sample data for testing frontend with backend
-- Based on actual JPA entities

-- ============================================
-- PLATFORM SCHEMA - Games and Achievements
-- ============================================

-- Insert sample games
INSERT INTO kdg_platform.games (uuid, name, rules, picture_url, category, game_url, average_minutes, developed_by, created_at, approved)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Connect Four',
     'Players take turns dropping colored discs into a seven-column, six-row grid. The objective is to be the first to form a horizontal, vertical, or diagonal line of four of one''s own discs.',
     'https://images.unsplash.com/photo-1611996575749-79a3a250f948?w=800',
     'Strategy',
     'http://localhost:3000/games/connect4',
     15,
     'Hasbro Gaming',
     '1974-01-01',
     true),

    ('550e8400-e29b-41d4-a716-446655440002', 'Tic Tac Toe',
     'Two players take turns marking spaces in a 3×3 grid. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row wins.',
     'https://images.unsplash.com/photo-1566694271453-390536dd1f0d?w=800',
     'Puzzle',
     'http://localhost:5174/',
     5,
     'Classic Games Inc',
     '1952-01-01',
     true),

    ('550e8400-e29b-41d4-a716-446655440003', 'Checkers',
     'Players move their pieces diagonally on a checkered board. Capture opponent pieces by jumping over them. Win by capturing all opponent pieces or blocking their moves.',
     'https://images.unsplash.com/photo-1528819622765-d6bcf132f793?w=800',
     'Strategy',
     'http://localhost:3000/games/checkers',
     20,
     'Traditional Games Ltd',
     '1400-01-01',
     true),

    ('550e8400-e29b-41d4-a716-446655440004', 'Reversi',
     'Place discs on an 8×8 board to outflank and flip opponent pieces. The player with the most pieces of their color at the end wins.',
     'https://images.unsplash.com/photo-1566694271453-390536dd1f0d?w=800',
     'Strategy',
     'http://localhost:3000/games/reversi',
     25,
     'Board Game Masters',
     '1883-01-01',
     true),

    ('550e8400-e29b-41d4-a716-446655440005', 'Dots and Boxes',
     'Players take turns connecting dots on a grid. Complete a box to claim it and earn another turn. The player with the most boxes wins.',
     'https://images.unsplash.com/photo-1611996575749-79a3a250f948?w=800',
     'Puzzle',
     'http://localhost:3000/games/dotsandboxes',
     10,
     'Pen & Paper Games',
     '1889-01-01',
     true),

    ('550e8400-e29b-41d4-a716-446655440006', 'Gomoku',
     'Players alternate placing stones on a 15×15 or 19×19 grid. The first to get exactly five stones in a row (horizontally, vertically, or diagonally) wins.',
     'https://images.unsplash.com/photo-1528819622765-d6bcf132f793?w=800',
     'Strategy',
     'http://localhost:3000/games/gomoku',
     18,
     'Asian Board Games Co',
     '1899-01-01',
     true)
ON CONFLICT (uuid) DO NOTHING;

-- Insert achievements for Connect Four
INSERT INTO kdg_platform.achievement_definitions (uuid, game_id, name, description)
VALUES
    ('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'First Win', 'Win your first game'),
    ('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', 'Speed Demon', 'Win a game in under 2 minutes'),
    ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', 'Strategist', 'Win without letting opponent connect 3'),
    ('650e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440001', '10 Wins', 'Win 10 games'),
    ('650e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440001', 'Unbeatable', 'Win 5 games in a row')
ON CONFLICT (uuid) DO NOTHING;

-- Insert achievements for Tic Tac Toe
INSERT INTO kdg_platform.achievement_definitions (uuid, game_id, name, description)
VALUES
    ('650e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440002', 'First Victory', 'Win your first game'),
    ('650e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440002', 'Perfect Game', 'Win without opponent scoring'),
    ('650e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440002', 'Lightning Fast', 'Win in 5 moves or less')
ON CONFLICT (uuid) DO NOTHING;

-- Insert achievements for other games
INSERT INTO kdg_platform.achievement_definitions (uuid, game_id, name, description)
VALUES
    ('650e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440003', 'King Me', 'Get your first king piece'),
    ('650e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440003', 'Sweep Victory', 'Capture all opponent pieces'),
    ('650e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440004', 'Corner Control', 'Capture all four corners'),
    ('650e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440004', 'Flip Master', 'Flip 20+ pieces in one move'),
    ('650e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440005', 'Box Hoarder', 'Claim 10 boxes in one game'),
    ('650e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440006', 'Five in a Row', 'Get your first five in a row')
ON CONFLICT (uuid) DO NOTHING;

-- ============================================
-- PLAYER SCHEMA - Sample Players
-- ============================================

-- Insert sample players
-- NOTE: Replace these UUIDs with actual Keycloak user IDs from your realm
INSERT INTO kdg_player.player (uuid, username, email, picture_url, created_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440001', 'HogCranker', 'hogcranker@example.com', 'https://i.pravatar.cc/150?img=1', CURRENT_TIMESTAMP),
    ('57423a96-12fb-49d7-b2f3-78c3be67bdaa', 'brocode', 'brocode@gmail.com', 'https://i.pravatar.cc/150?img=2', CURRENT_TIMESTAMP),
    ('750e8400-e29b-41d4-a716-446655440003', 'GameMaster', 'gamemaster@example.com', 'https://i.pravatar.cc/150?img=3', CURRENT_TIMESTAMP),
    ('750e8400-e29b-41d4-a716-446655440004', 'ProGamer99', 'progamer99@example.com', 'https://i.pravatar.cc/150?img=4', CURRENT_TIMESTAMP),

    -- >> ADD THE NEW KEYCLOAK IDS HERE <<
    ('5c739cda-1d85-4c9c-ba9b-7da5518b25ae', 'HotMinionPics', 'friendA@example.com', 'https://wallpapers.com/images/hd/funny-profile-picture-1l2l3tmmbobjqd53.jpg', CURRENT_TIMESTAMP),
    ('e6846a08-8808-420b-b23d-4f09698a2969', 'HogCranker', 'friendB@example.com', 'https://wallpapers.com/images/hd/funny-profile-picture-1l2l3tmmbobjqd53.jpg', CURRENT_TIMESTAMP),
    ('49aee311-9a26-4315-ac85-3c971573af3f', 'Tower1', 'friendC@example.com', 'https://wallpapers.com/images/hd/funny-profile-picture-1l2l3tmmbobjqd53.jpg', CURRENT_TIMESTAMP),
    ('e7b0794b-a64a-4fd4-bd0d-a80186de2953', 'Tower2', 'friendD@example.com', 'https://wallpapers.com/images/hd/funny-profile-picture-1l2l3tmmbobjqd53.jpg', CURRENT_TIMESTAMP)
ON CONFLICT (uuid) DO NOTHING;
-- ============================================
-- PLAYER SCHEMA - Game Library
-- ============================================

-- Add games to player libraries (using player_game_library table)
INSERT INTO kdg_player.player_game_library (id, player_id, game_id, added_at, last_played_at, total_playtime_seconds, favourite, games_lost, games_won, games_draw, matches_played)
VALUES
    -- HogCranker's library
    ('850e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 7200, true,0,0,0,0),
    ('850e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 2700, true,0,0,0,0),
    ('850e8400-e29b-41d4-a716-446655440003', '750e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '5 days', 4800, false,0,0,0,0),

    -- Player123's library
    ('850e8400-e29b-41d4-a716-446655440004', '57423a96-12fb-49d7-b2f3-78c3be67bdaa', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '1 hour', 5400, true,0,0,0,0),
    ('850e8400-e29b-41d4-a716-446655440005', '57423a96-12fb-49d7-b2f3-78c3be67bdaa', '550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '3 hours', 3600, false,0,0,0,0)
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- PLAYER SCHEMA - Game Projections
-- ============================================

-- Insert game projections (denormalized view for player service)
INSERT INTO kdg_player.game_projection (game_id, name, picture_url, category, rules, achievement_count, average_minutes, developed_by)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'Connect Four', 'https://images.unsplash.com/photo-1611996575749-79a3a250f948?w=800', 'Strategy', 'Players take turns dropping colored discs into a seven-column, six-row grid. The objective is to be the first to form a horizontal, vertical, or diagonal line of four of one''s own discs.', 5, 15, 'Hasbro Gaming'),
    ('550e8400-e29b-41d4-a716-446655440002', 'Tic Tac Toe', 'https://images.unsplash.com/photo-1566694271453-390536dd1f0d?w=800', 'Puzzle', 'Two players take turns marking spaces in a 3×3 grid. The player who succeeds in placing three of their marks in a horizontal, vertical, or diagonal row wins.', 3, 5, 'Classic Games Inc'),
    ('550e8400-e29b-41d4-a716-446655440003', 'Checkers', 'https://images.unsplash.com/photo-1528819622765-d6bcf132f793?w=800', 'Strategy', 'Players move their pieces diagonally on a checkered board. Capture opponent pieces by jumping over them. Win by capturing all opponent pieces or blocking their moves.', 2, 20, 'Traditional Games Ltd'),
    ('550e8400-e29b-41d4-a716-446655440004', 'Reversi', 'https://images.unsplash.com/photo-1566694271453-390536dd1f0d?w=800', 'Strategy', 'Place discs on an 8×8 board to outflank and flip opponent pieces. The player with the most pieces of their color at the end wins.', 2, 25, 'Board Game Masters'),
    ('550e8400-e29b-41d4-a716-446655440005', 'Dots and Boxes', 'https://images.unsplash.com/photo-1611996575749-79a3a250f948?w=800', 'Puzzle', 'Players take turns connecting dots on a grid. Complete a box to claim it and earn another turn. The player with the most boxes wins.', 1, 10, 'Pen & Paper Games'),
    ('550e8400-e29b-41d4-a716-446655440006', 'Gomoku', 'https://images.unsplash.com/photo-1528819622765-d6bcf132f793?w=800', 'Strategy', 'Players alternate placing stones on a 15×15 or 19×19 grid. The first to get exactly five stones in a row (horizontally, vertically, or diagonally) wins.', 1, 18, 'Asian Board Games Co')
ON CONFLICT (game_id) DO NOTHING;

INSERT INTO kdg_player.game_projection (game_id, name, picture_url, category, rules, achievement_count, average_minutes, developed_by)
VALUES
    ('8496c496-a884-48ed-9bb3-7c3aa50fb8ca', 'Your Game Name', 'https://example.com/pic.png', 'Strategy', 'Rules here...', 8, 30, 'Developer Name');


-- ============================================
-- PLAYER SCHEMA - Unlocked Achievements
-- ============================================

-- Unlock some achievements for HogCranker
INSERT INTO kdg_player.player_achievement (uuid, player_id, achievement_id, game_id, unlocked_at)
VALUES
    ('950e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    ('950e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '20 days'),
    ('950e8400-e29b-41d4-a716-446655440003', '750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '15 days')
ON CONFLICT (uuid) DO NOTHING;

-- Unlock achievements for Player123
INSERT INTO kdg_player.player_achievement (uuid, player_id, achievement_id, game_id, unlocked_at)
VALUES
    ('950e8400-e29b-41d4-a716-446655440004', '57423a96-12fb-49d7-b2f3-78c3be67bdaa', '650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '8 days'),
    ('950e8400-e29b-41d4-a716-446655440005', '57423a96-12fb-49d7-b2f3-78c3be67bdaa', '650e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP - INTERVAL '4 days')
ON CONFLICT (uuid) DO NOTHING;

-- ============================================
-- PLAYER SCHEMA - Friends (ElementCollection)
-- ============================================

-- Insert friends using the ElementCollection table
INSERT INTO kdg_player.player_friends (player_id, friend_id, since)
VALUES
    ('57423a96-12fb-49d7-b2f3-78c3be67bdaa', '5c739cda-1d85-4c9c-ba9b-7da5518b25ae', '2025-01-01 10:00:00'),
    ('57423a96-12fb-49d7-b2f3-78c3be67bdaa', 'e6846a08-8808-420b-b23d-4f09698a2969', '2025-01-02 10:00:00'),
    ('57423a96-12fb-49d7-b2f3-78c3be67bdaa', 'e7b0794b-a64a-4fd4-bd0d-a80186de2953', '2025-01-03 10:00:00');


-- ============================================
-- PLAYER SCHEMA - Friendship Requests
-- ============================================

-- Insert some pending friendship requests
INSERT INTO kdg_player.friendship_request (uuid, sender_id, receiver_id, status, created_at)
VALUES
    ('a50e8400-e29b-41d4-a716-446655440001', '750e8400-e29b-41d4-a716-446655440004', '750e8400-e29b-41d4-a716-446655440001', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ('a50e8400-e29b-41d4-a716-446655440002', '750e8400-e29b-41d4-a716-446655440003', '750e8400-e29b-41d4-a716-446655440002', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '1 day')
ON CONFLICT (uuid) DO NOTHING;

-- ============================================
-- GAMEPLAY SCHEMA - Sample Matches
-- ============================================

INSERT INTO kdg_player.achievement_projection (achievement_id, description, name, game_id) VALUES
                                                                                               ('94c53f71-289e-4639-b553-50316ab7898b', 'Capture your opponent''s first piece', 'FIRST_BLOOD', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('e042db94-6eb8-4835-ab7a-95af0e4d3ebd', 'Promoted a pawn', 'PAWN_POWER', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('795b0095-e68f-4fdd-a85a-4f9fbb34632a', 'Win in under 20 moves', 'SPEEDY_VICTORY', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('64ba8396-d1a0-4f67-aa1a-1e3b83069968', 'Make a move in under 5 second', 'SPEED_DEMON', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('81800354-2d93-4a93-a392-2520dc80f20c', 'Winner winner chicken dinner', 'WINNER_WINNER_CHICKEN_DINNER', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('c2c6fc7d-a6e7-402d-bcb0-e925237a4532', 'Castle kingside or queenside', 'CASTLE_TIME', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('77be0fa9-da6e-4229-bcac-6274ff534990', 'Move your rook for the first time', 'ROOKIE_MOVE', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca'),
                                                                                               ('05b6c2f1-14d6-49bc-8841-71845cfc522e', 'Make 3 pawn moves in a row', 'PAWN_STORM', '8496c496-a884-48ed-9bb3-7c3aa50fb8ca');


