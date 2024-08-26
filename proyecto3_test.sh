#!/bin/bash

range_start=2
range_end=255
# 7 algorithms
num_ranges=7
range_size=$(( (range_end - range_start + 1) / num_ranges ))

generate_sequential_value() {
    local start=$1
    local end=$2
    local index=$3
    local step=$(( (end - start) / 8 )) 
    echo $(( start + (index % 8) * step ))
}

for i in {1..7}
do
    selected_range=$(( (i - 1) % num_ranges ))

    subrange_start=$(( range_start + selected_range * range_size ))
    subrange_end=$(( subrange_start + range_size - 1 ))
  
    if [ $selected_range -eq $((num_ranges - 1)) ]; then
	subrange_end=$range_end
    fi
    
    valor_w=$(generate_sequential_value $subrange_start $subrange_end $i)
    valor_h=$(generate_sequential_value $subrange_start $subrange_end $i)

    
    java -jar ./proyecto3/target/proyecto3.jar -g -w $valor_w -h $valor_h > ./proyecto3_ejemplos/${i}.mze
    java -jar ./proyecto3/target/proyecto3.jar ./proyecto3_ejemplos/${i}.mze > ./proyecto3_ejemplos/${i}.svg

    echo "Ejecución $i con valores w=$valor_w y h=$valor_h en el subrango $subrange_start-$subrange_end completada, archivos guardados como ${i}.mze y ${i}.svg."
done


# for i in {1..3}
# do

#   valor_w=$(shuf -i 190-215 -n 1)
#   valor_h=$(shuf -i 190-215 -n 1)

#   java -jar target/proyecto3.jar -g -w $valor_w -h $valor_h > ../../ejemplos/${i}.mze
#   java -jar target/proyecto3.jar ../../ejemplos/${i}.mze > ../../ejemplos/${i}.svg

#   echo "Ejecución $i con valores w=$valor_w y h=$valor_h completada, archivos guardados como ${i}.mze y ${i}.svg."
# done
