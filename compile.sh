
mkdir -p build/topic
mkdir -p build/book
mkdir -p build/test

javac -cp "lib/RiTaWN/library/*" ./src/topic/*.java -d ./build 
javac ./src/book/*.java -d ./build 
javac -cp "lib/RiTaWN/library/*" ./src/test/*.java -d ./build 

