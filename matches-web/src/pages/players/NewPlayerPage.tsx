
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { playerApi } from '@/services/api';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { PlayerForm } from '@/components/players/PlayerForm';
import { Player } from '@/types/models';
import { toast } from 'sonner';

const NewPlayerPage = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const createMutation = useMutation({
    mutationFn: (player: Player) => playerApi.create(player),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['players'] });
      toast.success('Player created successfully');
      navigate('/players');
    },
    onError: () => {
      toast.error('Failed to create player');
    },
  });

  const handleSubmit = (data: Player) => {
    setIsSubmitting(true);
    createMutation.mutate(data);
    setIsSubmitting(false);
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Create New Player</h1>
      <Card>
        <CardHeader>
          <CardTitle>Player Details</CardTitle>
        </CardHeader>
        <CardContent>
          <PlayerForm 
            onSubmit={handleSubmit}
            onCancel={() => navigate('/players')}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default NewPlayerPage;
