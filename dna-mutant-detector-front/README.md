# dna-mutant-detector-front

Next.js frontend for the DNA mutant detector.

## Run with Docker

```bash
# build image
docker build -t dna-mutant-detector-front .

# run container
docker run -p 3000:3000 dna-mutant-detector-front
```

Open [http://localhost:3000](http://localhost:3000).

## Run as developer

```bash
# install dependences
npm i
```

```bash
# run as dev
npm run dev
```

Open [http://localhost:3000](http://localhost:3000). The app will reload when you edit files.

## Run tests

```bash
npm test
```

## Check test coverage

```bash
npm run test:coverage
```

Coverage report is written to `coverage/`. Open `coverage/lcov-report/index.html` in a browser for the HTML report.
