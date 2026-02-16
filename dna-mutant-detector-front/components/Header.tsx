export default function Header() {
    return (
        <div className="w-full max-w-2xl mb-8">
          <h1 className="text-4xl font-bold text-center mb-2">
            <span className="text-magneto-red">MUTANT</span>{" "}
            <span className="text-magneto-silver">DETECTOR</span>
          </h1>
          <p className="text-center text-magneto-silver opacity-70">
            Analyze DNA sequences to identify mutants
          </p>
        </div>
    );
}