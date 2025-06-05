
import { Game, Player, Stadium, GameResult, GameStatus } from '../types/models';

const API_BASE_URL = 'http://localhost:8080/api';

// Helper function to handle API responses
const handleResponse = async (response: Response) => {
  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || 'An error occurred');
  }
  return response.json();
};

// Stadium API
export const stadiumApi = {
  getAll: async (): Promise<Stadium[]> => {
    const response = await fetch(`${API_BASE_URL}/stadiums`);
    return handleResponse(response);
  },
  
  getById: async (id: number): Promise<Stadium> => {
    const response = await fetch(`${API_BASE_URL}/stadiums/${id}`);
    return handleResponse(response);
  },
  
  create: async (stadium: Stadium): Promise<Stadium> => {
    const response = await fetch(`${API_BASE_URL}/stadiums`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(stadium)
    });
    return handleResponse(response);
  },
  
  update: async (id: number, stadium: Stadium): Promise<Stadium> => {
    const response = await fetch(`${API_BASE_URL}/stadiums/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(stadium)
    });
    return handleResponse(response);
  },
  
  delete: async (id: number): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/stadiums/${id}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  },
  
  search: async (name: string): Promise<Stadium[]> => {
    const response = await fetch(`${API_BASE_URL}/stadiums/search?name=${encodeURIComponent(name)}`);
    return handleResponse(response);
  },
  
  getByMaxPrice: async (maxPrice: number): Promise<Stadium[]> => {
    const response = await fetch(`${API_BASE_URL}/stadiums/price?maxPrice=${maxPrice}`);
    return handleResponse(response);
  },
  
  getByMinCapacity: async (minCapacity: number): Promise<Stadium[]> => {
    const response = await fetch(`${API_BASE_URL}/stadiums/capacity?minCapacity=${minCapacity}`);
    return handleResponse(response);
  }
};

// Player API
export const playerApi = {
  getAll: async (): Promise<Player[]> => {
    const response = await fetch(`${API_BASE_URL}/players`);
    return handleResponse(response);
  },
  
  getById: async (id: number): Promise<Player> => {
    const response = await fetch(`${API_BASE_URL}/players/${id}`);
    return handleResponse(response);
  },
  
  create: async (player: Player): Promise<Player> => {
    const response = await fetch(`${API_BASE_URL}/players`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(player)
    });
    return handleResponse(response);
  },
  
  update: async (id: number, player: Player): Promise<Player> => {
    const response = await fetch(`${API_BASE_URL}/players/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(player)
    });
    return handleResponse(response);
  },
  
  delete: async (id: number): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/players/${id}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  },
  
  search: async (searchTerm: string): Promise<Player[]> => {
    const response = await fetch(`${API_BASE_URL}/players/search?searchTerm=${encodeURIComponent(searchTerm)}`);
    return handleResponse(response);
  },
  
  getFit: async (): Promise<Player[]> => {
    const response = await fetch(`${API_BASE_URL}/players/fit`);
    return handleResponse(response);
  },
  
  getActive: async (): Promise<Player[]> => {
    const response = await fetch(`${API_BASE_URL}/players/active`);
    return handleResponse(response);
  }
};

// Game API
export const gameApi = {
  // Updated: Combined search API with optional parameters
  getAll: async (ascending?: boolean, opponentTeam?: string, date?: string): Promise<Game[]> => {
    // Build query string with available parameters
    const params = new URLSearchParams();
    if (ascending !== undefined) {
      params.append('ascending', ascending.toString());
    }
    if (opponentTeam) {
      params.append('opponentTeam', opponentTeam);
    }
    if (date) {
      params.append('date', date);
    }
    
    const queryString = params.toString();
    const url = `${API_BASE_URL}/games/search${queryString ? '?' + queryString : ''}`;
    
    const response = await fetch(url);
    return handleResponse(response);
  },
  
  getById: async (id: number): Promise<Game> => {
    const response = await fetch(`${API_BASE_URL}/games/${id}`);
    return handleResponse(response);
  },
  
  create: async (game: Game): Promise<Game> => {
    const response = await fetch(`${API_BASE_URL}/games`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(game)
    });
    return handleResponse(response);
  },
  
  update: async (id: number, game: Game): Promise<Game> => {
    const response = await fetch(`${API_BASE_URL}/games/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(game)
    });
    return handleResponse(response);
  },
  
  delete: async (id: number): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/games/${id}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  },
  
  addPlayer: async (gameId: number, playerId: number): Promise<Game> => {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}/players/${playerId}`, {
      method: 'POST'
    });
    return handleResponse(response);
  },
  
  removePlayer: async (gameId: number, playerId: number): Promise<Game> => {
    const response = await fetch(`${API_BASE_URL}/games/${gameId}/players/${playerId}`, {
      method: 'DELETE'
    });
    return handleResponse(response);
  },
  
  getByStadium: async (stadiumId: number): Promise<Game[]> => {
    const response = await fetch(`${API_BASE_URL}/games/stadium/${stadiumId}`);
    return handleResponse(response);
  },
  
  // These methods will now use the combined search API
  searchByOpponent: async (opponentTeam: string, date?: string, ascending?: boolean): Promise<Game[]> => {
    return gameApi.getAll(ascending, opponentTeam, date);
  },
  
  getScheduled: async (): Promise<Game[]> => {
    // This still needs a dedicated endpoint as it filters by status
    const response = await fetch(`${API_BASE_URL}/games/scheduled`);
    return handleResponse(response);
  },
  
  getByResult: async (result: GameResult): Promise<Game[]> => {
    const response = await fetch(`${API_BASE_URL}/games/result?result=${result}`);
    return handleResponse(response);
  },
  
  getByPlayer: async (playerId: number): Promise<Game[]> => {
    const response = await fetch(`${API_BASE_URL}/games/player/${playerId}`);
    return handleResponse(response);
  },
  
  getByDateRange: async (start: string, end: string): Promise<Game[]> => {
    const response = await fetch(
      `${API_BASE_URL}/games/date-range?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`
    );
    return handleResponse(response);
  },
  
  getGamesByResultCategory: async (): Promise<Record<string, Game[]>> => {
    const response = await fetch(`${API_BASE_URL}/games/categories`);
    return handleResponse(response);
  }
};
