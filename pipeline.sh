
#words_cmd=java
#words_args=""
#words_exe=topic.TopicMain
#words_in_file=output/lda_output/topics.txt
#words_out_file=output/words.txt
#
#sentences_cmd=python
#sentences_args=""
#sentences_exe=src/python/sentences.py
#sentences_in_file=$word_out_file
#sentences_out_file=output/sentences.txt
#
#output_cmd=java
#output_args=""
#output_exe=bin/output
#output_in_file=$sentences_out_file
#output_out_file=output/book_$(book + "%T").html

./compile

java -cp "build/:lib/RiTaWN/library/*" topic.TopicMain output/lda_output/topics.txt data/CorrelationMatrix.txt  output/lda_stemmed_output/topics.txt $1

python python/sentences.py 26Words.txt output/sentences.txt

java -cp "build/" book.ABCBookCreator ./26Words.txt output/sentences.txt $1

#$words_cmd $words_args $words_exe $words_in_file $words_out_file
#$sentences_cmd $sentences_args $sentences_exe $sentences_in_file $sentences_out_file
#$output_cmd $output_args $output_exe $output_in_file $output_out_file


