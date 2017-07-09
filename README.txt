Dwarf Fortress Raw Utilities v2.01.01 TBD

Requirements:

This is a java program.  To run it, please install the latest version of the Java Runtime Environment (JRE) available at: http://www.oracle.com/technetwork/java/javase/downloads/index.html

Purpose:

This is a suite of tools intended to mke working with the dwarf fortress raw files easier for modders.    
	
Usage:

Take the DF raw files you want processed and put them into the working/in directory.  Run the .jar from the base directory.  Outputs will be placed in the working/out and working/garbage directories.  Some of the utilities expect additional configuration in the working/target directory.  

	formatter:
		-simply parses all input files and immediately writes them back to disk
		-useful for generating indentation, or idenifying badly-formed tags (read the log file to identify any errors)

	organizer:
		-organizes the input files to match the structure described in the working/target directory
		-the files in the working/target directory should be lists of tags (one per line) in the desired output files
		-matching outputs are generated in the workoug/out directory, and any tags in the input that were not part of the target structure are places in the working/garbade directory

Changelog:

2.01.01:
    -updated tags

2.00.03:
    -first released version
		-support for "formatter" and "organizer"

2.00.01:
    -initial baseline (follows 1.33)
		-new core parsing engine with much lower memory footprint
		-not all functionality from 1.* versions available