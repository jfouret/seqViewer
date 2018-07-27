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

```
/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
```

## seqViewer

All other files in this repository are authored by Julien FOURET and licenced under the GNU Public Licence v3.0

This work have been done during a PhD fellowship co-funded by [ViroScan3D](http://www.viroscan3d.com/) and the DGA (Direction Générale de l'Armement) in the context of a [CIFRE-Défense](https://www.ixarm.com/fr/theses-dga-cifre-defense)

```
/*
 *   seqViewer is a program dedicated to the visualization of sites under 
 *   branch-site positive selection or similar approaches with superpositions 
 *   with annotated UniProt features.
 *   
 *   Copyright (C) 2018  Julien Fouret
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
```