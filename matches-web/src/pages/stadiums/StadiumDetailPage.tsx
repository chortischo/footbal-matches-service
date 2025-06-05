
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { stadiumApi, gameApi } from '@/services/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle, CardFooter } from '@/components/ui/card';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { StadiumForm } from '@/components/stadiums/StadiumForm';
import { Stadium, Game } from '@/types/models';
import { Edit, Trash2, Calendar, Users } from 'lucide-react';
import { toast } from 'sonner';
import { format } from 'date-fns';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';

const StadiumDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [activeTab, setActiveTab] = useState('details');

  const { data: stadium, isLoading: isStadiumLoading } = useQuery({
    queryKey: ['stadium', id],
    queryFn: () => stadiumApi.getById(Number(id)),
    enabled: !!id,
  });

  // Fetch games for this stadium
  const { data: gamesAtStadium, isLoading: isGamesLoading } = useQuery({
    queryKey: ['games', 'stadium', id],
    queryFn: () => gameApi.getByStadium(Number(id)),
    enabled: !!id,
  });

  const updateMutation = useMutation({
    mutationFn: (updatedStadium: Stadium) =>
      stadiumApi.update(Number(id), updatedStadium),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['stadium', id] });
      queryClient.invalidateQueries({ queryKey: ['stadiums'] });
      setIsEditModalOpen(false);
      toast.success('Stadium updated successfully');
    },
    onError: () => {
      toast.error('Failed to update stadium');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: () => stadiumApi.delete(Number(id)),
    onSuccess: () => {
      toast.success('Stadium deleted successfully');
      navigate('/stadiums');
    },
    onError: () => {
      toast.error('Failed to delete stadium');
    },
  });

  const handleDelete = () => {
    deleteMutation.mutate();
  };

  const handleUpdate = (updatedStadium: Stadium) => {
    updateMutation.mutate(updatedStadium);
  };

  const getStatusColor = (status: string) => {
    const statusColors: Record<string, string> = {
      SCHEDULED: 'bg-blue-100 text-blue-800',
      IN_PROGRESS: 'bg-amber-100 text-amber-800',
      COMPLETED: 'bg-green-100 text-green-800',
      CANCELLED: 'bg-red-100 text-red-800',
    };
    return statusColors[status] || 'bg-gray-100 text-gray-800';
  };

  if (isStadiumLoading) {
    return <div className="flex justify-center items-center h-64">Loading stadium details...</div>;
  }

  if (!stadium) {
    return <div className="text-center p-8">Stadium not found</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Stadium Details</h1>
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

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="grid w-[400px] grid-cols-2">
          <TabsTrigger value="details">Stadium Details</TabsTrigger>
          <TabsTrigger value="games">Scheduled Games</TabsTrigger>
        </TabsList>
        
        <TabsContent value="details">
          <Card>
            <CardHeader>
              <CardTitle>{stadium.name}</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-muted-foreground text-sm">Capacity</p>
                  <p>{stadium.capacity.toLocaleString()} seats</p>
                </div>
                <div>
                  <p className="text-muted-foreground text-sm">Price per Seat</p>
                  <p className="font-mono">${stadium.pricePerSeat.toFixed(2)}</p>
                </div>
                <div>
                  <p className="text-muted-foreground text-sm">Total Games</p>
                  <p>{gamesAtStadium?.length || 0}</p>
                </div>
              </div>
            </CardContent>
            <CardFooter>
              <Button variant="outline" onClick={() => navigate('/stadiums')}>
                Back to Stadiums
              </Button>
            </CardFooter>
          </Card>
        </TabsContent>
        
        <TabsContent value="games">
          <Card>
            <CardHeader>
              <CardTitle>Games at {stadium.name}</CardTitle>
            </CardHeader>
            <CardContent>
              {isGamesLoading ? (
                <div className="text-center py-4">Loading games...</div>
              ) : gamesAtStadium && gamesAtStadium.length > 0 ? (
                <div className="space-y-6">
                  {gamesAtStadium.map((game: Game) => (
                    <Card key={game.id} className="overflow-hidden">
                      <CardHeader className="bg-muted/50 py-3">
                        <div className="flex justify-between items-center">
                          <div>
                            <h3 className="text-lg font-medium">vs {game.opponentTeam}</h3>
                            <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                              <Calendar className="h-4 w-4" />
                              <span>{format(new Date(game.dateTime), 'PPP p')}</span>
                            </div>
                          </div>
                          <Badge className={getStatusColor(game.status)}>
                            {game.status.replace('_', ' ')}
                          </Badge>
                        </div>
                      </CardHeader>
                      <CardContent className="py-4">
                        <div className="mb-4">
                          <div className="flex items-center mb-2">
                            <Users className="h-4 w-4 mr-2" />
                            <h4 className="font-medium">Players ({game.players?.length || 0})</h4>
                          </div>
                          
                          {game.players && game.players.length > 0 ? (
                            <Table>
                              <TableHeader>
                                <TableRow>
                                  <TableHead>Name</TableHead>
                                  <TableHead>Health Status</TableHead>
                                </TableRow>
                              </TableHeader>
                              <TableBody>
                                {game.players.map(player => (
                                  <TableRow key={player.id} className="cursor-pointer hover:bg-muted/50" onClick={() => navigate(`/players/${player.id}`)}>
                                    <TableCell>{player.firstName} {player.lastName}</TableCell>
                                    <TableCell>
                                      <Badge
                                        variant={player.healthStatus === 'FIT' ? 'default' : player.healthStatus === 'INJURED' ? 'destructive' : 'outline'}
                                      >
                                        {player.healthStatus}
                                      </Badge>
                                    </TableCell>
                                  </TableRow>
                                ))}
                              </TableBody>
                            </Table>
                          ) : (
                            <p className="text-sm text-muted-foreground">No players assigned to this game yet.</p>
                          )}
                        </div>
                        
                        <div className="flex justify-end">
                          <Button variant="outline" size="sm" onClick={() => navigate(`/games/${game.id}`)}>
                            View Game Details
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              ) : (
                <div className="text-center py-4 text-muted-foreground">No games scheduled at this stadium yet.</div>
              )}
            </CardContent>
            <CardFooter>
              <Button variant="outline" onClick={() => navigate('/stadiums')}>
                Back to Stadiums
              </Button>
            </CardFooter>
          </Card>
        </TabsContent>
      </Tabs>

      <Dialog open={isEditModalOpen} onOpenChange={setIsEditModalOpen}>
        <DialogContent className="sm:max-w-[600px]">
          <DialogHeader>
            <DialogTitle>Edit Stadium</DialogTitle>
          </DialogHeader>
          {stadium && (
            <StadiumForm 
              initialData={stadium} 
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
          <p>Are you sure you want to delete {stadium.name}?</p>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDeleteModalOpen(false)}>Cancel</Button>
            <Button variant="destructive" onClick={handleDelete}>Delete</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default StadiumDetailPage;
