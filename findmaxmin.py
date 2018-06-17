#!/usr/bin/python3

import os
import sys

def minmax():
    command_list=[]
    command_list.append(('paste data/50_1_12/*/data.dat > columns.txt','data/50_1_12/min.dat','data/50_1_12/max.dat'))
    command_list.append(('paste data/50_3_9/*/data.dat > columns.txt','data/50_3_9/min.dat','data/50_3_9/max.dat'))
    command_list.append(('paste data/50_4_8/*/data.dat > columns.txt','data/50_4_8/min.dat','data/50_4_8/max.dat'))
    command_list.append(('paste data/40_6_9/*/data.dat > columns.txt','data/40_6_9/min.dat','data/40_6_9/max.dat'))

    for command,min_path,max_path in command_list:
        min_list=[]
        max_list=[]
        print('command: '+ command)
        print('min_path: '+ min_path)

        print('max_path: '+ max_path)

        os.system(command)
        with open('columns.txt') as f:
            row=f.readline()

            while row:

                int_perrow = []
                content = [x.strip() for x in row.split()]
                for item in content:
                    if item != '':
                        int_perrow.append(int(item))
                answer_tuple=mean_confidence_interval(int_perrow)
                min_list.append(str(answer_tuple[1])+'\n')
                max_list.append(str(answer_tuple[2])+'\n')

                row = f.readline()
        f.close()
        print (str(min_list))
        with open(min_path, 'w+') as f:
            f.writelines(min_list)
            f.close()
        with open(max_path, 'w+') as f:
            f.writelines(max_list)
            f.close()
        os.remove('columns.txt')




    return 'done'

#https://stackoverflow.com/questions/15033511/compute-a-confidence-interval-from-sample-data
import numpy as np
import scipy as sp
import scipy.stats

def mean_confidence_interval(data, confidence=0.95):
    a = 1.0*np.array(data)
    n = len(a)
    m, se = np.mean(a), scipy.stats.sem(a)
    h = se * sp.stats.t._ppf((1+confidence)/2., n-1)
    return m, m-h, m+h


if __name__ == '__main__':
    sys.exit(minmax())
