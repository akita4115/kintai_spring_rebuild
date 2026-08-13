const { defineConfig } = require("vite");
const react = require("@vitejs/plugin-react");
const path = require("path");

module.exports = defineConfig({
	plugins: [
		react()
	],

	build: {
		outDir: path.resolve(
			__dirname,
			"../main/resources/static/assets"
		),
		emptyOutDir: false,

		rollupOptions: {
			input: path.resolve(
				__dirname,
				"src/index.jsx"
			),

			output: {
				entryFileNames: "bundle.js",
				assetFileNames: "assets/[name][extname]"
			}
		}
	}
});