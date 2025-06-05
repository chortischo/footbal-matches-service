
import { useNavigate } from 'react-router-dom';
import { DataTable } from '@/components/ui/data-table';
import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import { Stadium } from '@/types/models';

interface StadiumsListProps {
  stadiums: Stadium[];
  isLoading?: boolean;
}

export function StadiumsList({ stadiums, isLoading }: StadiumsListProps) {
  const navigate = useNavigate();

  const columns = [
    {
      header: 'Name',
      accessorKey: 'name' as keyof Stadium,
    },
    {
      header: 'Capacity',
      accessorKey: 'capacity' as keyof Stadium,
      cell: (stadium: Stadium) => stadium.capacity.toLocaleString()
    },
    {
      header: 'Price Per Seat',
      accessorKey: 'pricePerSeat' as keyof Stadium,
      cell: (stadium: Stadium) => `$${stadium.pricePerSeat.toFixed(2)}`
    },
    {
      header: 'Total Games',
      accessorKey: 'games' as keyof Stadium,
      cell: (stadium: Stadium) => stadium.games?.length || 0
    },
  ];
  
  if (isLoading) {
    return <div className="text-center py-8">Loading stadiums...</div>;
  }
  
  return (
    <div className="space-y-4">
      <div className="flex justify-end">
        <Button onClick={() => navigate('/stadiums/new')}>
          <Plus className="h-4 w-4 mr-2" /> New Stadium
        </Button>
      </div>
      
      <DataTable
        data={stadiums}
        columns={columns}
        searchable
        onRowClick={(stadium) => navigate(`/stadiums/${stadium.id}`)}
      />
    </div>
  );
}
