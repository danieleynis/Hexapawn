"""

Author: Daniel Eynis

Online resources consulted:
http://stackoverflow.com/questions/3964681/find-all-files-in-a-directory-with-extension-txt-in-python
http://stackoverflow.com/questions/3415507/how-can-i-compile-and-run-a-java-class-in-a-different-directory
http://stackoverflow.com/questions/33054527/python-3-5-typeerror-a-bytes-like-object-is-required-not-str-when-writing-t
http://stackoverflow.com/questions/8475290/how-do-i-write-to-a-python-subprocess-stdin
http://stackoverflow.com/questions/8084260/python-printing-a-file-to-stdout
https://docs.python.org/3/library/glob.html
https://docs.python.org/3.5/library/os.path.html

"""

import glob
import os.path as path
from subprocess import Popen, PIPE

for fname in glob.glob("tests/*.in"):
	
	base = path.basename(fname) 
	name = path.splitext(base)[0]  # name of file without extension
	outf = "tests/" + name + ".out"  # name of output file
	
	with open(fname) as infile:  # get contents of input file

		with open(outf) as outfile:  # get resulting output to compare
			foutcont = outfile.read()

		# start a subprocess with the program running and pass the input file contents, capture stderr
		proc = Popen(["java", "-cp", "src", "Main", "-ea"], stdin=infile, stdout=PIPE, stderr=PIPE)
		out, err = proc.communicate()

		if err.decode() == foutcont:  # if output matches expected output in file pass or fail
			result = "PASSED"
		else:
			result = "FAILED"
		
		print("Test " + name + ": " + result)  # print result
