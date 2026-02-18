import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  scenarios: {
    spike: {
      executor: 'ramping-arrival-rate',
      startRate: 100, // iterations/s
      timeUnit: '1s',
      preAllocatedVUs: 1000, 
      maxVUs: 3000,
      stages: [
        // target iterations per `timeUnit` for the duration.
        // 600 iterations per second for 30s, 
        { target: 600, duration: '30s' },  // ramp up to 600 iterations/s
        { target: 600, duration: '1m' },  // keep at 600 iterations/s
        { target: 100, duration: '30s' },     // ramp down to 100 iterations/s
      ],
    },
  },
};

// avg = 66.3µs
// it do not impact test metrics
function generateRandomDna() {
  const chars = ['A', 'T', 'C', 'G'];

  // random dna size 6x6
  const n = 6;

  const dna = [];

  for (let i = 0; i < n; i++) {
    let sequence = '';
    for (let j = 0; j < n; j++) {
      sequence += chars[Math.floor(Math.random() * chars.length)];
    }
    dna.push(sequence);
  }

  return dna;
}


const API_URL = __ENV.API_URL || 'http://localhost:8080'
export default function() {
  const dna = generateRandomDna();
  let res1 = http.post(
      `${API_URL}/mutant`,
      JSON.stringify({ dna }),
      {
        headers: { 'Content-Type': 'application/json' },
      }
  );
  check(res1, { "status is 200 or 403": (res) => res.status === 200 || res.status === 403 })

  let res2 = http.get(`${API_URL}/stats`);
  check(res2, { "status is 200": (res) => res.status === 200 });
}
