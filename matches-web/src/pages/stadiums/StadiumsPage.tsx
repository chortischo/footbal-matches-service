
import { useQuery } from '@tanstack/react-query';
import { StadiumsList } from '@/components/stadiums/StadiumsList';
import { stadiumApi } from '@/services/api';

const StadiumsPage = () => {
  const { data: stadiums, isLoading } = useQuery({
    queryKey: ['stadiums'],
    queryFn: stadiumApi.getAll
  });

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold">Stadiums Management</h2>
      </div>
      
      <StadiumsList stadiums={stadiums || []} isLoading={isLoading} />
    </div>
  );
};

export default StadiumsPage;
