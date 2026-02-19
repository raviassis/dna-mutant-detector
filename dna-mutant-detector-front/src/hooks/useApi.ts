import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import * as api from "../api";  

export const useStats = () => {
  return useQuery({
    queryKey: ["stats"],
    queryFn: api.getStats,
    refetchInterval: 10*1000, //10s
  });
};

export const useAnalyzeDna = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: async (tokens: string[]) => {
      return api.postMutant(tokens).finally(() => {
        queryClient.invalidateQueries({ queryKey: ["stats"] });
      });
    },
  });
};
