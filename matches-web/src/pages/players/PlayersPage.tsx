
import { useQuery } from '@tanstack/react-query';
import { PlayersList } from '@/components/players/PlayersList';
import { playerApi } from '@/services/api';

const PlayersPage = () => {
  const { data: players, isLoading } = useQuery({
    queryKey: ['players'],
    queryFn: playerApi.getAll
  });

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold">Players Management</h2>
      </div>
      
      <PlayersList players={players || []} isLoading={isLoading} />
    </div>
  );
};

export default PlayersPage;
