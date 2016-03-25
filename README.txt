TODO: file is badly out-of-date!

Dwarf Fortress raw-xml Converter v1.33 2013-05-05

Requirements:

This is a java program.  To run it, please install the latest version of the Java Runtime Environment (JRE) available at: http://www.oracle.com/technetwork/java/javase/downloads/index.html

Purpose:

This is a suite of tools intended to mke working with the dwarf fortress raw files easier for modders.    
	
Usage:

Simply put whatever files you want converted/parsed into the /in/ folder.  Appropriate output will be generated in the /out/ folder upon running the executable jar.  
		"raw checker.jar" - parses input and outputs an error log if issues arise (if the error log is empty, either your project is clean, or you have a truly horrific parsing error, like a missing top-level OBJECT tag).
		"raw formatter.jar" - parses the input and outputs correctly formed and indented files of the same content
		"raw alphabetizer.jar" - same as formatter, but also alphabetizes the content (useful for comparing mods)
		"raw xml converter.jar" - converts everything in the /in/ folder to/from xml (the conversion works in both directions)
		"raw bifurcator.jar" - filters second-tier children based on the specified tag structure
		"raw ssense.jar" - intended for use with graphics raws only; converts them for use with stonesense
		
The folders "in" "out" "out/Y" and "out/N" all need to exist in the parent directory in order for the tools to work correctly.  If output is not as expected, check "errorlog.txt" for a detailed description of issues encountered.  

Changelog:

1.33:	*added alphabetizer
	
1.32:	*added some argument error messages to the stonesense converter
		*improved the robustness of the stonesense converter

1.31:	*added some argument definitions to tools
		
1.30:	*added the stonesense graphics converter
		*updated the apache collections dependency
		*added (partial?) support for graphics raws
		
1.20:	*updated formatter to print list of second-tier tags (everything directly below OBJECT)
		*added bifurcator

1.11:	*moved data child definition into an external form in "directory.txt"
		*updated Paramaters
		
1.10:	*added a standalone formatter, which simply parses raw files, and re-outputs them with corrected indentation
		*updated Paramaters
		
1.01:	*added a standalone checker program which simply reads the raw files, and does not bother doing writes, in order to check integrity of raw text files
		*added a dialog box to indicate operation progress
		*updated Paramaters

1.00:	*baseline release