
mkdir -p build/topic
mkdir -p build/book
mkdir -p build/test
mkdir -p build/sentence

javac -cp "lib/RiTaWN/library/*" ./src/topic/*.java -d ./build 
javac ./src/book/*.java -d ./build 
javac -cp "lib/RiTaWN/library/*" ./src/test/*.java -d ./build 
javac -cp "lib/RiTaWN/library/*:lib/simplenlg-v442/*:lib/simplenlg-v442/lib/*" ./src/sentence/*.java -d ./build

