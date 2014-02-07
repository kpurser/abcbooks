#!/bin/bash

echo START: akuz-java-nlp-run-lda...

java -Xms256m -Xmx32g \
        -Dfile.encoding=UTF-8 \
        -jar ./akuz-java-nlp-run-lda-0.0.2.jar \
        -inputDir ../data/no_stem \
        -outputDir ../output/lda_output \
        -topicsConfigFile ./topics_config.txt \
        -stopWordsFile ./stop_words.txt \
	-burnInTempIter 30 \
	-samplingIter 100 \
        -threadCount 4 \

if [ "$?" -ne "0" ]; then
  echo "ERROR: akuz-java-nlp-run-lda"

  exit 1
else
  echo "DONE: akuz-java-nlp-run-lda"
  exit 0
fi
