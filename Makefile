
DOC=chisel-book

all: gencode fig book

gencode:
	-mkdir code
	sbt test
	scala scripts/gencode.scala

fig:
	make -C figures

book:
	pdflatex $(DOC)
	bibtex $(DOC)
	makeindex $(DOC)
	pdflatex $(DOC)
	pdflatex $(DOC)

veryclean:
	git clean -fd

clean:
	rm -fr *.aux *.bbl *.blg *.log *.lof *.lot *.toc *.gz *.lol # *.pdf
	rm -rf code
	rm -rf test_run_dir

chisel:
	sbt "runMain Snippets"
	sbt "runMain Counter"
	sbt "test:runMain RegisterTester"
	sbt "test:runMain LogicTester"

test:
	sbt test

# test only one
flasher:
	sbt "testOnly FlasherSpec"

rtf:
	latex2rtf chisel-book.tex

detex:
	detex chisel-book.tex > chisel-book.txt

eclipse:
	sbt eclipse

chisel2_test:
	cd chisel2; sbt "runMain RegisterTester"
	cd chisel2; sbt "runMain LogicTester"

chisel2_hw:
	cd chisel2; sbt "runMain LogicHardware"
