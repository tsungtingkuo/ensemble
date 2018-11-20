
python ./$1_header_all.py $2 $3 $4/training $5/training $9

for i in `ls $2/training/*.*`
do
        sh ./dataset_one_note.sh $1 $i $3 $4/training $5/training $6 $7 $8 $9 training
done
