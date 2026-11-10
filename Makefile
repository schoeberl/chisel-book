
DOC=chisel-book

all: test gencode fig book genslides

test:
	sbt test

vhdl:
	make -C src/main/vhdl

gencode:
	mkdir -p code
	scala-cli run --server=false scripts/gencode.sc

keywords:
	scala-cli run --server=false scripts/keywords.sc

checkref:
	scala-cli run --server=false scripts/checkref.sc


fig:
	make -C figures

book: gencode fig
	pdflatex $(DOC)
	pdflatex $(DOC)
	bibtex $(DOC)
	makeindex $(DOC)
	pdflatex $(DOC)
	pdflatex $(DOC)

genslides: gencode fig
	cd slides; ./doslides.sh

veryclean:
	git clean -fd
	rm -rf ./idea

clean:
	rm -fr *.aux *.bbl *.blg *.log *.lof *.lot *.toc *.gz *.lol # *.pdf
	rm -rf code
	rm -rf test_run_dir
	rm -rf target
	rm -rf generated

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
