# Context

Multiple sequence alignment (MSA) is central in many bio-informatics and phylogenetic analyses. This tool is dedicated to the visualization of sites under branch-site positive selection or similar approaches with superpositions with annotated UniProt features.

# Requirements 

**openjdk-8-jdk** is required for building the tools.

Not compatible with java 7 !

Tested on Ubuntu 14.04/16.04, Windows 7/10 and Mac OSX10

# Installation 

`javac` and `jar` are expected to be in your path.

```
git clone https://fouret.me/gitea/jfouret/seqViewer.git
cd seqViewer
make
```

# Usage

On a virtual desktop just click on the `seqViewer.jar` file. By command line: `java -jar seqViewer.jar`

# Getting Started

In the folder `pteropus_vs_mammals` you'll find ~800 folders that are input for visualization. You can specified `pteropus_vs_mammals` folder as main default folder. Each folder correspond to a gene that is is under branch-site positive selection in *Pteropus* ancestor.


