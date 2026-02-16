"use client";

import { useState } from "react";
import DnaTokenInput from "@/components/DNATokenInput";
import { useStats, useAnalyzeDna } from "@/hooks/useApi";
import DNAAnalysisResult, { AnalysisResult } from "@/components/DNAAnalysisResult";
import Header from "@/components/Header";
import { Stats } from "fs";
import StatsMetrics from "@/components/StatsMetrics";

export default function Home() {
  const [analysisResult, setAnalysisResult] = useState<AnalysisResult | null>(null);
  const [tokens, setTokens] = useState<string[]>([]);
  const [resetTrigger, setResetTrigger] = useState(0);

  const { data: metrics, isLoading: metricsLoading } = useStats();
  const analyzeMutation = useAnalyzeDna();

  const handleSubmit = async () => {
    if (tokens.length === 0) {
      alert("Please add at least one DNA sequence");
      return;
    }

    try {
      const result = await analyzeMutation.mutateAsync(tokens);
      setAnalysisResult({
        isMutant: result.isMutant,
        sequences: tokens,
      });
      setResetTrigger((prev) => prev + 1);
    } catch (error) {
      console.error("Error analyzing DNA:", error);
      alert("Error analyzing DNA. Please try again.");
    }
  };

  return (
    <div className="min-h-screen bg-magneto-dark text-magneto-silver">
      <main className="flex flex-col items-center justify-center min-h-screen p-4">
        <Header />
        <StatsMetrics metrics={metrics} />

        <div className="w-full max-w-2xl mb-8">
          <h2 className="text-lg font-semibold mb-3 text-magneto-silver">
            Enter DNA Sequences
          </h2>
          <DnaTokenInput onTokensChange={setTokens} resetTrigger={resetTrigger} />
        </div>

        <button
          onClick={handleSubmit}
          disabled={analyzeMutation.isPending || tokens.length === 0}
          className="px-8 py-3 mb-8 font-bold rounded border-2 border-magneto-red bg-magneto-red text-white hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed transition-opacity"
        >
          {analyzeMutation.isPending ? "Analyzing..." : "Analyze DNA"}
        </button>

        <DNAAnalysisResult analysisResult={analysisResult} />
      </main>
    </div>
  );
}
