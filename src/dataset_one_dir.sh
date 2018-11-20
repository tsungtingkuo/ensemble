
sh ./dataset_one_dir_training.sh $1 $2 $3 $4 $5 $6 $7 $8 $9 | tee ../$1/$6_training.$1

sh ./dataset_one_dir_testing.sh $1 $2 $3 $4 $5 $6 $7 $8 $9 | tee ../$1/$6_testing.$1
