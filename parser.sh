
infile=$1
outfile="output/wiki_dependencies/${infile##*/}"

#echo $infile
#echo $outfile

java -mx4000m -cp "lib/stanford-parser-full-2014-01-04/*" edu.stanford.nlp.parser.lexparser.LexicalizedParser -maxLength 60 -retainTmpSubcategories -outputFormat "typedDependencies" -outputFormatOptions "basicDependencies" lib/stanford-parser-full-2014-01-04/englishPCFG.ser.gz $infile > $outfile
