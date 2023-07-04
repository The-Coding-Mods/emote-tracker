/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    colors: {
      'background': '#1f2026',
      'dark-primary': '#001d3d',
      'secondary': '#003566',
      'highlight': '#ffd60a',
      'dark-highlight': '#ffc300'
    },
    textColor: {
      'primary': '#ffffff'
    },
    extend: {},
  },
  plugins: [],
}

