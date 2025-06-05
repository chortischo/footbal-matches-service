import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { Badge } from '@/components/ui/badge';
import { DataTable } from '@/components/ui/data-table';
import { Button } from '@/components/ui/button';
import { Plus, Search, Calendar } from 'lucide-react';
import { Game, GameStatus, GameResult } from '@/types/models';
import { Input } from '@/components/ui/input';
import { useQuery } from '@tanstack/react-query';
import { gameApi } from '@/services/api';

interface GamesListProps {
  games: Game[];
  isLoading?: boolean;
  showHeader?: boolean;
  showNewButton?: boolean;
  onSearchTermChange?: (term: string) => void;
  onDateFilterChange?: (date: string) => void;
  searchTerm?: string;
  dateFilter?: string;
}

export function GamesList({ 
  games, 
  isLoading, 
  showHeader = true, 
  showNewButton = true,
  onSearchTermChange,
  onDateFilterChange,
  searchTerm: externalSearchTerm,
  dateFilter: externalDateFilter
}: GamesListProps) {
  const navigate = useNavigate();
  const [filter, setFilter] = useState<'all' | GameStatus>('all');
  
  // Use either external state (controlled) or internal state (uncontrolled)
  const [localSearchTerm, setLocalSearchTerm] = useState('');
  const [localDateFilter, setLocalDateFilter] = useState('');
  
  // Determine which values to use - external (parent controlled) or local (component state)
  const searchTerm = externalSearchTerm !== undefined ? externalSearchTerm : localSearchTerm;
  const dateFilter = externalDateFilter !== undefined ? externalDateFilter : localDateFilter;
  
  // Only use search query if we're not being controlled by parent
  const { data: searchResults, isLoading: isSearching } = useQuery({
    queryKey: ['gamesSearch', searchTerm, dateFilter],
    queryFn: () => gameApi.getAll(undefined, searchTerm || undefined, dateFilter || undefined),
    enabled: (onSearchTermChange === undefined && onDateFilterChange === undefined) && 
             (searchTerm.length > 0 || dateFilter.length > 0),
  });
  
  // Handle search term change
  const handleSearchTermChange = (value: string) => {
    if (onSearchTermChange) {
      onSearchTermChange(value);
    } else {
      setLocalSearchTerm(value);
    }
  };
  
  // Handle date filter change
  const handleDateFilterChange = (value: string) => {
    if (onDateFilterChange) {
      onDateFilterChange(value);
    } else {
      setLocalDateFilter(value);
    }
  };
  
  // Determine which games to display
  const displayGames = (onSearchTermChange === undefined && onDateFilterChange === undefined) && 
                      (searchTerm.length > 0 || dateFilter.length > 0) 
                      ? searchResults || [] 
                      : games;
  
  const filteredGames = filter === 'all' 
    ? displayGames 
    : displayGames.filter(game => game.status === filter);
    
  const statusColors: Record<GameStatus, string> = {
    SCHEDULED: 'bg-blue-100 text-blue-800',
    IN_PROGRESS: 'bg-amber-100 text-amber-800',
    COMPLETED: 'bg-green-100 text-green-800',
    CANCELLED: 'bg-red-100 text-red-800',
  };
  
  const resultColors: Record<GameResult, string> = {
    WIN: 'bg-green-100 text-green-800',
    LOSS: 'bg-red-100 text-red-800', 
    DRAW: 'bg-amber-100 text-amber-800',
    NOT_PLAYED: 'bg-gray-100 text-gray-800',
  };

  const columns = [
    {
      header: 'Date & Time',
      accessorKey: 'dateTime' as keyof Game,
      cell: (game: Game) => format(new Date(game.dateTime), 'dd MMM yyyy, HH:mm')
    },
    {
      header: 'Opponent',
      accessorKey: 'opponentTeam' as keyof Game,
    },
    {
      header: 'Stadium',
      accessorKey: 'stadium' as keyof Game,
      cell: (game: Game) => game.stadium?.name || 'Not assigned'
    },
    {
      header: 'Status',
      accessorKey: 'status' as keyof Game,
      cell: (game: Game) => (
        <Badge className={statusColors[game.status]}>
          {game.status.replace('_', ' ')}
        </Badge>
      )
    },
    {
      header: 'Result',
      accessorKey: 'result' as keyof Game,
      cell: (game: Game) => (
        <Badge className={resultColors[game.result]}>
          {game.result.replace('_', ' ')}
        </Badge>
      )
    },
    {
      header: 'Players',
      accessorKey: 'players' as keyof Game,
      cell: (game: Game) => game.players?.length || 0
    },
  ];
  
  // Always render the filter controls and search box
  const renderControls = () => (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <div className="space-x-1">
          {showHeader && (
            <>
              <Button 
                variant={filter === 'all' ? "default" : "outline"}
                size="sm"
                onClick={() => setFilter('all')}
              >
                All
              </Button>
              <Button 
                variant={filter === 'SCHEDULED' ? "default" : "outline"}
                size="sm"
                onClick={() => setFilter(GameStatus.SCHEDULED)}
              >
                Scheduled
              </Button>
              <Button 
                variant={filter === 'IN_PROGRESS' ? "default" : "outline"}
                size="sm"
                onClick={() => setFilter(GameStatus.IN_PROGRESS)}
              >
                In Progress
              </Button>
              <Button 
                variant={filter === 'COMPLETED' ? "default" : "outline"}
                size="sm"
                onClick={() => setFilter(GameStatus.COMPLETED)}
              >
                Completed
              </Button>
            </>
          )}
        </div>
        
        {showNewButton && (
          <Button onClick={() => navigate('/games/new')}>
            <Plus className="h-4 w-4 mr-2" /> New Game
          </Button>
        )}
      </div>
      
      <div className="flex flex-col sm:flex-row gap-2">
        <div className="relative flex-grow">
          <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search by opponent..."
            value={searchTerm}
            onChange={(e) => handleSearchTermChange(e.target.value)}
            className="pl-8"
          />
        </div>
        <div className="relative sm:w-64">
          <Calendar className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            type="date"
            placeholder="Filter by date..."
            value={dateFilter}
            onChange={(e) => handleDateFilterChange(e.target.value)}
            className="pl-8"
          />
        </div>
      </div>
    </div>
  );
  
  if (isLoading) {
    return (
      <div className="space-y-4">
        {renderControls()}
        <div className="text-center py-8">Loading games...</div>
      </div>
    );
  }
  
  if (isSearching) {
    return (
      <div className="space-y-4">
        {renderControls()}
        <div className="text-center py-8">Searching games...</div>
      </div>
    );
  }
  
  if (filteredGames.length === 0) {
    return (
      <div className="space-y-4">
        {renderControls()}
        <div className="text-center py-8 text-muted-foreground">
          {searchTerm || dateFilter ? 'No games found matching your search' : 'No games found for the selected filter'}
        </div>
      </div>
    );
  }
  
  return (
    <div className="space-y-4">
      {renderControls()}
      
      <DataTable
        data={filteredGames}
        columns={columns}
        onRowClick={(game) => navigate(`/games/${game.id}`)}
      />
    </div>
  );
}
