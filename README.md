Ensembles of NLP Tools for Data Element Extraction from Clinical Notes
------------------------------------------------------------------------

### Correction
We recently discovered that the evaluation metric used in our AMIA 2016 paper entitled, "Ensembles of NLP Tools for Data Element Extraction from Clinical Notes," for evaluating the ensembles’ performance is inconsistent with our implementation. Instead of precision, recall, and F1-score being calculated by “corpus-level” and “sentence-level” measures, they were calculated by “label-based micro-averaging” and “example-based” measures, respectively and as defined in a paper widely accepted by the NLP and Machine Learning community [1]. In brief, Zhang et al. state “label-based [micro-averaging] metrics work by evaluating the learning system’s performance on each class label separately, and then returning the [micro]-averaged value across all class labels,” while “example-based metrics work by evaluating the learning system’s performance on each test example separately, and then returning the mean value across the test set.”[1] Zhang ML, Zhou ZH. A review on multi-label learning algorithms. IEEE transactions on knowledge and data engineering. 2014 Aug;26(8):1819-37.
### Introduction

This is the code for the Natural Language Processing (NLP) tool ensemble component in the ensemble pipeline of clinical NLP data element extraction tools. In this component, there are 3 inputs:

1. Ground truth annotations in [BRAT](http://brat.nlplab.org/) format.
2. Annotations generated by an individual NLP tool (also in [BRAT](http://brat.nlplab.org/) format).
3. The beginning and ending position of each sentence in notes (generated by the sentence splitter).

This component can perform basic and advanced ensembles, compare the ground truth annotations, and output the label-based micro-averaging and example-based evaluation results. We currently support 2 basic ensemble methods, UNION and INTERSECTION, and the following 5 advanced ensemble methods:

1. [BR](http://www.igi-global.com/article/multi-label-classification/1786): Binary Relevance
2. [MLkNN](http://www.sciencedirect.com/science/article/pii/S0031320307000027): Multi-Label K-Nearest Neighbor
3. [IBLR-ML](http://link.springer.com/article/10.1007%2Fs10994-009-5127-5): Instance-Based Logistic Regression for Multi-Label
4. [RAkEL](http://link.springer.com/chapter/10.1007%2F978-3-540-74958-5_38): Random k-Labelsets
5. [ECC](http://link.springer.com/article/10.1007%2Fs10994-011-5256-5): Ensemble of Classifier Chains

We utilize the implementation of the MLC algorithms available in [Mulan](http://mulan.sourceforge.net/index.html) for ensembles, and apply [J48](http://weka.sourceforge.net/doc.dev/weka/classifiers/trees/J48.html) as the base learner for the MLC algorithms. The system is implemented in Java, Python, and Shell Scripts.

### Installation

Internet connection is needed for downloading required components.

1. Prerequisites
 * Linux or Mac OS X
 * Java (version 1.8 or later)
 * Python (version 2.7 or later)
2. Libraries
 * [Mulan 1.5](http://mulan.sourceforge.net)
 * [Weka 3.7.10](http://www.cs.waikato.ac.nz/~ml/weka/)
 * [Apache Common Math 1.2](https://commons.apache.org/proper/commons-math/)
 * [Tsung-Ting's Java Utilities](http://www.csie.ntu.edu.tw/~d97944007/utility/)
3. For Linux users: run "setup_linux.sh".
4. For Mac OS X users: run "setup_macosx.sh".

### Running
1. **Ground truth annotations.** Put ground truth annotations in brat_inp/[condition]/training and brat_inp/[condition]/testing folders. By default, [condition] is one of the following 3 conditions: "CHF" (Congestive Heart Failure), "WMO" (Weight Management/Obesity) or "KD" (Kawasaki Disease).

   Take "CHF" for example, we put training ground truth annotations for the clinical note named "example_training_note" as "brat_inp/CHF/training/example_training_note.ann", and testing ground truth annotations for the clinical note named "example_testing_note" as "brat_inp/CHF/testing/example_testing_note.ann". It should be noted that the annotations are in [BRAT](http://brat.nlplab.org/) format (the BEGIN_CHAR in inclusive and the END_CHAR is exclusive), and the file extension must be ".ann":

 ```
 ANNOTATION_ID<tab>DATA_ELEMENT<space>BEGIN_CHAR<space>END_CHAR<tab>ANNOTATION_TEXT
 ```

   For example, suppose the text "CHF" in the clinical note named "example_note" from character 950 to 953 is annotated as data element "congestive_heart_failure", then the ground truth annotation file named "example_training_note.ann" should contain a line as below:

 ```
 T1	congestive_heart_failure 950 953	CHF
 ```

   It should also be noted that there must be ground truth annotations for both training and testing folders in order to have our ensemble component run correctly (this is also true for NLP tool output annotations and sentence splitting information described below). The corresponding information for the same clinical note should all have same file name (for example, "example_training_note.XXX").

2. **NLP tool annotations.** Put output annotations generate by each NLP tool in single/[tool]_brat_out/[condition]/training and single/[tool]_brat_out/[condition]/testing folders. By default, [tool] is one of the following 2 tools: "ctakes" ([Apache cTAKES](http://ctakes.apache.org/)) or "metamap" ([MetaMap](https://metamap.nlm.nih.gov/)).

   Take "CHF" for example, we put the training output annotations of Apache cTAKES as "single/ctakes_brat_out/CHF/training/example_training_note.ann", and testing output annotations as "single/ctakes_brat_out/CHF/testing/example_testing_note.ann". The output annotations should also be in [BRAT](http://brat.nlplab.org/) format, and the file extension must be ".ann".

3. **Sentence splitting information.** Since we provide both label-based micro-averaging and example-based evaluations, we also need the sentence splitting information as input of our ensemble component. Put the sentence splitting information in data_sent/[condition]/training and data_sent/[condition]/testing folders, and the file extension must be ".txt".

   Take "CHF" for example, we put training sentence splitting information in "data_sent/CHF/training/example_training_note.txt", and testing sentence splitting information in "data_sent/CHF/testing/example_testing_note.txt". It should be noted that the annotations are in the following simple format (the BEGIN_CHAR in inclusive and the END_CHAR is exclusive):

 ```
 BEGIN_CHAR<tab>END_CHAR
 ```

   For example, the sentence splitting information for the first line of the clinical note "example_training_note.ann" is:

 ```
 0	312
 ```

4. **Dataset generation.** After the above files are all put in place, we can generate the dataset for the ensemble using the following command, specifying target condition:

 ```
 ./dataset-generator.sh [condition]
 ```

   For example, the following command generates datasets for CHF condition:

 ```
 ./dataset-generator.sh CHF
 ```

   The generated datasets are located in ./arff and ./xml folders.

5. **Single NLP tool.** To evaluate the results of single NLP tool, please execute the following command, specifying target condition and the fold number of cross validation (the fold number must equal to or larger than 2):

 ```
 ./nlp-tool.sh [condition] [fold]
 ```

   For example, the following command generates datasets for CHF condition with 10-fold cross validation:

 ```
 ./nlp-tool.sh CHF 10
 ```

   The output file name is nlp-tool-results-[condition]-[fold].csv (for example, "nlp-tool-results-CHF-10.csv"). The format of the result files is Comma-Separated Values (CSV), with the first row as header. There are 9 columns in a result file:

 * Condition
 * Method (by default either "ctakes" or "metamap")
 * Level (either corpus or sentence)
 * Training-Precision
 * Training-Recall
 * Training-F1
 * Testing-Precision
 * Testing-Recall
 * Testing-F1

6. **Basic ensemble methods.** To evaluate the results of basic ensemble methods of NLP tools, please execute the following command, specifying target condition and the fold number of cross validation:

 ```
 ./basic-ensemble.sh [condition] [fold]
 ```

   The output file name is basic-ensemble-results-[condition]-[fold].csv. The command arguments and result file format are the same as that of single NLP tool, except the "Method" column for basic ensemble results is one of the following 2 algorithms:

 * "union": UNION of NLP tools
 * "intersect": INTERSECTION of NLP tools

7. **Advanced ensemble methods.** To evaluate the results of advanced ensemble methods of NLP tools, please execute the following command, specifying target condition and the fold number of cross validation:

 ```
 ./advanced-ensemble.sh [condition] [fold]
 ```

   The output file name is advanced-ensemble-results-[condition]-[fold].csv. The command arguments and result file format are the same as that of single NLP tool, except the "Method" column for basic ensemble results is one of the following 5 algorithms:

 * "br": [BR](http://www.igi-global.com/article/multi-label-classification/1786), Binary Relevance
 * "mlknn": [MLkNN](http://www.sciencedirect.com/science/article/pii/S0031320307000027), Multi-Label K-Nearest Neighbor
 * "iblrml": [IBLR-ML](http://link.springer.com/article/10.1007%2Fs10994-009-5127-5), Instance-Based Logistic Regression for Multi-Label
 * "rakel": [RAkEL](http://link.springer.com/chapter/10.1007%2F978-3-540-74958-5_38), Random k-Labelsets
 * "ecc": [ECC](http://link.springer.com/article/10.1007%2Fs10994-011-5256-5), Ensemble of Classifier Chains

### Data Elements

We worked with our subject matter experts to identify 183 common data elements related to each condition (50 for CHF, 96 for WMO, and 37 for KD). To add/delete/modify data elements, please edit conf/[condition]_ANNOTATIONS.txt. For example, to change the data elements for CHF, please edit conf/CHF_ANNOTATIONS.txt. Below are exemplar data elements for "comorbidities" category in conf/CHF_ANNOTATIONS.txt:

 ```
 comorbidities_hypertension
 comorbidities_diabetes_mellitus
 comorbidities_atherosclerotic_disease
 comorbidities_obesity
 ```

The format is simply a data element per line. It should be note that data elements with duplicated names will be considered as the same.

### Conditions

The default 3 conditions for the ensemble component are "CHF" (Congestive Heart Failure), "WMO" (Weight Management/Obesity) and "KD" (Kawasaki Disease). To add a new condition (for example, "DIABETES"), we can simply do the following steps:

1. Add new data element file in the conf/ folder (for example, "conf/DIABETES_ANNOTATIONS.txt").

2. Create folders for ground truth annotations (for example, "brat_inp/DIABETES/training" and "brat_inp/DIABETES/testing"), and put corresponding files in the folders.

3. Create folders for NLP tool annotations (for example, "single/ctakes_brat_out/DIABETES/training", "single/metamap_brat_out/DIABETES/testing", "single/ctakes_brat_out/DIABETES/training", and "single/metamap_brat_out/DIABETES/testing"), and put corresponding files in the folders.

4. Create folders for sentence splitting information (for example, "data_sent/DIABETES/training" and "data_sent/DIABETES/testing"), and put corresponding files in the folders.

5. Run "dataset-generator.sh", "nlp-tools.sh", "basic-ensemble.sh", and "advanced-ensemble.sh" as described above, using the new condition (for example, "DIABETES") as argument.

### NLP Tools

The default 2 NLP tools to be used in the ensemble component are "ctakes" ([Apache cTAKES](http://ctakes.apache.org/)) and "metamap" ([MetaMap](https://metamap.nlm.nih.gov/)). Too add a new NLP tool (for example, "kdnlp" ([KD-NLP](http://www.ncbi.nlm.nih.gov/pubmed/26826020))), we can simply do the following steps:

1. Add the new NLP tool as a new line (for example, "kdnlp") in the conf/NLP_TOOLS.txt.

2. Create folders for new NLP tool annotations (for example, "single/kdnlp_brat_out/CHF/training" and "single/kdnlp_brat_out/CHF/testing"), and put corresponding files in the folders.

3. Run "dataset-generator.sh", "nlp-tools.sh", "basic-ensemble.sh", and "advanced-ensemble.sh" as described above.

### Citation

Please cite as below:

*Tsung-Ting Kuo, Pallavi Rao, Cleo Maehara, Son Doan, Juan D. Chaparro, Michele E. Day, Claudiu Farcas, Lucila Ohno-Machado, and Chun-Nan Hsu, "Ensembles of NLP Tools for Data Element Extraction from Clinical Notes", AMIA Annual Symposium, 2016.*

### Acknowledgement

This work is funded by [PCORI](http://www.pcori.org) contract CDRN-1306-04819.

### Contact

Thank you for using our software. If you have any questions or suggestions, please kindly contact Tsung-Ting Kuo (tskuo@ucsd.edu), Department of Biomedical Informatics, University of California, San Diego, USA.
