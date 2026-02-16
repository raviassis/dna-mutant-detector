import { render, screen, fireEvent } from "@testing-library/react";
import DnaSequeceInput from "./DNASequenceInput";

describe('DNASequenceInput Component', () => {
    it('should render the component', () => {
        render(<DnaSequeceInput onSequencesChange={() => {}} resetTrigger={0} />);
        const inputElement = screen.getByPlaceholderText('Type sequence and press space...');
        expect(inputElement).toBeInTheDocument();
    });

    it('should input a valid DNA sequence and display it', () => {
        render(<DnaSequeceInput onSequencesChange={() => {}} resetTrigger={0} />);
        const inputElement = screen.getByPlaceholderText('Type sequence and press space...');

        fireEvent.change(inputElement, { target: { value: 'ATGC' } });
        fireEvent.keyDown(inputElement, { key: ' ' });

        const sequenceTag = screen.getByText('ATGC');
        expect(sequenceTag).toBeInTheDocument();
        expect(sequenceTag).toHaveClass('bg-green-700');
    });

    it('should not input accept invalid characters', () => {
        render(<DnaSequeceInput onSequencesChange={() => {}} resetTrigger={0} />);
        const inputElement = screen.getByPlaceholderText('Type sequence and press space...');

        fireEvent.keyDown(inputElement, { key: 'X' });
        fireEvent.keyDown(inputElement, { key: 'X' });
        fireEvent.keyDown(inputElement, { key: 'X' });
        fireEvent.keyDown(inputElement, { key: ' ' });

        const sequenceTag = screen.queryByText('XXX');
        expect(sequenceTag).not.toBeInTheDocument();
    });

    it('should paste a valid DNA sequence and display it', () => {
        render(<DnaSequeceInput onSequencesChange={() => {}} resetTrigger={0} />);
        const inputElement = screen.getByPlaceholderText('Type sequence and press space...');

        fireEvent.paste(inputElement, { clipboardData: { getData: () => 'ATGC ATGT,AGTT   AGGT' } });
        fireEvent.keyDown(inputElement, { key: ' ' });

        expect(screen.getByText('ATGC')).toBeInTheDocument();
        expect(screen.getByText('ATGT')).toBeInTheDocument();
        expect(screen.getByText('AGTT')).toBeInTheDocument();
        expect(screen.getByText('AGGT')).toBeInTheDocument();
    });

    it('should remove a sequence when the remove button is clicked', () => {
        render(<DnaSequeceInput onSequencesChange={() => {}} resetTrigger={0} />);
        const inputElement = screen.getByPlaceholderText('Type sequence and press space...');

        fireEvent.change(inputElement, { target: { value: 'ATGC' } });
        fireEvent.keyDown(inputElement, { key: ' ' });

        const removeButton = screen.getByText('×');
        fireEvent.click(removeButton);

        const sequenceTag = screen.queryByText('ATGC');
        expect(sequenceTag).not.toBeInTheDocument();
    });

});