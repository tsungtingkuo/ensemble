##################### COPYRIGHT NOTICE - BSD License #########################
#       Copyright (c) 2015, Regents of the University of California and
#       the pSCANNER Project
#       All rights reserved.
#
#       Redistribution and use in source and binary forms, with or without
#       modification, are permitted provided that the following conditions are
#       met:
#
#        * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#        * Redistributions in binary form must reproduce the above copyright
#       notice, this list of conditions and the following disclaimer in the
#       documentation and/or other materials provided with the distribution.
#
#       THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
#       IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
#       TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
#       PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
#       OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
#       EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
#       PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
#       PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
#       LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
#       NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
#       SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
##############################################################################

# Written by Tsung-Ting Kuo

mkdir lib
mkdir bin
mkdir arff
mkdir xml

mkdir brat_inp

mkdir brat_inp/CHF
mkdir brat_inp/CHF/training
mkdir brat_inp/CHF/testing
mkdir brat_inp/WMO
mkdir brat_inp/WMO/training
mkdir brat_inp/WMO/testing
mkdir brat_inp/KD
mkdir brat_inp/KD/training
mkdir brat_inp/KD/testing

mkdir data_sent

mkdir data_sent/CHF
mkdir data_sent/CHF/training
mkdir data_sent/CHF/testing
mkdir data_sent/WMO
mkdir data_sent/WMO/training
mkdir data_sent/WMO/testing
mkdir data_sent/KD
mkdir data_sent/KD/training
mkdir data_sent/KD/testing

mkdir single

mkdir single/ctakes_brat_out
mkdir single/ctakes_brat_out/CHF
mkdir single/ctakes_brat_out/CHF/training
mkdir single/ctakes_brat_out/CHF/testing
mkdir single/ctakes_brat_out/WMO
mkdir single/ctakes_brat_out/WMO/training
mkdir single/ctakes_brat_out/WMO/testing
mkdir single/ctakes_brat_out/KD
mkdir single/ctakes_brat_out/KD/training
mkdir single/ctakes_brat_out/KD/testing

mkdir single/metamap_brat_out
mkdir single/metamap_brat_out/CHF
mkdir single/metamap_brat_out/CHF/training
mkdir single/metamap_brat_out/CHF/testing
mkdir single/metamap_brat_out/WMO
mkdir single/metamap_brat_out/WMO/training
mkdir single/metamap_brat_out/WMO/testing
mkdir single/metamap_brat_out/KD
mkdir single/metamap_brat_out/KD/training
mkdir single/metamap_brat_out/KD/testing

curl -L https://sourceforge.net/projects/mulan/files/mulan-1-5/mulan-1.5.0.zip/download -o mulan-1.5.0.zip
unzip mulan-1.5.0.zip
cp -f mulan/dist/mulan.jar lib/mulan-1.5.0.jar
rm -rf mulan
rm mulan-1.5.0.zip

curl -L https://sourceforge.net/projects/weka/files/weka-3-7/3.7.10/weka-3-7-10.zip/download -o weka-3-7-10.zip
unzip weka-3-7-10.zip
cp -f weka-3-7-10/weka.jar lib/weka-3-7-10.jar
rm -rf weka-3-7-10
rm weka-3-7-10.zip

curl -O http://archive.apache.org/dist/commons/math/binaries/commons-math-1.2.zip
unzip commons-math-1.2.zip
cp -f commons-math-1.2/commons-math-1.2.jar lib/commons-math-1.2.jar
rm -rf commons-math-1.2
rm commons-math-1.2.zip

curl http://www.csie.ntu.edu.tw/~d97944007/utility/utility.jar -o lib/utility.jar

javac -cp .:bin:lib/* -d bin src/*.java
