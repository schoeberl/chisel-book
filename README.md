![build status](https://github.com/schoeberl/chisel-book/actions/workflows/test.yml/badge.svg)

# Digital Design with Chisel

[![Book Cover](cover-small.jpg)](http://www.imm.dtu.dk/~masca/chisel-book.html)

This book is an introduction to digital system design using a modern hardware
construction language, [Chisel](https://www.chisel-lang.org/).
In this book we focus on a slightly higher abstraction level to get you up to
speed to build more complex, interacting digital systems.

A compiled PDF version of the book is available at
[Digital Design with Chisel PDF](http://www.imm.dtu.dk/~masca/chisel-book.pdf).
Translations in Chinese, Japanese, and Vietnamese are also available
[here](http://www.imm.dtu.dk/~masca/chisel-book.html).

The book is available [at Amazon](https://www.amazon.com/dp/168933603X/).

In case you want to cite the book:

```
@book{chisel:book,
  title = {Digital Design with Chisel},
  publisher = {Kindle Direct Publishing},
  year = {2019},
  author = {Martin Schoeberl},
  url = {https://github.com/schoeberl/chisel-book}
}
```

This book project has an accompanying repository containing
[Chisel examples](https://github.com/schoeberl/chisel-examples).

To build the Chisel book from the source you need make, latex, sbt, and
a Java JDK (1.8 or later) installed. For some tests you also need Verilator
and z3 installed. Then you simply build the book with:

```
make
```

The book also contains slides for a one semester course on digital electronics
with Chisel. The slides are [here](slides). You can see a
[lecture plan](http://www2.imm.dtu.dk/courses/02139/) for a course
given at DTU that includes PDFs of the slides and the corresponding
[lab material](https://github.com/schoeberl/chisel-lab).

