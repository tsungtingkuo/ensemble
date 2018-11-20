# Generate XML LABEL
# Author: Tsung-Ting Kuo

import sys,os

def main():

	### Step 1: get standard vocabulary

	entities = []
	fin=open(sys.argv[1])
        for line in fin.readlines():
                if(len(line.strip())>0 and (line[0] != '#')):
			entities.append(line.strip())
	fin.close()


	### Step 2: generate XML
	print('<labels xmlns=\"http://mulan.sourceforge.net/labels\">')
	for entity in entities:
		print('\t<label name=\"' + entity + '\"></label>')
	print('</labels>')

if __name__=="__main__": main()
