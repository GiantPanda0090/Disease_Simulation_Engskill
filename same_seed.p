 # Gnuplot script file for plotting data in file "heap.dat"
set terminal png
set output "same_seed.png"

set terminal png linewidth 1 size 1920,1080  font verdana 24


# This is to set the color
set style line 1 lc rgb "black" lw 1 pt 1
set style line 2 lc rgb "red" lw 1 pt 1

set title "The probability of disease become epidemic"

set key left top

set xlabel "Porbability of infection"
set ylabel "Number of effected individual"


data1 = "<( paste data/40_6_9/prob/prob.dat data/40_6_9/1/data.dat )"
data2 = "<( paste data/40_6_9/prob/prob.dat data/40_6_9/2/data.dat )"
data3 = "<( paste data/40_6_9/prob/prob.dat data/40_6_9/3/data.dat )"
data4 = "<( paste data/40_6_9/prob/prob.dat data/40_6_9/4/data.dat )"

set xrange [0:0.1]

MAXCOL = 3



stat data1 u 1:2
stat data2 u 1:2
stat data3 u 1:2 
stat data4 u 1:2 



plot data1 u 1:2 w lp pt 6 ps 2 title "seed1",data2 u 1:2 w lp pt 6 ps 2 title "seed2",data3 u 1:2 w lp pt 6 ps 2 title "seed3",data4 u 1:2 w lp pt 6 ps 2 title "seed4"
