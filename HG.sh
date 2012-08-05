newfiles=`hg status | grep '?' | cut -c 3-`
for f in $newfiles; do hg add $f;done

delfiles=`hg status | grep '!' | cut -c 3-`
#for f in $newfiles; do hg delete $f;done
