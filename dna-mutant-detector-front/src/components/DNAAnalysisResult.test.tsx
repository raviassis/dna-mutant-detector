import { render, screen } from "@testing-library/react";
import DNAAnalysisResult from "./DNAAnalysisResult";

describe('DNAAnalysisResult Component', () => {
    it('should render mutant result correctly', () => {
        const analysisResult = {
            isMutant: true,
        };

        render(<DNAAnalysisResult analysisResult={analysisResult} />);
        const mutantText = screen.getByText('🧬 MUTANT DETECTED 🧬');
        expect(mutantText).toBeInTheDocument();
        expect(mutantText).toHaveClass('text-magneto-red');
    });
    it('should render human result correctly', () => {
        const analysisResult = {
            isMutant: false,
        };

        render(<DNAAnalysisResult analysisResult={analysisResult} />);
        const humanText = screen.getByText('HUMAN DNA');
        expect(humanText).toBeInTheDocument();
        expect(humanText).toHaveClass('text-magneto-green');
    });
    it('should not render anything when analysisResult is null', () => {
        render(<DNAAnalysisResult analysisResult={null} />);
        const mutantText = screen.queryByText('🧬 MUTANT DETECTED 🧬');
        const humanText = screen.queryByText('HUMAN DNA');
        expect(mutantText).not.toBeInTheDocument();
        expect(humanText).not.toBeInTheDocument();
    });
});