
import os
import sys


def main(indir, outdir):
	_id = 0
	contents = []
	try:
		os.makedirs(outdir)
	except:
		pass
	for f in os.listdir(indir):
		path = os.path.join(indir, f)
		contents.append(open(path).read())
		if len(contents) == 10:
			outfile = os.path.join(outdir, "file%d.txt" % _id)
			_id += 1
			out = open(outfile, 'w')
			out.write("\n".join(contents))
			contents = []

if __name__ == "__main__":
	main(sys.argv[1], sys.argv[2])

