
DOC=chisel-book

all:
	pdflatex $(DOC)
	bibtex $(DOC)
	pdflatex $(DOC)
	pdflatex $(DOC)

clean:
	rm -fr *.aux *.bbl *.blg *.log *.lof *.lot *.toc *.gz *.pdf *.lol

chisel:
	sbt "runMain Snippets"
	sbt "runMain Counter"
	sbt "runMain DivReg"
	sbt "test:runMain RegisterTester"
	sbt "test:runMain LogicTester"

eclipse:
	sbt eclipse

chisel2_test:
	cd chisel2; sbt "runMain RegisterTester"
	cd chisel2; sbt "runMain LogicTester"

chisel2_hw:
	cd chisel2; sbt "runMain LogicHardware"
