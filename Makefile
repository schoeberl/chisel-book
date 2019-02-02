
DOC=chisel-book

all: gencode fig book

gencode:
	-mkdir code
	sbt compile
	scala scripts/gencode.scala

fig:
	cd figures; pdflatex *.tex

book:
	pdflatex $(DOC)
	bibtex $(DOC)
	pdflatex $(DOC)
	pdflatex $(DOC)

clean:
	rm -fr *.aux *.bbl *.blg *.log *.lof *.lot *.toc *.gz *.pdf *.lol
	rm -rf code

chisel:
	sbt "runMain Snippets"
	sbt "runMain Counter"
	sbt "test:runMain RegisterTester"
	sbt "test:runMain LogicTester"

test:
	sbt test

eclipse:
	sbt eclipse

chisel2_test:
	cd chisel2; sbt "runMain RegisterTester"
	cd chisel2; sbt "runMain LogicTester"

chisel2_hw:
	cd chisel2; sbt "runMain LogicHardware"
