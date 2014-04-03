
word1=$1
word2=$2

java -cp "build/:lib/simplenlg-v442/*:lib/simplenlg-v442/lib/*:lib/RiTaWN/library/*" sentence.Test ./output/sentence_model  data/vocab_lists/dictionaryVocabStopwords.txt ./mpron/cmudict0.3 $word1 $word2

