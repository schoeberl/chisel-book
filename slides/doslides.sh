
for i in $(find . -maxdepth 1 -name '*.tex')
  do
    pdflatex $i
done
