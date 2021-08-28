# Generate ARFF DATA
# Author: Tsung-Ting Kuo

import sys,os

def main():

	### Step 1: get standard vocabulary

	filename = sys.argv[1].split('/')[-1].split('.')[0]

	entities = []
	fin=open(sys.argv[2])
        for line in fin.readlines():
                if(len(line.strip())>0 and (line[0] != '#')):
			entities.append(line.strip())
	fin.close()


	### Step 2: get sentence indices
	fin=open(sys.argv[3] + '/' + filename + '.txt')
	index_pairs = {}
	for line in fin.readlines():
                if len(line.strip())>0:
			fields = line.strip().split('\t')
			index_pairs[fields[0]] = fields[1]
	fin.close()


	### Step 3: get gold standard BRAT annotations

	fin=open(sys.argv[4] + '/' + filename + '.ann')
	lines = fin.readlines()
	fin.close()
	negatives = []
	anno_tags = []
	anno_begins = []
	anno_ends = []
	for line in lines:
                if len(line.strip())>0:
                        field = line.strip().split('\t')[1]
			annos = field.split()
                        if(annos[0] == 'Neg'):
				id = annos[2].split(':')[1]
                                negatives.append(id)
        for line in lines:
                if len(line.strip())>0:
			fields = line.strip().split('\t')
			subfields = fields[1].split()
			id = fields[0]
			anno = subfields[0]
                        if((anno in entities) and (id not in negatives)):
				anno_tags.append(subfields[0])
				anno_begins.append(subfields[1])
				anno_ends.append(subfields[2])


	### Step 4: get tool list

	tools = []
	fin=open(sys.argv[8])
        for line in fin.readlines():
                if(len(line.strip())>0 and (line[0] != '#')):
			tools.append(line.strip())
	fin.close()

	### Step 5: for each tool, get predicted BRAT annotations

	pred_tools_tags = []
	pred_tools_begins = []
	pred_tools_ends = []
	for tool in tools:
		pred_tags = []
		pred_begins = []
		pred_ends = []
		fin=open(sys.argv[7] + '/' + tool + '_brat_out/' + sys.argv[5] + '/' + sys.argv[9] + '/' + filename + '.ann')
		for line in fin.readlines():
			if len(line.strip())>0:
				fields = line.strip().split('\t')
				if len(fields) > 2:
					subfields = fields[1].split()
					if(subfields[0] in entities):
						pred_tags.append(subfields[0])
						pred_begins.append(subfields[1])
						pred_ends.append(subfields[2])
		fin.close()
		pred_tools_tags.append(pred_tags)
		pred_tools_begins.append(pred_begins)
		pred_tools_ends.append(pred_ends)	


	### Step 6: for each sentence, generate
        ###         (1) predictions (number = |tools| * |elements|)
        ###         (2) labels (number = |elements|)
        ### Note: only output sentences with at least 1 label

	for sent_begin, sent_end in index_pairs.iteritems():

		count = 0
		label_number = 0
		output_string = ''
		for index, pred_tags in enumerate(pred_tools_tags):
			pred_begins = pred_tools_begins[index]
			pred_ends = pred_tools_ends[index]
			mentions = []
			for idx, pred_tag in enumerate(pred_tags):
				if(pred_begins[idx] >= sent_begin and pred_ends[idx] <= sent_end):
					mentions.append(pred_tag)
			mentions = list(set(mentions))
			for entity in entities:
				if count > 0:
					output_string += ','
				if entity in mentions:
					output_string += '1'
				else:
					output_string += '0'
				count += 1

		annotations = []
		for idx, anno_tag in enumerate(anno_tags):
			if(anno_begins[idx] >= sent_begin and anno_ends[idx] <= sent_end):
				annotations.append(anno_tag)
		annotations = list(set(annotations))
		for entity in entities:
			if entity in annotations:
				output_string += ',1'
				label_number += 1
			else:
				output_string += ',0'

		if label_number > 0:
			print(output_string)

if __name__=="__main__": main()
