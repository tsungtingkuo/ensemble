
# $1 = disease ("CHF", "WMO", "KD", "KD_Priv")

python xml_label.py ../conf/$1_ANNOTATIONS.txt | tee ../xml/$1_ANNOTATIONS.xml
