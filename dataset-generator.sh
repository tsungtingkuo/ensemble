# Written by Tsung-Ting Kuo

printf '\n'
printf 'Ensembles of NLP Tools for Data Element Extraction from Clinical Notes\n'
printf 'Dataset Generator v1.0\n'
printf 'Department of Biomedical Informatics, UC San Diego, 2016\n'
printf '\n'

if [ "$#" -ne 1 ]; then {
	printf 'usage: dataset-generator [condition]\n'
	printf '\n'
	printf 'By default, [condition] should be one of the following:\n'
	printf '\n'
	printf 'CHF = Congestive Heart Failure\n'
	printf 'WMO = Weight Management / Obesity\n'
	printf 'KD = Kawasaki Disease\n'
	printf '\n'
	printf 'For more detailed usage information, please check our website:\n'
	printf 'https://github.com/tsungtingkuo/ensemble\n'
}
else {
	cd ./src
	sh generateDatasetArffAll.sh $1
	sh generateXmlArffAll.sh $1
	printf '\n'
	printf 'The output ARFF and XML dataset files are generated.\n'
	printf 'For more detailed usage information, please check our website:\n'
	printf 'https://github.com/tsungtingkuo/ensemble\n'
}
fi

printf '\n'
