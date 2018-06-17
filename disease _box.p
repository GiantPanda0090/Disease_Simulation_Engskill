 # Gnuplot script file for plotting data in file "heap.dat"
set terminal png
set output "disease_box.png"

set terminal png linewidth 1 size 1920,1080  font verdana 24


# This is to set the color


set style data boxplot 




set title "The confidence interval for disease become epidemic"



set xlabel "Porbability of infection"
set ylabel "Number of effected individual"




unset key

set xtics ("HT14" 1, "HT15" 2, "HT16" 3,"HT17" 4) scale 0.0

set yrange[1400:2500]











plot [0:5] "minmax.dat" using 1:2:3:4 with yerrorlines
