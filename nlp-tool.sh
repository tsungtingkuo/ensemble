# Written by Tsung-Ting Kuo

printf '\n'
printf 'Ensembles of NLP Tools for Data Element Extraction from Clinical Notes\n'
printf 'NLP Tool v1.0\n'
printf 'Department of Biomedical Informatics, UC San Diego, 2016\n'
printf '\n'

if [ "$#" -eq 0 ]; then {
	printf 'usage: nlp-tool [condition] [fold]\n'
	printf '\n'
	printf 'By default, [condition] should be one of the following:\n'
	printf '\n'
	printf 'CHF = Congestive Heart Failure\n'
	printf 'WMO = Weight Management / Obesity\n'
	printf 'KD = Kawasaki Disease\n'
	printf '\n'
	printf 'The [fold] argument is to specify the fold number for cross validation.\n'
	printf 'It is an optional argument, and the default value is 10.\n'
	printf '\n'
	printf 'For more detailed usage information, please check our website:\n'
	printf 'https://github.com/tsungtingkuo/ensemble\n'
}
else {
	java -cp .:bin:lib/* Single_Mulan4pSCANNER $1 $2
	printf '\n'
	printf 'The output CSV file is generated.\n'
	printf 'For more detailed usage information, please check our website:\n'
	printf 'https://github.com/tsungtingkuo/ensemble\n'
}
fi

printf '\n'
