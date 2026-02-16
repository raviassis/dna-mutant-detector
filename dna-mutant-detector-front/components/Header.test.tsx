import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Header from './Header';

describe('Header Component', () => {
  it('should render the header component', () => {
    render(<Header />);
    const headerElement = screen.getByRole('heading', { level: 1 });
    expect(headerElement).toBeInTheDocument();
  });

  it('should display "MUTANT" text with correct styling', () => {
    render(<Header />);
    const mutantSpan = screen.getByText('MUTANT');
    expect(mutantSpan).toBeInTheDocument();
    expect(mutantSpan).toHaveClass('text-magneto-red');
  });

  it('should display "DETECTOR" text with correct styling', () => {
    render(<Header />);
    const detectorSpan = screen.getByText('DETECTOR');
    expect(detectorSpan).toBeInTheDocument();
    expect(detectorSpan).toHaveClass('text-magneto-silver');
  });

  it('should display the subtitle text', () => {
    render(<Header />);
    const subtitle = screen.getByText('Analyze DNA sequences to identify mutants');
    expect(subtitle).toBeInTheDocument();
  });
});
