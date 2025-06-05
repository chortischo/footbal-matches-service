
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { Badge } from '@/components/ui/badge';
import { DataTable } from '@/components/ui/data-table';
import { Button } from '@/components/ui/button';
import { Plus, Search } from 'lucide-react';
import { Player, PlayerStatus, HealthStatus } from '@/types/models';
import { Input } from '@/components/ui/input';
import { useQuery } from '@tanstack/react-query';
import { playerApi } from '@/services/api';

interface PlayersListProps {
  players: Player[];
  isLoading?: boolean;
}

export function PlayersList({ players, isLoading }: PlayersListProps) {
  const navigate = useNavigate();
  const [filter, setFilter] = useState<'all' | PlayerStatus>('all');
  const [searchTerm, setSearchTerm] = useState('');
  
  // Use the search API when a search term is provided
  const { data: searchResults, isLoading: isSearching } = useQuery({
    queryKey: ['playersSearch', searchTerm],
    queryFn: () => playerApi.search(searchTerm),
    enabled: searchTerm.length > 0,
  });
  
  // Determine which players to display
  const displayPlayers = searchTerm.length > 0 ? searchResults || [] : players;
  
  const filteredPlayers = filter === 'all' 
    ? displayPlayers 
    : displayPlayers.filter(player => player.status === filter);
    
  const statusColors: Record<PlayerStatus, string> = {
    ACTIVE: 'bg-green-100 text-green-800',
    INACTIVE: 'bg-gray-100 text-gray-800',
    SUSPENDED: 'bg-red-100 text-red-800',
  };
  
  const healthColors: Record<HealthStatus, string> = {
    FIT: 'bg-green-100 text-green-800',
    INJURED: 'bg-red-100 text-red-800', 
    RECOVERING: 'bg-amber-100 text-amber-800',
  };

  const columns = [
    {
      header: 'Name',
      accessorKey: 'firstName' as keyof Player,
      cell: (player: Player) => `${player.firstName} ${player.lastName}`
    },
    {
      header: 'Date of Birth',
      accessorKey: 'dateOfBirth' as keyof Player,
      cell: (player: Player) => format(new Date(player.dateOfBirth), 'dd MMM yyyy')
    },
    {
      header: 'Status',
      accessorKey: 'status' as keyof Player,
      cell: (player: Player) => (
        <Badge className={statusColors[player.status]}>
          {player.status}
        </Badge>
      )
    },
    {
      header: 'Health',
      accessorKey: 'healthStatus' as keyof Player,
      cell: (player: Player) => (
        <Badge className={healthColors[player.healthStatus]}>
          {player.healthStatus}
        </Badge>
      )
    },
    {
      header: 'Salary',
      accessorKey: 'salary' as keyof Player,
      cell: (player: Player) => (
        <span className="font-mono">
          ${player.salary.toLocaleString()}
        </span>
      )
    },
  ];
  
  if (isLoading) {
    return <div className="text-center py-8">Loading players...</div>;
  }
  
  if (isSearching) {
    return <div className="text-center py-8">Searching players...</div>;
  }
  
  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <div className="flex items-center space-x-4">
          <div className="space-x-1">
            <Button 
              variant={filter === 'all' ? "default" : "outline"}
              size="sm"
              onClick={() => setFilter('all')}
            >
              All
            </Button>
            <Button 
              variant={filter === 'ACTIVE' ? "default" : "outline"}
              size="sm"
              onClick={() => setFilter('ACTIVE' as PlayerStatus)}
            >
              Active
            </Button>
            <Button 
              variant={filter === 'INACTIVE' ? "default" : "outline"}
              size="sm"
              onClick={() => setFilter('INACTIVE' as PlayerStatus)}
            >
              Inactive
            </Button>
            <Button 
              variant={filter === 'SUSPENDED' ? "default" : "outline"}
              size="sm"
              onClick={() => setFilter('SUSPENDED' as PlayerStatus)}
            >
              Suspended
            </Button>
          </div>
          
          <div className="relative w-64">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Search players..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-8"
            />
          </div>
        </div>
        
        <Button onClick={() => navigate('/players/new')}>
          <Plus className="h-4 w-4 mr-2" /> New Player
        </Button>
      </div>
      
      <DataTable
        data={filteredPlayers}
        columns={columns}
        onRowClick={(player) => navigate(`/players/${player.id}`)}
        searchable={false} // Disable built-in search since we're using API search
      />
    </div>
  );
}
