import pandas as pd
import matplotlib.pyplot as plt
import sys
from os import listdir
import os
import statistics

if __name__ == '__main__':
    ROOT_PATH = sys.argv[1]
    FOLDER_NAME = sys.argv[2]
    FOLDER_PATH = os.path.join(ROOT_PATH, 'data', FOLDER_NAME)
    
    df = pd.read_csv(os.path.join(FOLDER_PATH, 'model_aggregate.csv'), index_col=None)
    
    # * Number of Individuals vs Time 
    fig, ax = plt.subplots()
    
    ax.plot(df['Time'], df['SusceptibleMean'], color='blue')
    ax.plot(df['Time'], df['InfectedMean'], color='red')
    ax.plot(df['Time'], df['RemovedMean'], color='green')
    
    ax.fill_between(df['Time'], df['SusceptibleMax'], df['SusceptibleMin'], color='blue', alpha=0.2)
    ax.fill_between(df['Time'], df['InfectedMax'], df['InfectedMin'], color='red', alpha=0.2)
    ax.fill_between(df['Time'], df['RemovedMax'], df['RemovedMin'], color='green', alpha=0.2)
    
    plt.xlabel('Time')
    plt.ylabel('Number of Individuals')
    plt.title('Number of Individuals vs Time')
    
    plt.show()
    
    # * Cumulative Infections vs Time
    fig, ax = plt.subplots()
    
    ax.plot(df['Time'], df['CumulativeInfectedMean'], 'red')
    
    ax.fill_between(df['Time'], df['CumulativeInfectedMax'], df['CumulativeInfectedMin'], color='red', alpha=0.2)
    
    plt.xlabel('Time')
    plt.ylabel('Cumulative Infections')
    plt.title('Cumulative Infections vs Time')
    
    plt.show()
    