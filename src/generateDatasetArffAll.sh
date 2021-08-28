
# $1 = disease ("CHF", "WMO", "KD")

sh dataset_one_dir.sh arff ../data_sent/$1 ../conf/$1_ANNOTATIONS.txt ../data_sent/$1 ../brat_inp/$1 $1 ALL ../single ../conf/NLP_TOOLS.txt

printf '\n'
