import { render, screen } from '@testing-library/react';
import StatsMetrics from './StatsMetrics';

describe('StatsMetrics Component', () => {
  it('should display default values when metrics is null', () => {
    render(<StatsMetrics metrics={null} />);

    expect(screen.getByTestId('human-dna-count').textContent).toBe('0');
    expect(screen.getByTestId('mutant-dna-count').textContent).toBe('0');
    expect(screen.getByTestId('ratio-value').textContent).toBe('0.00');
  });

  it('should display correct metrics values', () => {
    const mockMetrics = {
      count_human_dna: 10,
      count_mutant_dna: 5,
      ratio: 0.5,
    };

    render(<StatsMetrics metrics={mockMetrics} />);

    expect(screen.getByText('10')).toBeInTheDocument();
    expect(screen.getByText('5')).toBeInTheDocument();
    expect(screen.getByText('0.50')).toBeInTheDocument();
  });
});
