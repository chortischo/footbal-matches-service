import { ReactNode, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { SidebarTrigger } from '@/components/ui/sidebar';
import { Button } from '@/components/ui/button';
import { Search, MapPin, Users, Calendar } from 'lucide-react';
import { CommandDialog, CommandInput, CommandList, CommandEmpty, CommandGroup, CommandItem } from '@/components/ui/command';
import { useQuery } from '@tanstack/react-query';
import { gameApi, playerApi, stadiumApi } from '@/services/api';

export function AppHeader() {
  const location = useLocation();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  
  // Search queries
  const { data: games } = useQuery({
    queryKey: ['gamesSearch', searchTerm],
    queryFn: () => gameApi.searchByOpponent(searchTerm),
    enabled: open && searchTerm.length > 0,
  });
  
  const { data: players } = useQuery({
    queryKey: ['playersSearch', searchTerm],
    queryFn: () => playerApi.search(searchTerm),
    enabled: open && searchTerm.length > 0,
  });
  
  const { data: stadiums } = useQuery({
    queryKey: ['stadiumsSearch', searchTerm],
    queryFn: () => stadiumApi.search(searchTerm),
    enabled: open && searchTerm.length > 0,
  });
  
  const getPageTitle = (): ReactNode => {
    const path = location.pathname;
    
    if (path === '/') return 'Dashboard';
    if (path === '/games') return 'Games';
    if (path.startsWith('/games/')) return 'Game Details';
    if (path === '/players') return 'Players';
    if (path.startsWith('/players/')) return 'Player Details';
    if (path === '/stadiums') return 'Stadiums';
    if (path.startsWith('/stadiums/')) return 'Stadium Details';
    if (path === '/settings') return 'Settings';
    
    return 'Football Match Scheduler';
  };

  const handleSearch = () => {
    setOpen(true);
  };

  return (
    <>
      <header className="border-b bg-white p-4 flex items-center justify-between">
        <div className="flex items-center">
          <div className="md:hidden">
            <SidebarTrigger />
          </div>
          <h1 className="text-xl font-bold ml-2">{getPageTitle()}</h1>
        </div>
        
        <div className="flex items-center space-x-2">
          <Button variant="outline" size="icon" onClick={handleSearch}>
            <Search className="h-4 w-4" />
          </Button>
        </div>
      </header>
      
      <CommandDialog open={open} onOpenChange={setOpen}>
        <CommandInput 
          placeholder="Search games, players, or stadiums..." 
          value={searchTerm}
          onValueChange={setSearchTerm}
        />
        <CommandList>
          <CommandEmpty>No results found.</CommandEmpty>
          
          {games && games.length > 0 && (
            <CommandGroup heading="Games">
              {games.slice(0, 5).map((game) => (
                <CommandItem 
                  key={game.id}
                  onSelect={() => {
                    navigate(`/games/${game.id}`);
                    setOpen(false);
                  }}
                >
                  <Calendar className="mr-2 h-4 w-4" />
                  <span>{game.opponentTeam}</span>
                  <span className="ml-2 text-muted-foreground">
                    {new Date(game.dateTime).toLocaleDateString()}
                  </span>
                </CommandItem>
              ))}
            </CommandGroup>
          )}
          
          {players && players.length > 0 && (
            <CommandGroup heading="Players">
              {players.slice(0, 5).map((player) => (
                <CommandItem 
                  key={player.id}
                  onSelect={() => {
                    navigate(`/players/${player.id}`);
                    setOpen(false);
                  }}
                >
                  <Users className="mr-2 h-4 w-4" />
                  <span>{player.firstName} {player.lastName}</span>
                </CommandItem>
              ))}
            </CommandGroup>
          )}
          
          {stadiums && stadiums.length > 0 && (
            <CommandGroup heading="Stadiums">
              {stadiums.slice(0, 5).map((stadium) => (
                <CommandItem 
                  key={stadium.id}
                  onSelect={() => {
                    navigate(`/stadiums/${stadium.id}`);
                    setOpen(false);
                  }}
                >
                  <MapPin className="mr-2 h-4 w-4" />
                  <span>{stadium.name}</span>
                  <span className="ml-2 text-muted-foreground">
                    Capacity: {stadium.capacity.toLocaleString()}
                  </span>
                </CommandItem>
              ))}
            </CommandGroup>
          )}
        </CommandList>
      </CommandDialog>
    </>
  );
}
