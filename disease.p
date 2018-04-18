 # Gnuplot script file for plotting data in file "heap.dat"
set terminal png
set output "disease.png"

set terminal png linewidth 1 size 1920,1080  font verdana 24


# This is to set the color
set style line 1 lc rgb "black" lw 1 pt 1
set style line 2 lc rgb "red" lw 1 pt 1

set title "The probability of disease become epidemic"

set key left top

set xlabel "Porbability of infection"
set ylabel "Number of effected individual"


data1 = "<( paste data/40_6_9/prob/prob.dat data/40_6_9/*/data.dat )"
data2 = "<( paste data/50_1_12/prob/prob.dat data/50_1_12/*/data.dat )"
data3 = "<( paste data/50_3_9/prob/prob.dat data/50_3_9/*/data.dat )"
data4 = "<( paste data/50_4_8/prob/prob.dat data/50_4_8/*/data.dat )"

MAXCOL = 3

set xrange [-0:0.1]


stat data1 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1)
stat data2 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1)
stat data3 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) 
stat data4 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) 



plot data1 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) w lp pt 6 ps 2 title "HT17",data2 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) w lp pt 6 ps 2 title "HT16",data3 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) w lp pt 6 ps 2 title "HT15",data4 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) w lp pt 6 ps 2 title "HT14", 1250 lt rgb "red",800 lt rgb "blue"
