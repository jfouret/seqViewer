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

# Database format
seqViewer use a database in the zip archive with format specification detailed soon.
You can ask to be provided an example of database to julien.fouret@viroscan3d.com
# LICENCE
## JAligner

All files under the JAligner folder have been copied and modified from https://github.com/ahmedmoustafa/JAligner repository owned by ahmedmoustafa github.com user.

This files are licenced under the GNU General Public License v2.0 : https://github.com/ahmedmoustafa/JAligner/blob/master/LICENSE

## seqViewer

All other files in this repository are authored by Julien FOURET and licenced under the GNU Public Licence v3.0

This work have been done during a PhD fellowship co-funded by [ViroScan3D](http://www.viroscan3d.com/) and the DGA (Direction Générale de l'Armement) in the context of a [CIFRE-Défense](https://www.ixarm.com/fr/theses-dga-cifre-defense)