/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    // Static HTML pages
    "./src/main/resources/static/**/*.html",
    
    // Thymeleaf templates (all HTML files in templates folder and subfolders)
    "./src/main/resources/templates/**/*.html"
  ],

  theme: {
    extend: {
    },
  },

  plugins: [
  ],
}
