 # Gnuplot script file for plotting data in file "heap.dat"
set terminal png
set output "ht16.png"

set terminal png linewidth 1 size 1920,1080  font verdana 24


# This is to set the color
set style line 1 lc rgb "black" lw 1 pt 1
set style line 2 lc rgb "red" lw 1 pt 1

set title "The probability of disease become epidemic"

set key right bottom

set xlabel "Porbability of infection"
set ylabel "Number of effected individual"

set style fill transparent solid 0.2 noborder


data1 = "<( paste data/40_6_9/prob/prob.dat data/40_6_9/*/data.dat data/40_6_9/min.dat data/40_6_9/max.dat)"
data2 = "<( paste data/50_1_12/prob/prob.dat data/50_1_12/*/data.dat data/50_1_12/min.dat data/50_1_12/max.dat)"
data3 = "<( paste data/50_3_9/prob/prob.dat data/50_3_9/*/data.dat data/50_3_9/min.dat data/50_3_9/max.dat)"
data4 = "<( paste data/50_4_8/prob/prob.dat data/50_4_8/*/data.dat data/50_4_8/min.dat data/50_4_8/max.dat)"
MAXCOL = 20






stat data1 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1)
stat data2 u 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1)
stat data3 u 1:(sum [col=1:MAXCOL] column(col))/(MAXCOL-1) 
stat data4 u 1:(sum [col=1:MAXCOL] column(col))/(MAXCOL-1) 



plot data2 u 1:22:23 with filledcurves title '95% confidence for HT16', \
     '' using 1:(sum [col=2:MAXCOL] column(col))/(MAXCOL-1) with lp lt 1 pt 7 ps 1.5 lw 3 title 'HT16',1250 lt rgb "red"
