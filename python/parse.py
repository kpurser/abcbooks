
import sys
import json

relations = ['nsubj', 'dobj', 'iobj', 'amod']
struct = {key: dict() for key in relations}

vocab_file = sys.argv[1]
words = open(vocab_file).read().split()
vocab = set(words)
"prep(lived-6, In-1)"

def process_sentences(depends):
    for d in depends:
        idx = d.find('(')
        relation = d[:idx]
        word1 = d[idx + 1: d.find('-')]
        word2 = d[d.find(' ') + 1: d.rfind('-')]
        if relation in relations:
            if relation == 'nsubj':
                word1, word2 = word2, word1
            _dict = struct[relation]
            if word1 not in _dict:
                _dict[word1] = dict()
            if word2 not in _dict[word1]:
                _dict[word1][word2] = 0
            _dict[word1][word2] += 1

parser_file = sys.argv[2]

dependencies = list()

for line in open(parser_file).readlines():
    line = line.strip()
    if line == "":
        process_sentences(dependencies)
        dependencies = list()
    else:
        dependencies.append(line)

print json.dumps(struct, indent=4)
    

