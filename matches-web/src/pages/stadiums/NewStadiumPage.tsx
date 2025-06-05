
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { stadiumApi } from '@/services/api';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { StadiumForm } from '@/components/stadiums/StadiumForm';
import { Stadium } from '@/types/models';
import { toast } from 'sonner';

const NewStadiumPage = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const createMutation = useMutation({
    mutationFn: (stadium: Stadium) => stadiumApi.create(stadium),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['stadiums'] });
      toast.success('Stadium created successfully');
      navigate('/stadiums');
    },
    onError: () => {
      toast.error('Failed to create stadium');
    },
  });

  const handleSubmit = (data: Stadium) => {
    setIsSubmitting(true);
    createMutation.mutate(data);
    setIsSubmitting(false);
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Create New Stadium</h1>
      <Card>
        <CardHeader>
          <CardTitle>Stadium Details</CardTitle>
        </CardHeader>
        <CardContent>
          <StadiumForm 
            onSubmit={handleSubmit}
            onCancel={() => navigate('/stadiums')}
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default NewStadiumPage;
