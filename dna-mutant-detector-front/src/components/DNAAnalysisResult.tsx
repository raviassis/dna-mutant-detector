import { JSX } from "react";

export interface AnalysisResult {
  isMutant: boolean;
}

export default function DNAAnalysisResult({ analysisResult }: { analysisResult: AnalysisResult | null }): JSX.Element {
  return (
    <>
        {analysisResult && (
          <div className="w-full max-w-2xl">
            <div
              className={`rounded p-6 border-2 ${
                analysisResult.isMutant
                  ? "border-magneto-red bg-magneto-red bg-opacity-10"
                  : "border-magneto-green bg-magneto-green bg-opacity-10"
              }`}
            >
              <div className="text-center">
                <div className="text-2xl font-bold mb-2">
                  {analysisResult.isMutant ? (
                    <span className="text-magneto-red">🧬 MUTANT DETECTED 🧬</span>
                  ) : (
                    <span className="text-magneto-green">HUMAN DNA</span>
                  )}
                </div>
              </div>
            </div>
          </div>
        )}
    </>
  );
}