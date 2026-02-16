import { useQuery, useMutation } from "@tanstack/react-query";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

interface Metrics {
  count_human_dna: number;
  count_mutant_dna: number;
  ratio: number;
}

interface DnaAnalysisResponse {
  isMutant: boolean;
}

export const useStats = () => {
  return useQuery({
    queryKey: ["stats"],
    queryFn: async () => {
      const response = await fetch(`${API_BASE_URL}/stats`);
      if (!response.ok) {
        throw new Error("Failed to fetch stats");
      }
      const data = await response.json();
      return {
        count_human_dna: data.count_human_dna || 0,
        count_mutant_dna: data.count_mutant_dna || 0,
        ratio: data.ratio || 0,
      } as Metrics;
    },
  });
};

export const useAnalyzeDna = () => {
  return useMutation({
    mutationFn: async (tokens: string[]) => {
      const response = await fetch(`${API_BASE_URL}/mutant`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          dna: tokens,
        }),
      });

      if (response.ok) {
        return {
          isMutant: true,
        } as DnaAnalysisResponse;
      }

      if (response.status === 403) {
        return {
          isMutant: false,
        } as DnaAnalysisResponse;
      }

      throw new Error("Failed to analyze DNA");
    },
  });
};
