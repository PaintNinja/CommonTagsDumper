# CommonTagsDumper

CommonTagsDumper is a multiloader mod that dumps all loaded c namespaced tags to a nicely formatted markdown file, which lists all the tags and their contents in a fully sorted, human-readable format. This makes it easy to compare the tags between different loaders and compare differences.

The mod currently only works in a development environment, but we are looking to improve it in the future to work in a production environment as well so that it can be ran in modpacks to identify new candidate tags for the common convention based on what are commonly seen in the wild.

While this was made as part of https://github.com/MinecraftForge/MinecraftForge/pull/9955, we hope this will be useful for other loaders as well to validate their tags against other loaders, avoid mistakes and improve interoperability between all loaders.

To use it, simply import the project into your IDE and run the server for each loader. The markdown files will be generated in the run folder for each loader as soon as the server has finished starting up.
