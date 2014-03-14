
import sys

word_set = set()
for filename in sys.argv[1:]:
    contents = open(filename, 'r').read()
    words = contents.split()
    for word in words:
        word_set.add(word)

for word in sorted(word_set):
    print word

