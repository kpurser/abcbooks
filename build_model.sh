
vocab_file=data/vocab_lists/dictionaryVocab.txt
model=output/sentence_model  
det_file=data/vocab_lists/determiners.txt

./compile.sh

java -cp "build/:lib/simplenlg-v442/*:lib/simplenlg-v442/lib/*:lib/RiTaWN/library/*" sentence.ModelGenDriver output/dependencies $model $vocab_file data/names.txt $det_file
