interface StatsMetricsProps {
    metrics: {
        count_human_dna: number;
        count_mutant_dna: number;
        ratio: number;
    };
}

export default function StatsMetrics({ metrics }: { metrics: any }) {
    return (
        <div className="w-full max-w-2xl mb-8 grid grid-cols-3 gap-4">
          <div className="border-2 border-magneto-red rounded p-4 text-center">
            <div data-testid="human-dna-count" className="text-2xl font-bold text-magneto-green">
              {metrics?.count_human_dna || 0}
            </div>
            <div className="text-sm text-magneto-silver opacity-70 mt-1">
              Human DNA
            </div>
          </div>
          <div className="border-2 border-magneto-red rounded p-4 text-center">
            <div data-testid="mutant-dna-count" className="text-2xl font-bold text-magneto-green">
              {metrics?.count_mutant_dna || 0}
            </div>
            <div className="text-sm text-magneto-silver opacity-70 mt-1">
              Mutant DNA
            </div>
          </div>
          <div className="border-2 border-magneto-red rounded p-4 text-center">
            <div data-testid="ratio-value" className="text-2xl font-bold text-magneto-green">
              {metrics?.ratio.toFixed(2) || "0.00"}
            </div>
            <div className="text-sm text-magneto-silver opacity-70 mt-1">
              Ratio
            </div>
          </div>
        </div>
    );
}