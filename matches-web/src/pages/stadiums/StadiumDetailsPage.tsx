import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { stadiumApi } from '@/services/api';
import { Stadium } from '@/types/models';
import { Button } from '@/components/ui/button';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { toast } from '@/components/ui/use-toast';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from '@/components/ui/alert-dialog';
import { Pencil, Trash2 } from 'lucide-react';

const stadiumFormSchema = z.object({
  name: z.string().min(1, "Name is required"),
  capacity: z.coerce.number().min(1, "Capacity must be greater than 0"),
  pricePerSeat: z.coerce.number().min(0, "Price per seat must be greater than or equal to 0")
});

type StadiumFormValues = z.infer<typeof stadiumFormSchema>;

const StadiumDetailsPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isEditing, setIsEditing] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { data: stadium, isLoading } = useQuery({
    queryKey: ['stadium', id],
    queryFn: () => stadiumApi.getById(Number(id)),
    enabled: !!id
  });

  const form = useForm<StadiumFormValues>({
    resolver: zodResolver(stadiumFormSchema),
    defaultValues: {
      name: '',
      capacity: 0,
      pricePerSeat: 0
    }
  });

  useEffect(() => {
    if (stadium) {
      form.reset({
        name: stadium.name,
        capacity: stadium.capacity,
        pricePerSeat: stadium.pricePerSeat
      });
    }
  }, [stadium, form]);

  const updateStadiumMutation = useMutation({
    mutationFn: (data: Stadium) => stadiumApi.update(Number(id), data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['stadium', id] });
      queryClient.invalidateQueries({ queryKey: ['stadiums'] });
      toast({
        title: "Stadium updated",
        description: "The stadium has been updated successfully."
      });
      setIsEditing(false);
    },
    onError: () => {
      toast({
        title: "Error",
        description: "There was a problem updating the stadium.",
        variant: "destructive"
      });
    }
  });

  const deleteStadiumMutation = useMutation({
    mutationFn: () => stadiumApi.delete(Number(id)),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['stadiums'] });
      toast({
        title: "Stadium deleted",
        description: "The stadium has been deleted successfully."
      });
      navigate('/stadiums');
    }
  });

  const onSubmit = async (data: StadiumFormValues) => {
    setIsSubmitting(true);
    try {
      const stadiumData: Stadium = {
        ...stadium,
        name: data.name,
        capacity: data.capacity,
        pricePerSeat: data.pricePerSeat
      };
      await updateStadiumMutation.mutateAsync(stadiumData);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = () => {
    deleteStadiumMutation.mutate();
  };

  if (isLoading) {
    return <div className="text-center py-8">Loading stadium information...</div>;
  }

  if (!stadium) {
    return <div className="text-center py-8">Stadium not found</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Stadium Details</h2>
        <div className="flex gap-2">
          {!isEditing && (
            <>
              <Button 
                variant="outline" 
                onClick={() => setIsEditing(true)}
              >
                <Pencil className="h-4 w-4 mr-2" />
                Edit
              </Button>
              <AlertDialog>
                <AlertDialogTrigger asChild>
                  <Button variant="destructive">
                    <Trash2 className="h-4 w-4 mr-2" />
                    Delete
                  </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                    <AlertDialogDescription>
                      This action cannot be undone. This will permanently delete the stadium
                      and remove its data from our servers.
                    </AlertDialogDescription>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction
                      onClick={handleDelete}
                      className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
                    >
                      Delete
                    </AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
            </>
          )}
        </div>
      </div>

      {isEditing ? (
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 max-w-2xl">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Name</FormLabel>
                  <FormControl>
                    <Input placeholder="Stadium name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            
            <FormField
              control={form.control}
              name="capacity"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Capacity</FormLabel>
                  <FormControl>
                    <Input type="number" placeholder="0" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            
            <FormField
              control={form.control}
              name="pricePerSeat"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Price per Seat</FormLabel>
                  <FormControl>
                    <Input type="number" placeholder="0" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            
            <div className="flex gap-2 justify-end">
              <Button 
                type="button" 
                variant="outline" 
                onClick={() => setIsEditing(false)}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Saving...' : 'Save Changes'}
              </Button>
            </div>
          </form>
        </Form>
      ) : (
        <div className="grid gap-6">
          <Card>
            <CardHeader>
              <CardTitle>Stadium Information</CardTitle>
            </CardHeader>
            <CardContent className="grid gap-4">
              <div>
                <div className="text-sm font-medium text-muted-foreground">Name</div>
                <div className="text-lg">{stadium.name}</div>
              </div>
              <div>
                <div className="text-sm font-medium text-muted-foreground">Capacity</div>
                <div className="text-lg">{stadium.capacity.toLocaleString()} seats</div>
              </div>
              <div>
                <div className="text-sm font-medium text-muted-foreground">Price per Seat</div>
                <div className="text-lg font-mono">${stadium.pricePerSeat.toLocaleString()}</div>
              </div>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
};

export default StadiumDetailsPage; 