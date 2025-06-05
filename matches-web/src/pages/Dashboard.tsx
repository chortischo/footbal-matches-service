
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { 
  Users, 
  MapPin, 
  CalendarDays, 
  TrendingUp, 
  Medal, 
  Clock 
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { StatCard } from '@/components/ui/stat-card';
import { Badge } from '@/components/ui/badge';
import { gameApi, playerApi, stadiumApi } from '@/services/api';
import { Game, GameStatus } from '@/types/models';

const Dashboard = () => {
  const navigate = useNavigate();
  
  const { data: games } = useQuery({
    queryKey: ['games'],
    queryFn: async () => gameApi.getAll(),
  });
  
  const { data: players } = useQuery({
    queryKey: ['players'],
    queryFn: async () => playerApi.getAll(),
  });
  
  const { data: stadiums } = useQuery({
    queryKey: ['stadiums'],
    queryFn: async () => stadiumApi.getAll(),
  });

  const { data: scheduledGames } = useQuery({
    queryKey: ['scheduledGames'],
    queryFn: async () => gameApi.getScheduled(),
  });
  
  // Calculate stats
  const totalGames = games?.length || 0;
  const totalPlayers = players?.length || 0;
  const totalStadiums = stadiums?.length || 0;
  
  const activePlayers = players?.filter(p => p.status === 'ACTIVE').length || 0;
  const injuredPlayers = players?.filter(p => p.healthStatus === 'INJURED').length || 0;
  
  const wins = games?.filter(g => g.result === 'WIN').length || 0;
  const winPercentage = totalGames ? Math.round((wins / totalGames) * 100) : 0;
  
  // Get upcoming games (next 5 scheduled games)
  const upcomingGames = scheduledGames
    ?.filter(game => game.status === 'SCHEDULED')
    .sort((a, b) => new Date(a.dateTime).getTime() - new Date(b.dateTime).getTime())
    .slice(0, 5) || [];
  
  return (
    <div className="space-y-6">
      {/* Quick stats */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total Players"
          value={totalPlayers}
          description={`${activePlayers} active players`}
          icon={<Users />}
        />
        <StatCard
          title="Total Stadiums"
          value={totalStadiums}
          icon={<MapPin />}
        />
        <StatCard
          title="Total Games"
          value={totalGames}
          icon={<CalendarDays />}
        />
        <StatCard
          title="Win Rate"
          value={`${winPercentage}%`}
          icon={<TrendingUp />}
          trend={{
            direction: winPercentage >= 50 ? 'up' : 'down',
            value: `${wins} wins`
          }}
        />
      </div>
      
      <div className="grid gap-4 md:grid-cols-2">
        {/* Upcoming games */}
        <Card>
          <CardHeader className="flex flex-row items-center">
            <div className="flex-1">
              <CardTitle className="text-lg">Upcoming Games</CardTitle>
            </div>
            <Button 
              variant="ghost" 
              size="sm"
              onClick={() => navigate('/games')}
            >
              View all
            </Button>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {upcomingGames.length > 0 ? (
                upcomingGames.map((game) => (
                  <div 
                    key={game.id} 
                    className="flex items-center justify-between border-b pb-3 last:border-0"
                    onClick={() => navigate(`/games/${game.id}`)}
                  >
                    <div className="flex items-center space-x-3">
                      <div className="bg-football-green text-white p-2 rounded-full">
                        <CalendarDays className="h-4 w-4" />
                      </div>
                      <div>
                        <div className="font-medium">{game.opponentTeam}</div>
                        <div className="text-sm text-muted-foreground">
                          {format(new Date(game.dateTime), 'dd MMM yyyy, HH:mm')}
                        </div>
                      </div>
                    </div>
                    <div>
                      <Badge variant="outline">
                        {game.stadium?.name || 'No stadium'}
                      </Badge>
                    </div>
                  </div>
                ))
              ) : (
                <div className="text-center py-4 text-muted-foreground">
                  No upcoming games scheduled
                </div>
              )}
            </div>
          </CardContent>
        </Card>
        
        {/* Player status */}
        <Card>
          <CardHeader className="flex flex-row items-center">
            <div className="flex-1">
              <CardTitle className="text-lg">Player Status</CardTitle>
            </div>
            <Button 
              variant="ghost" 
              size="sm"
              onClick={() => navigate('/players')}
            >
              View all
            </Button>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="bg-green-100 text-green-800 p-2 rounded-full">
                    <Medal className="h-4 w-4" />
                  </div>
                  <div>
                    <div className="font-medium">Active Players</div>
                    <div className="text-sm text-muted-foreground">
                      Ready for matches
                    </div>
                  </div>
                </div>
                <div className="font-bold">{activePlayers}</div>
              </div>
              
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="bg-red-100 text-red-800 p-2 rounded-full">
                    <Clock className="h-4 w-4" />
                  </div>
                  <div>
                    <div className="font-medium">Injured Players</div>
                    <div className="text-sm text-muted-foreground">
                      Not available for matches
                    </div>
                  </div>
                </div>
                <div className="font-bold">{injuredPlayers}</div>
              </div>
              
              <div className="mt-4 pt-4 border-t">
                <div className="text-sm text-muted-foreground mb-2">Player Health Status</div>
                <div className="bg-gray-100 rounded-full h-2">
                  <div 
                    className="bg-football-green h-full rounded-full" 
                    style={{
                      width: `${totalPlayers ? ((totalPlayers - injuredPlayers) / totalPlayers) * 100 : 0}%`
                    }}
                  ></div>
                </div>
                <div className="text-xs mt-1 text-right text-muted-foreground">
                  {totalPlayers ? Math.round(((totalPlayers - injuredPlayers) / totalPlayers) * 100) : 0}% Fit
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;
