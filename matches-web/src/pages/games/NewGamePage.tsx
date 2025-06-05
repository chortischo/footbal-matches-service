
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { gameApi, stadiumApi } from '@/services/api';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { GameForm } from '@/components/games/GameForm';
import { Game, GameResult, GameStatus } from '@/types/models';
import { toast } from 'sonner';

const NewGamePage = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { data: stadiums = [] } = useQuery({
    queryKey: ['stadiums'],
    queryFn: stadiumApi.getAll
  });

  const createMutation = useMutation({
    mutationFn: (game: Game) => gameApi.create(game),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['games'] });
      toast.success('Game created successfully');
      navigate('/games');
    },
    onError: () => {
      toast.error('Failed to create game');
    },
  });

  const handleSubmit = (data: Game) => {
    setIsSubmitting(true);
    createMutation.mutate(data);
    setIsSubmitting(false);
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Create New Game</h1>
      <Card>
        <CardHeader>
          <CardTitle>Game Details</CardTitle>
        </CardHeader>
        <CardContent>
          <GameForm 
            stadiums={stadiums}
            onSubmit={handleSubmit}
            onCancel={() => navigate('/games')}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default NewGamePage;
