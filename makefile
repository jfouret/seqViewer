seqViewer.jar:

	mkdir -p build
	javac -target 8 -d build -cp src src/SeqViewer.java || javac -release 8 -d build -cp src src/SeqViewer.java
	cp -r src/ressources build/ressources
	cp src/Manifest.txt build/Manifest.txt
	cd build && jar -cfm ../seqViewer.jar Manifest.txt .
	rm -r build

.PHONY: clean 
clean:
	-rm seqViewer.jar
	-rm -r build

