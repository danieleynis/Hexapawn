# Author: Daniel Eynis

import glob
import os.path as path

for fname in glob.glob("tests/*.in"):
	
	base = path.basename(fname) 
	name = path.splitext(base)[0]  # name of file without extension
	outf = "tests/" + name + ".out"  # name of output file
	#fdir = path.dirname(path.abspath(file))  # get abs path to test directory
	
	with open(fname) as infile:
		fcontent = infile.read()
		print(fcontent)


	with open(outf) as outfile:
		foutcont = outfile.read()
		print(foutcont)
	
