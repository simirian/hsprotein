#!/bin/bash

if [[ $# != 2 ]]; then
    echo This command requires two arguments
    echo   runall.sh INDIR OUTDIR
    exit 1
fi

echo "STARTING EXECUTION AT $(date)"

in=$(realpath $1)
out=$(realpath $2)
dir=$(which $0)
mkdir -p $2
cd ${dir%/*}
mvn compile

for file in $(ls $in); do
    if [[ $file == *_P.cif ]]; then
        if [[ ! -e $out/$file ]]; then
            echo "Running with \"$in/$file $out\""
            date +"%n%nRunning with \"$in/$file $out\" at %c" >> $out/output.log
            mvn exec:java -Dexec.args="$in/$file $out" >> $out/output.log
            date +"Finished at %c" >> $out/output.log
        else
            echo "skipping because $out/$file exists"
        fi
    fi
done

echo "EXECUTION FINISHED AT $(date)"
