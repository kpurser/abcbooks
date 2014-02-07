
from phrase_generator import PhraseGenerator
from nltk.corpus import brown
import sys

_corpus_path = "./data/oneFile_50.txt"

def main(abc_file, out_file):
	#gen = PhraseGenerator(brown)
	corpus = open(_corpus_path).read()
	print "Corpus read"
	gen = PhraseGenerator(corpus)
	lines = open(abc_file).readlines()
	assert len(lines) == 26
	sentences = []
	for x in xrange(0, len(lines), 2):
		one = lines[x]
		two = lines[x + 1]
		sentence = gen(one + " " + two)
		if sentence.endswith("..."):
			sentence = gen(one + " and " + two + "are really")
		sentences.append(sentence)
	print sentences
	_out = open(out_file, 'w')
	for sentence in sentences:
		_out.write(sentence)
		_out.write("\n")
	_out.close()

if __name__ == "__main__":
	abc_file = sys.argv[1]
	out_file = sys.argv[2]
	main(abc_file, out_file)

