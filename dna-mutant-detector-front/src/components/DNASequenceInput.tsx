"use client";

import { JSX, useState, useEffect } from "react";

interface DnaSequence {
  value: string;
  isValid: boolean;
}

interface DnaSequenceInputProps {
  onSequencesChange?: (sequences: string[]) => void;
  resetTrigger?: number;
}

const dnaRegex: RegExp = /^[ATCG]+$/i;

export default function DnaSequeceInput({ onSequencesChange, resetTrigger }: DnaSequenceInputProps): JSX.Element {
  const [sequences, setSequences] = useState<DnaSequence[]>([]);
  const [input, setInput] = useState<string>("");

  useEffect(() => {
    if (onSequencesChange) {
      onSequencesChange(sequences.map((seq) => seq.value));
    }
  }, [sequences, onSequencesChange]);

  useEffect(() => {
    if (resetTrigger !== undefined) {
      setSequences([]);
      setInput("");
    }
  }, [resetTrigger]);

  function addSequence(value: string): void {
    if (!value.trim()) return;

    const clean = value.trim().toUpperCase();
    const sequences = clean.split(/\s+/).filter((seq) => seq.length > 0);

    sequences.forEach((seq) => {
      const newSequence: DnaSequence = {
        value: seq,
        isValid: dnaRegex.test(seq),
      };
      setSequences((prev) => [...prev, newSequence]);
    });
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLInputElement>): void {
    if (e.key === "Enter" || e.key === " ") {
      e.preventDefault();
      addSequence(input);
      setInput("");
    } else if (!/^[ATGCatgc]$/.test(e.key) && !["Backspace", "Delete", "ArrowLeft", "ArrowRight", "Tab", "Control", "Meta", "c", "v", "x"].includes(e.key)) {
      e.preventDefault();
    }
  }

  function handleInputChange(e: React.ChangeEvent<HTMLInputElement>): void {
    const newValue = e.target.value.toUpperCase().replace(/[^ATGC ]/gi, " ").replace(/\s+/g, " ").trim();
    setInput(newValue);
  }

  function handlePaste(e: React.ClipboardEvent<HTMLInputElement>): void {
    e.preventDefault();
    const pastedText = e.clipboardData.getData("text/plain");
    setInput((prev) => (prev + " " + pastedText).toUpperCase().replace(/[^ATGC ]/gi, " ").replace(/\s+/g, " ").trim());
  }

  function removeSequence(index: number): void {
    setSequences((prev) => prev.filter((_, i) => i !== index));
  }

  return (
    <div className="flex flex-col gap-2 p-2 rounded border-2 border-magneto-red bg-magneto-dark">
      <div className="flex flex-wrap gap-1.5">
        {sequences && sequences.map((seq, index) => (
          <div
            key={index}
            className={`flex items-center gap-1.5 px-2.5 py-1.5 rounded-full text-white font-semibold ${
              seq.isValid ? "bg-green-700" : "bg-red-700"
            }`}
          >
            {seq.value}
            <span
              className="cursor-pointer font-bold hover:opacity-70"
              onClick={() => removeSequence(index)}
            >
              ×
            </span>
          </div>
        ))}
      </div>

      <input
        data-testid="dna-sequence-input"
        value={input}
        onChange={handleInputChange}
        onPaste={handlePaste}
        onKeyDown={handleKeyDown}
        className="border-none outline-none w-full p-2 rounded bg-magneto-dark-bg text-magneto-silver"
        placeholder="Type sequence and press space..."
      />
    </div>
  );
}
