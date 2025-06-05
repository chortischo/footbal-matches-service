
import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { GamesList } from '@/components/games/GamesList';
import { gameApi } from '@/services/api';
import { Button } from '@/components/ui/button';
import { 
  ArrowDownAZ, 
  ArrowUpAZ,
  Trophy,
  SlidersHorizontal
} from 'lucide-react';
import { 
  Select, 
  SelectContent, 
  SelectItem, 
  SelectTrigger, 
  SelectValue 
} from '@/components/ui/select';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

type SortDirection = 'asc' | 'desc';
type ViewMode = 'all' | 'categories';

const GamesPage = () => {
  const [sortDirection, setSortDirection] = useState<SortDirection>('asc');
  const [viewMode, setViewMode] = useState<ViewMode>('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [dateFilter, setDateFilter] = useState('');

  // Updated: Use the combined search API with all current filters
  const { data: games, isLoading, refetch } = useQuery({
    queryKey: ['games', sortDirection, viewMode, searchTerm, dateFilter],
    queryFn: () => viewMode === 'all' ? 
      gameApi.getAll(sortDirection === 'asc', searchTerm || undefined, dateFilter || undefined) : 
      gameApi.getAll()
  });

  // Query for categorized games
  const { data: categorizedGames, isLoading: isCategoriesLoading } = useQuery({
    queryKey: ['gameCategories'],
    queryFn: gameApi.getGamesByResultCategory,
    enabled: viewMode === 'categories'
  });

  const toggleSortDirection = () => {
    setSortDirection(prev => prev === 'asc' ? 'desc' : 'asc');
  };

  // Handle search term updates from GamesList component
  const handleSearchTermChange = (term: string) => {
    setSearchTerm(term);
  };

  // Handle date filter updates from GamesList component
  const handleDateFilterChange = (date: string) => {
    setDateFilter(date);
  };

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold">Games Management</h2>
        <div className="flex items-center space-x-2">
          <Tabs value={viewMode} onValueChange={(value) => setViewMode(value as ViewMode)} className="w-[400px]">
            <TabsList className="grid grid-cols-2">
              <TabsTrigger value="all">All Games</TabsTrigger>
              <TabsTrigger value="categories">By Result</TabsTrigger>
            </TabsList>
          </Tabs>
        </div>
      </div>
      
      {viewMode === 'all' && (
        <div>
          <div className="flex justify-end mb-4">
            <Button 
              variant="outline" 
              onClick={toggleSortDirection}
              className="flex items-center gap-2"
            >
              {sortDirection === 'asc' ? (
                <>
                  <ArrowDownAZ className="h-4 w-4" />
                  <span>Sort by Date (Ascending)</span>
                </>
              ) : (
                <>
                  <ArrowUpAZ className="h-4 w-4" />
                  <span>Sort by Date (Descending)</span>
                </>
              )}
            </Button>
          </div>
          <GamesList 
            games={games || []} 
            isLoading={isLoading} 
            onSearchTermChange={handleSearchTermChange}
            onDateFilterChange={handleDateFilterChange}
            searchTerm={searchTerm}
            dateFilter={dateFilter}
          />
        </div>
      )}
      
      {viewMode === 'categories' && (
        <div className="space-y-6">
          {isCategoriesLoading ? (
            <div className="text-center py-8">Loading categorized games...</div>
          ) : (
            <>
              <div>
                <h3 className="text-lg font-medium mb-4 flex items-center">
                  <Trophy className="h-5 w-5 text-green-600 mr-2" />
                  Wins
                </h3>
                <GamesList games={categorizedGames?.WON || []} isLoading={false} />
              </div>
              
              <div>
                <h3 className="text-lg font-medium mb-4 flex items-center">
                  <Trophy className="h-5 w-5 text-red-600 mr-2" />
                  Losses
                </h3>
                <GamesList games={categorizedGames?.LOST || []} isLoading={false} />
              </div>
              
              <div>
                <h3 className="text-lg font-medium mb-4 flex items-center">
                  <Trophy className="h-5 w-5 text-amber-600 mr-2" />
                  Draws
                </h3>
                <GamesList games={categorizedGames?.DRAW || []} isLoading={false} />
              </div>
              
              <div>
                <h3 className="text-lg font-medium mb-4 flex items-center">
                  <SlidersHorizontal className="h-5 w-5 text-blue-600 mr-2" />
                  Not Played
                </h3>
                <GamesList games={categorizedGames?.NOT_PLAYED || []} isLoading={false} />
              </div>
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default GamesPage;
