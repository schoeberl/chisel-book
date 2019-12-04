
# Slides for a 13 Week Course on Digital Design with Chisel

Those slides are used in the course Digital Electronics 2 at the
Technical University of Denmark. The course aims for second semester
EE students without programming experience, but basic digital
electronics knowledge (transistors, gates, timing, minimal VHDL).

 * [Introduction](01_intro.md)

The slides are written in Markdown. Currently I generate the PDF
with Visuel Studio Code. I am looking for a better scripting solution.
The PDFs will be made available on the
[DE2 course web site](http://www2.imm.dtu.dk/courses/02139/)
during the semester.

The slides are open-source, so feel free to adapt them for your
course needs. Please acknowledge the source.

## Notes on How to Use the Slides

 * with VCS
 * [landslide](https://github.com/adamzap/landslide) gives a presentation.html
 * with [remark](https://github.com/gnab/remark) using an index.html and a local web server

When working locally, with your slideshow HTML opened directly from disk, using the `sourceUrl` won't work out of the box. This requires hosting your files using a web server, which can be accomplished in [multiple ways](https://gist.github.com/willurd/5720255), e.g. by running `python3 -m http.server` in the directory of your `index.html` file. With a web server up and running, say on port 8000, you should be able to access your files via [http://localhost:8000](http://localhost:8000).
