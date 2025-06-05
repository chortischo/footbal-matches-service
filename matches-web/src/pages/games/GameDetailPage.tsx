
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { gameApi, stadiumApi, playerApi } from '@/services/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from '@/components/ui/card';
import { format } from 'date-fns';
import { Badge } from '@/components/ui/badge';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { GameForm } from '@/components/games/GameForm';
import { Game, GameStatus, GameResult, Player } from '@/types/models';
import { Edit, Trash2, UserPlus, Users } from 'lucide-react';
import { toast } from 'sonner';
import { Checkbox } from '@/components/ui/checkbox';

const GameDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [isPlayersModalOpen, setIsPlayersModalOpen] = useState(false);

  const { data: game, isLoading } = useQuery({
    queryKey: ['game', id],
    queryFn: () => gameApi.getById(Number(id)),
    enabled: !!id,
  });

  const { data: stadiums } = useQuery({
    queryKey: ['stadiums'],
    queryFn: stadiumApi.getAll,
  });

  const { data: allPlayers, isLoading: isPlayersLoading } = useQuery({
    queryKey: ['players'],
    queryFn: playerApi.getAll,
  });

  const updateMutation = useMutation({
    mutationFn: (updatedGame: Game) =>
      gameApi.update(Number(id), updatedGame),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['game', id] });
      queryClient.invalidateQueries({ queryKey: ['games'] });
      setIsEditModalOpen(false);
      toast.success('Game updated successfully');
    },
    onError: () => {
      toast.error('Failed to update game');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: () => gameApi.delete(Number(id)),
    onSuccess: () => {
      toast.success('Game deleted successfully');
      navigate('/games');
    },
    onError: () => {
      toast.error('Failed to delete game');
    },
  });

  const addPlayerMutation = useMutation({
    mutationFn: (playerId: number) => gameApi.addPlayer(Number(id), playerId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['game', id] });
      toast.success('Player added to game');
    },
    onError: () => {
      toast.error('Failed to add player to game');
    },
  });

  const removePlayerMutation = useMutation({
    mutationFn: (playerId: number) => gameApi.removePlayer(Number(id), playerId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['game', id] });
      toast.success('Player removed from game');
    },
    onError: () => {
      toast.error('Failed to remove player from game');
    },
  });

  const handleDelete = () => {
    deleteMutation.mutate();
  };

  const handleUpdate = (updatedGame: Game) => {
    updateMutation.mutate(updatedGame);
  };

  const handleTogglePlayer = (player: Player) => {
    const isPlayerInGame = game?.players?.some(p => p.id === player.id);
    
    if (isPlayerInGame) {
      removePlayerMutation.mutate(player.id!);
    } else {
      addPlayerMutation.mutate(player.id!);
    }
  };

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

  if (isLoading) {
    return <div className="flex justify-center items-center h-64">Loading game details...</div>;
  }

  if (!game) {
    return <div className="text-center p-8">Game not found</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Game Details</h1>
        <div className="space-x-2">
          <Button 
            variant="outline" 
            onClick={() => setIsPlayersModalOpen(true)}
          >
            <Users className="h-4 w-4 mr-2" /> Manage Players
          </Button>
          <Button 
            variant="outline" 
            onClick={() => setIsEditModalOpen(true)}
          >
            <Edit className="h-4 w-4 mr-2" /> Edit
          </Button>
          <Button 
            variant="destructive" 
            onClick={() => setIsDeleteModalOpen(true)}
          >
            <Trash2 className="h-4 w-4 mr-2" /> Delete
          </Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>vs {game.opponentTeam}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <p className="text-muted-foreground text-sm">Date & Time</p>
              <p>{format(new Date(game.dateTime), 'PPP p')}</p>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Stadium</p>
              <p>{game.stadium?.name || 'Not assigned'}</p>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Status</p>
              <Badge className={statusColors[game.status]}>
                {game.status}
              </Badge>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Result</p>
              <Badge className={resultColors[game.result]}>
                {game.result}
              </Badge>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Attendance</p>
              <p>{game.attendance?.toLocaleString() || 'N/A'}</p>
            </div>
          </div>

          <div className="mt-8">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-medium">Players</h3>
              <Button 
                variant="outline" 
                size="sm"
                onClick={() => setIsPlayersModalOpen(true)}
              >
                <UserPlus className="h-4 w-4 mr-2" /> Manage Players
              </Button>
            </div>
            {game.players && game.players.length > 0 ? (
              <ul className="space-y-2 divide-y">
                {game.players.map(player => (
                  <li key={player.id} className="py-2">
                    {player.firstName} {player.lastName}
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-muted-foreground">No players assigned to this game</p>
            )}
          </div>
        </CardContent>
        <CardFooter>
          <Button variant="outline" onClick={() => navigate('/games')}>
            Back to Games
          </Button>
        </CardFooter>
      </Card>

      <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
        <DialogContent className="sm:max-w-[600px]">
          <DialogHeader>
            <DialogTitle>Edit Game</DialogTitle>
          </DialogHeader>
          {game && (
            <GameForm 
              initialData={game} 
              stadiums={stadiums || []} 
              onSubmit={handleUpdate} 
              onCancel={() => setIsEditModalOpen(false)}
            />
          )}
        </DialogContent>
      </Dialog>

      <Dialog open={isDeleteModalOpen} onOpenChange={setIsDeleteModalOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirm Deletion</DialogTitle>
          </DialogHeader>
          <p>Are you sure you want to delete this game against {game.opponentTeam}?</p>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>Cancel</Button>
            <Button variant="destructive" onClick={handleDelete}>Delete</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={isPlayersModalOpen} onOpenChange={setIsPlayersModalOpen}>
        <DialogContent className="sm:max-w-[600px]">
          <DialogHeader>
            <DialogTitle>Manage Players</DialogTitle>
          </DialogHeader>
          
          {isPlayersLoading ? (
            <div className="py-4">Loading players...</div>
          ) : (
            <div className="py-2 max-h-[60vh] overflow-y-auto">
              <div className="space-y-4">
                {allPlayers?.map(player => {
                  const isInGame = game.players?.some(p => p.id === player.id);
                  
                  return (
                    <div key={player.id} className="flex items-center space-x-2 py-2 border-b">
                      <Checkbox 
                        id={`player-${player.id}`}
                        checked={isInGame}
                        onCheckedChange={() => handleTogglePlayer(player)}
                      />
                      <label 
                        htmlFor={`player-${player.id}`}
                        className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 cursor-pointer flex-1"
                      >
                        {player.firstName} {player.lastName}
                      </label>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
          
          <DialogFooter>
            <Button onClick={() => setIsPlayersModalOpen(false)}>Done</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default GameDetailPage;
