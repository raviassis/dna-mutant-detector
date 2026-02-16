import * as api from '../api';
import { render, screen, fireEvent } from "@testing-library/react";
import Home from "./page";
import RootLayout from "./layout";

jest.mock('../api');

function renderWithLayout(ui: React.ReactElement) {

  return render(
    <RootLayout>
      {ui}
    </RootLayout>
  );
}


describe('Home Component', () => {
    
    it('should render the HomePage component', async () => {
        (api.getStats as any).mockResolvedValue({
            count_human_dna: 10,
            count_mutant_dna: 5,
            ratio: 0.5,
        });
        renderWithLayout(<Home />);
        expect(screen.getByText('MUTANT')).toBeInTheDocument();
        expect(await screen.findByText('10')).toBeInTheDocument();
        expect(await screen.findByText('0.50')).toBeInTheDocument();
    });

    it('should submit DNA sequences and display analysis result', async () => {
        (api.postMutant as any).mockResolvedValue({ isMutant: true });
        renderWithLayout(<Home />);
        
        const inputElement = screen.getByPlaceholderText('Type sequence and press space...');
        fireEvent.change(inputElement, { target: { value: 'ATGC' } });
        fireEvent.keyDown(inputElement, { key: ' ' });

        const submitButton = screen.getByText('Analyze DNA');
        fireEvent.click(submitButton);

        expect(await screen.findByText('🧬 MUTANT DETECTED 🧬')).toBeInTheDocument();
    });
});