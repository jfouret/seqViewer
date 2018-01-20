seqViewer.jar:

	mkdir build
	javac -release 8 -d build -cp src src/SeqViewer.java
	cp -r src/ressources build/ressources
	cp src/Manifest.txt build/Manifest.txt
	cd build && jar -cfm ../seqViewer.jar Manifest.txt .
	rm -r build
	tar -xvzf pteropus_vs_mammals.tar.gz

.PHONY: clean 
clean:
	-rm seqViewer.jar
	-rm -r build
	-rm -r pteropus_vs_mammals	

