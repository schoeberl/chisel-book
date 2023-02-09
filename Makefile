
DOC=chisel-book

all: test gencode fig book genslides

#	scala scripts/gencode.scala

test:
	sbt test

gencode:
	-mkdir code
	sbt -Dsbt.main.class=sbt.ScriptMain scripts/gencode.scala

keywords:
	sbt -Dsbt.main.class=sbt.ScriptMain scripts/keywords.scala

checkref:
	sbt -Dsbt.main.class=sbt.ScriptMain scripts/checkref.scala


fig:
	make -C figures

book:
	pdflatex $(DOC)
	pdflatex $(DOC)
	bibtex $(DOC)
	makeindex $(DOC)
	pdflatex $(DOC)
	pdflatex $(DOC)

genslides:
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

chisel:
	sbt "runMain Snippets"
	sbt "runMain Counter"
	sbt "test:runMain RegisterTester"
	sbt "test:runMain LogicTester"

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
