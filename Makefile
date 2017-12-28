
DOC=chisel-book

all:
	pdflatex $(DOC)
	bibtex $(DOC)
	pdflatex $(DOC)
	pdflatex $(DOC)

clean:
	rm -fr *.aux *.bbl *.blg *.log *.lof *.lot *.toc *.gz *.pdf *.lol

chisel:
	sbt "runMain DivReg"

# broken as they need to be changed to Chisel 3
broken:
	sbt "test:runMain RegisterTester"
	sbt "test:runMain LogicTester"
