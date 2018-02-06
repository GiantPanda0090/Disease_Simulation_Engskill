 # Gnuplot script file for plotting data in file "heap.dat"
set terminal png
set output "disease.png"

set terminal png linewidth 1 size 1360,768  font verdana 24


# This is to set the color
set style line 1 lc rgb "black" lw 1 pt 1
set style line 2 lc rgb "red" lw 1 pt 1

set title "The pefromence of using skew heap vs linked list as pirority queue"

set key left top

set xlabel "Porbability of infection"
set ylabel "Number of effected individual"

data1 = "<( paste final_data.dat )"

stat data1 u 1:2

plot data1 u 1:2 w lp pt 6 ps 2 title "probability"