
python ./$1_header_all.py $2 $3 $4/testing $5/testing $9

for i in `ls $2/testing/*.*`
do
        sh ./dataset_one_note.sh $1 $i $3 $4/testing $5/testing $6 $7 $8 $9 testing
done
