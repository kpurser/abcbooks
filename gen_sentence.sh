
indir=$1
outdir=$2
vocab=$3

java -cp "build/:lib/simplenlg-v442/*:lib/simplenlg-v442/lib/*:lib/RiTaWN/library/*" sentence.Test $indir $outdir $vocab

