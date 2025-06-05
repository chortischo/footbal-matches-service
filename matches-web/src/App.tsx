
import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout from "./components/layout/AppLayout";
import Dashboard from "./pages/Dashboard";
import GamesPage from "./pages/games/GamesPage";
import GameDetailPage from "./pages/games/GameDetailPage";
import NewGamePage from "./pages/games/NewGamePage";
import PlayersPage from "./pages/players/PlayersPage";
import PlayerDetailPage from "./pages/players/PlayerDetailPage";
import NewPlayerPage from "./pages/players/NewPlayerPage";
import StadiumsPage from "./pages/stadiums/StadiumsPage";
import StadiumDetailPage from "./pages/stadiums/StadiumDetailPage";
import NewStadiumPage from "./pages/stadiums/NewStadiumPage";
import SettingsPage from "./pages/settings/SettingsPage";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={
            <AppLayout>
              <Dashboard />
            </AppLayout>
          } />
          <Route path="/games" element={
            <AppLayout>
              <GamesPage />
            </AppLayout>
          } />
          <Route path="/games/new" element={
            <AppLayout>
              <NewGamePage />
            </AppLayout>
          } />
          <Route path="/games/:id" element={
            <AppLayout>
              <GameDetailPage />
            </AppLayout>
          } />
          <Route path="/players" element={
            <AppLayout>
              <PlayersPage />
            </AppLayout>
          } />
          <Route path="/players/new" element={
            <AppLayout>
              <NewPlayerPage />
            </AppLayout>
          } />
          <Route path="/players/:id" element={
            <AppLayout>
              <PlayerDetailPage />
            </AppLayout>
          } />
          <Route path="/stadiums" element={
            <AppLayout>
              <StadiumsPage />
            </AppLayout>
          } />
          <Route path="/stadiums/new" element={
            <AppLayout>
              <NewStadiumPage />
            </AppLayout>
          } />
          <Route path="/stadiums/:id" element={
            <AppLayout>
              <StadiumDetailPage />
            </AppLayout>
          } />
          <Route path="/settings" element={
            <AppLayout>
              <SettingsPage />
            </AppLayout>
          } />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
