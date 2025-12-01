# Game Creation API Documentation

## Overview

This API endpoint allows you to create a new game along with its associated achievements in a single request. The endpoint handles both game metadata and achievement definitions together.

---

## Endpoint Details

**URL:** `/api/games`

**Method:** `POST`

**Content-Type:** `application/json`

**Accept:** `application/json`

---

## Request Body Structure

### Game Object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | String | Yes | The name of the game |
| `rules` | String | Yes | Brief description of the game rules |
| `pictureUrl` | String | Yes | URL to the game's cover image or thumbnail |
| `gameUrl` | String | Yes | URL where the game can be accessed or played |
| `category` | String | Yes | Game category (e.g., "Arcade", "Adventure", "Puzzle") |
| `developedBy` | String | Yes | Name of the developer or development studio |
| `createdAt` | String (Date) | Yes | Game creation date in `YYYY-MM-DD` format |
| `averageMinutes` | Integer | Yes | Average playtime in minutes |
| `achievements` | Array | Yes | List of achievement definitions (see below) |

### Achievement Definition Object

Each achievement in the `achievements` array should contain:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | String | Yes | The name of the achievement |
| `description` | String | Yes | A brief description of what the achievement represents |
| `howToUnlock` | String | Yes | Instructions or conditions for unlocking this achievement |

---

## Example Request

```http
POST http://localhost:8080/api/games
Content-Type: application/json
Accept: application/json
```

```json
{
  "name": "Space Shooter",
  "rules": "Shoot enemies",
  "pictureUrl": "http://example.com/img",
  "gameUrl": "http://example.com/game",
  "category": "Arcade",
  "developedBy": "Me",
  "createdAt": "2024-01-01",
  "averageMinutes": 15,
  "achievements": [
    {
      "name": "First Kill",
      "description": "Kill your first enemy",
      "howToUnlock": "Perform any kill"
    },
    {
      "name": "Boss Defeated",
      "description": "Kill the first boss",
      "howToUnlock": "Beat level 1"
    }
  ]
}
```

---

## Response

### Success Response

**Status Code:** `200 OK`

**Response Body:** Returns a `GameDto` object containing the created game with all its details and achievements.

```json
{
  "id": "game-uuid",
  "name": "Space Shooter",
  "rules": "Shoot enemies",
  "pictureUrl": "http://example.com/img",
  "gameUrl": "http://example.com/game",
  "category": "Arcade",
  "developedBy": "Me",
  "createdAt": "2024-01-01",
  "averageMinutes": 15,
  "achievements": [
    {
      "id": "achievement-uuid-1",
      "name": "First Kill",
      "description": "Kill your first enemy",
      "howToUnlock": "Perform any kill"
    },
    {
      "id": "achievement-uuid-2",
      "name": "Boss Defeated",
      "description": "Kill the first boss",
      "howToUnlock": "Beat level 1"
    }
  ]
}
```

---

## Notes

- **Date Format:** The `createdAt` field must follow the ISO 8601 date format (`YYYY-MM-DD`)
- **Achievements:** At least one achievement should be provided, though the endpoint may accept an empty array depending on business rules
- **URLs:** Both `pictureUrl` and `gameUrl` should be valid, accessible URLs
- **Average Minutes:** This should represent typical playtime for a single session or completion

---