# App-disabler
Android System Application disabler (need a root), based RootTools

## Install


1. Add RootTools Library to project depency. (https://github.com/Stericson/RootTools)
   (or add RootTools jar file into /libs)
2. Add MainActivity and row_disabler.xml to your project
3. Just run

This project uses RootTools Library(by Stericson), version 3.3.
if you want use RootTools 2.6 or below, 
change this code ````RootTools.getShell(true).add(command);```` to 
```` RootTools.getShell(true).add(command).waitForFinished(); ````

## Advanced
if you want disable / enable data application, 

in MainActivity. change this code 

````"pm list packages -s -e"```` to ````"pm list packages -e"````

in EnableActivity. change this code 

````"pm list packages -s -d"```` to ````"pm list packages -d" ````

## License

Copyright (c) 2013 - 2014 @WindSekirun. 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

