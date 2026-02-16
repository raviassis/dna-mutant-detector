const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

export const postMutant = async (dna: string[]) => {
  const response = await fetch(`${API_BASE_URL}/mutant`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      dna,
    }),
  });

  if (response.ok) {
    return {
      isMutant: true,
    };
  }

  if (response.status === 403) {
    return {
      isMutant: false,
    };
  }

  throw new Error("Failed to analyze DNA");
};

interface Metrics {
  count_human_dna: number;
  count_mutant_dna: number;
  ratio: number;
}

export const getStats = async () => {
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
};