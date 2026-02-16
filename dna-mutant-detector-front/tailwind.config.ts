import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        magneto: {
          red: '#B11226',
          purple: '#6A0DAD',
          dark: '#1A1A1A',
          silver: '#C0C0C0',
          green: '#22C55E',
          'dark-bg': '#2A2A2A',
        },
      },
    },
  },
  plugins: [],
}

export default config
