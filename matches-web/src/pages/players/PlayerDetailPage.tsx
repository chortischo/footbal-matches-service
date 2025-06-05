
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { playerApi } from '@/services/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from '@/components/ui/card';
import { format } from 'date-fns';
import { Badge } from '@/components/ui/badge';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { PlayerForm } from '@/components/players/PlayerForm';
import { Player, PlayerStatus, HealthStatus } from '@/types/models';
import { Edit, Trash2 } from 'lucide-react';
import { toast } from 'sonner';

const PlayerDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

  const { data: player, isLoading } = useQuery({
    queryKey: ['player', id],
    queryFn: () => playerApi.getById(Number(id)),
    enabled: !!id,
  });

  const updateMutation = useMutation({
    mutationFn: (updatedPlayer: Player) =>
      playerApi.update(Number(id), updatedPlayer),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['player', id] });
      queryClient.invalidateQueries({ queryKey: ['players'] });
      setIsEditModalOpen(false);
      toast.success('Player updated successfully');
    },
    onError: () => {
      toast.error('Failed to update player');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: () => playerApi.delete(Number(id)),
    onSuccess: () => {
      toast.success('Player deleted successfully');
      navigate('/players');
    },
    onError: () => {
      toast.error('Failed to delete player');
    },
  });

  const handleDelete = () => {
    deleteMutation.mutate();
  };

  const handleUpdate = (updatedPlayer: Player) => {
    updateMutation.mutate(updatedPlayer);
  };

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

  if (isLoading) {
    return <div className="flex justify-center items-center h-64">Loading player details...</div>;
  }

  if (!player) {
    return <div className="text-center p-8">Player not found</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Player Details</h1>
        <div className="space-x-2">
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
          <CardTitle>
            {player.firstName} {player.lastName}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <p className="text-muted-foreground text-sm">Date of Birth</p>
              <p>{format(new Date(player.dateOfBirth), 'dd MMM yyyy')}</p>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Status</p>
              <Badge className={statusColors[player.status]}>
                {player.status}
              </Badge>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Health Status</p>
              <Badge className={healthColors[player.healthStatus]}>
                {player.healthStatus}
              </Badge>
            </div>
            <div>
              <p className="text-muted-foreground text-sm">Salary</p>
              <p className="font-mono">${player.salary.toLocaleString()}</p>
            </div>
          </div>
        </CardContent>
        <CardFooter>
          <Button variant="outline" onClick={() => navigate('/players')}>
            Back to Players
          </Button>
        </CardFooter>
      </Card>

      <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
        <DialogContent className="sm:max-w-[600px]">
          <DialogHeader>
            <DialogTitle>Edit Player</DialogTitle>
          </DialogHeader>
          {player && (
            <PlayerForm 
              initialData={player} 
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
          <p>Are you sure you want to delete {player.firstName} {player.lastName}?</p>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>Cancel</Button>
            <Button variant="destructive" onClick={handleDelete}>Delete</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default PlayerDetailPage;
