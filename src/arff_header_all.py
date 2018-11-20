# Generate ARFF HEADER
# Author: Tsung-Ting Kuo

import sys,os

def main():

	### Step 1: get standard vocabulary

	entities = []
	fin=open(sys.argv[2])
        for line in fin.readlines():
                if(len(line.strip())>0 and (line[0] != '#')):
			entities.append(line.strip())
	fin.close()


	### Step 2: get tool list

	tools = []
	fin=open(sys.argv[5])
        for line in fin.readlines():
                if(len(line.strip())>0 and (line[0] != '#')):
			tools.append(line.strip())
	fin.close()

	### Step 3: generate "relation"

	condition = sys.argv[2].split('/')[2].split('_')[0]
	element = sys.argv[2].split('/')[2].split('_')[1].split('.')[0]
	phase = sys.argv[3].split('/')[-1]
	sys.stdout.write('@relation \'')
	sys.stdout.write(condition)
	sys.stdout.write(' ')
	sys.stdout.write(element)
	sys.stdout.write(' ')
	sys.stdout.write(phase)
	sys.stdout.write('\'\n\n')


	### Step 4: generate "attribute" for each tool

	for tool in tools:
		for entity in entities:
			sys.stdout.write('@attribute ')
			sys.stdout.write(tool)
			sys.stdout.write('_')
			sys.stdout.write(entity)
			sys.stdout.write(' {0,1}\n')


	### Step 5: generate "attribute" for gold standard

	for entity in entities:
		sys.stdout.write('@attribute ')
		sys.stdout.write(entity)
		sys.stdout.write(' {0,1}\n')


        ### Step 6: generate "data" header

	sys.stdout.write('\n@data\n\n')
	sys.stdout.flush()

if __name__=="__main__": main()
