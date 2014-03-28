
./compile.sh
java -cp "build/:lib/simplenlg-v442/*:lib/simplenlg-v442/lib/*:lib/RiTaWN/library/*" sentence.ModelGenDriver output/dependencies output/sentence_model  dictionaryVocabStopwords.txt data/names.txt
