
import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Game, GameStatus, GameResult, Stadium } from "@/types/models";
import { useState, useEffect } from "react";

interface GameFormProps {
  initialData?: Game;
  stadiums: Stadium[];
  onSubmit: (data: Game) => void;
  onCancel: () => void;
}

export function GameForm({ initialData, stadiums, onSubmit, onCancel }: GameFormProps) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [currentStatus, setCurrentStatus] = useState<GameStatus>(initialData?.status || GameStatus.SCHEDULED);

  const defaultValues: Game = initialData || {
    dateTime: new Date().toISOString().slice(0, 16),
    opponentTeam: "",
    result: GameResult.NOT_PLAYED,
    status: GameStatus.SCHEDULED,
    attendance: 0,
  };

  const form = useForm<Game>({
    defaultValues,
  });

  // Watch for status changes to enforce result consistency
  useEffect(() => {
    const subscription = form.watch((value, { name }) => {
      if (name === 'status') {
        const newStatus = value.status as GameStatus;
        setCurrentStatus(newStatus);
        
        // If status changes from COMPLETED to something else, reset result to NOT_PLAYED
        if (newStatus !== GameStatus.COMPLETED) {
          form.setValue('result', GameResult.NOT_PLAYED);
        }
      }
    });
    
    return () => subscription.unsubscribe();
  }, [form]);

  const handleSubmit = async (data: Game) => {
    setIsSubmitting(true);
    try {
      // If stadium is selected, update the stadium property
      if (data.stadium && typeof data.stadium === 'string') {
        const stadiumId = parseInt(data.stadium);
        const selectedStadium = stadiums.find(s => s.id === stadiumId);
        data.stadium = selectedStadium;
      }
      
      await onSubmit(data);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-6">
        <div className="grid grid-cols-2 gap-4">
          <FormField
            control={form.control}
            name="opponentTeam"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Opponent Team</FormLabel>
                <FormControl>
                  <Input placeholder="Team Name" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="dateTime"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Date & Time</FormLabel>
                <FormControl>
                  <Input 
                    type="datetime-local" 
                    {...field} 
                    value={typeof field.value === 'string' ? field.value.slice(0, 16) : ''}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        
        <div className="grid grid-cols-2 gap-4">
          <FormField
            control={form.control}
            name="stadium"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Stadium</FormLabel>
                <Select 
                  onValueChange={field.onChange} 
                  value={field.value?.id?.toString() || ''}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select stadium" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {stadiums.map(stadium => (
                      <SelectItem 
                        key={stadium.id} 
                        value={stadium.id?.toString() || ''}
                      >
                        {stadium.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="attendance"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Attendance</FormLabel>
                <FormControl>
                  <Input 
                    type="number" 
                    placeholder="0" 
                    {...field} 
                    onChange={e => field.onChange(Number(e.target.value))}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <FormField
            control={form.control}
            name="status"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Status</FormLabel>
                <Select 
                  onValueChange={(value) => field.onChange(value as GameStatus)} 
                  defaultValue={field.value}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select status" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value={GameStatus.SCHEDULED}>Scheduled</SelectItem>
                    <SelectItem value={GameStatus.IN_PROGRESS}>In Progress</SelectItem>
                    <SelectItem value={GameStatus.COMPLETED}>Completed</SelectItem>
                    <SelectItem value={GameStatus.CANCELLED}>Cancelled</SelectItem>
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="result"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Result</FormLabel>
                <Select 
                  onValueChange={(value) => field.onChange(value as GameResult)} 
                  defaultValue={field.value}
                  disabled={currentStatus !== GameStatus.COMPLETED}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select result" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value={GameResult.NOT_PLAYED}>Not Played</SelectItem>
                    {currentStatus === GameStatus.COMPLETED && (
                      <>
                        <SelectItem value={GameResult.WIN}>Win</SelectItem>
                        <SelectItem value={GameResult.LOSS}>Loss</SelectItem>
                        <SelectItem value={GameResult.DRAW}>Draw</SelectItem>
                      </>
                    )}
                  </SelectContent>
                </Select>
                {currentStatus !== GameStatus.COMPLETED && (
                  <p className="text-xs text-muted-foreground mt-1">
                    Game must be completed to set a result
                  </p>
                )}
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        
        <div className="flex justify-end space-x-2">
          <Button variant="outline" type="button" onClick={onCancel}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Saving..." : initialData ? "Update" : "Create"}
          </Button>
        </div>
      </form>
    </Form>
  );
}
