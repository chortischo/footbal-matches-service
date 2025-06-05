# Football Match Management System

A Spring Boot application for managing football matches, players, and stadiums.

## Features

### Player Management
- Add, delete, and update player information
- View player details and list all players
- Search players by name
- Track player status and health

### Game Management
- Schedule and manage football matches
- Add/remove players to/from games
- Update game details (date, location, attendance)
- Record game results
- View games by various criteria (date, result, stadium)

### Stadium Management
- Add, delete, and update stadium information
- Track stadium capacity and pricing
- View scheduled games at each stadium

## Technical Stack
- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- Maven

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. Access the application at `http://localhost:8080`

### H2 Database Console
- Access the H2 console at `http://localhost:8080/h2-console`
- Use the following connection details:
  - JDBC URL: `jdbc:h2:mem:footballdb`
  - Username: `sa`
  - Password: `password`

## API Endpoints

### Players
- `GET /api/players` - Get all players
- `GET /api/players/{id}` - Get player by ID
- `POST /api/players` - Add new player
- `PUT /api/players/{id}` - Update player
- `DELETE /api/players/{id}` - Delete player
- `GET /api/players/search?searchTerm={term}` - Search players
- `GET /api/players/active` - Get active players
- `GET /api/players/fit` - Get fit players

### Games
- `GET /api/games` - Get all games
- `GET /api/games/{id}` - Get game by ID
- `POST /api/games` - Add new game
- `PUT /api/games/{id}` - Update game
- `DELETE /api/games/{id}` - Delete game
- `GET /api/games/date-range` - Get games by date range
- `GET /api/games/search` - Search games by opponent
- `GET /api/games/result` - Get games by result
- `GET /api/games/scheduled` - Get scheduled games
- `GET /api/games/stadium/{stadiumId}` - Get games by stadium
- `GET /api/games/player/{playerId}` - Get games by player
- `POST /api/games/{gameId}/players/{playerId}` - Add player to game
- `DELETE /api/games/{gameId}/players/{playerId}` - Remove player from game

### Stadiums
- `GET /api/stadiums` - Get all stadiums
- `GET /api/stadiums/{id}` - Get stadium by ID
- `POST /api/stadiums` - Add new stadium
- `PUT /api/stadiums/{id}` - Update stadium
- `DELETE /api/stadiums/{id}` - Delete stadium
- `GET /api/stadiums/search` - Search stadiums by name
- `GET /api/stadiums/capacity` - Get stadiums by minimum capacity
- `GET /api/stadiums/price` - Get stadiums by maximum price per seat 