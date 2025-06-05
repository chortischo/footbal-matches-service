
export type Stadium = {
  id?: number;
  name: string;
  capacity: number;
  pricePerSeat: number;
  games?: Game[];
};

export enum PlayerStatus {
  ACTIVE = "ACTIVE",
  INACTIVE = "INACTIVE",
  SUSPENDED = "SUSPENDED"
}

export enum HealthStatus {
  FIT = "FIT",
  INJURED = "INJURED",
  RECOVERING = "RECOVERING"
}

export type Player = {
  id?: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string; // ISO format date
  status: PlayerStatus;
  healthStatus: HealthStatus;
  salary: number;
};

export enum GameResult {
  WIN = "WIN",
  LOSS = "LOSS",
  DRAW = "DRAW",
  NOT_PLAYED = "NOT_PLAYED"
}

// Updated result categories to match API response
export type ResultCategories = {
  WON: Game[];
  LOST: Game[];
  DRAW: Game[];
  NOT_PLAYED: Game[];
}

export enum GameStatus {
  SCHEDULED = "SCHEDULED",
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED"
}

export type Game = {
  id?: number;
  dateTime: string; // ISO format date-time
  opponentTeam: string;
  stadium?: Stadium;
  players?: Player[];
  attendance?: number;
  result: GameResult;
  status: GameStatus;
};
