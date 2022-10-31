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
    print(FOLDER_PATH)

    dfs = []
    for file in listdir(FOLDER_PATH):
        FILE_PATH = os.path.join(FOLDER_PATH, file)
        dfs.append(pd.read_csv(FILE_PATH, index_col=None))

    # Get number of time intervals
    rows, columns = dfs[0].shape

    mins = []
    maxs = []

    agg_df = pd.DataFrame(columns=['Time', 'Susceptible_Max', 'Susceptible_Min', 'Susceptible_Mean', 'Infected_Max', 'Infected_Min', 'Infected_Mean', 'Removed_Max', 'Removed_Min', 'Removed_Mean'])

    for i in range(rows):
        series = pd.Series({
            'Time': i,
            'Susceptible_Max': max([df.iloc[i]['Susceptible'] for df in dfs]),
            'Susceptible_Min': min([df.iloc[i]['Susceptible'] for df in dfs]),
            'Susceptible_Mean': statistics.mean([df.iloc[i]['Susceptible'] for df in dfs]),
            'Infected_Max': max([df.iloc[i]['Infected'] for df in dfs]),
            'Infected_Min': min([df.iloc[i]['Infected'] for df in dfs]),
            'Infected_Mean': statistics.mean([df.iloc[i]['Infected'] for df in dfs]),
            'Removed_Max': max([df.iloc[i]['Removed'] for df in dfs]),
            'Removed_Min': min([df.iloc[i]['Removed'] for df in dfs]),
            'Removed_Mean': statistics.mean([df.iloc[i]['Removed'] for df in dfs])
        }, dtype='float32')
        agg_df = pd.concat([agg_df.astype('float32'), series.to_frame().T], ignore_index=True)

    fig, ax = plt.subplots()
    ax.plot(agg_df['Time'], agg_df['Susceptible_Mean'], color = 'blue')
    ax.plot(agg_df['Time'], agg_df['Infected_Mean'], color = 'red')
    ax.plot(agg_df['Time'], agg_df['Removed_Mean'], color = 'green')
    ax.fill_between(agg_df['Time'], agg_df['Susceptible_Max'], agg_df['Susceptible_Min'], color='blue', alpha=0.25)
    ax.fill_between(agg_df['Time'], agg_df['Infected_Max'], agg_df['Infected_Min'], color='red', alpha=0.25)
    ax.fill_between(agg_df['Time'], agg_df['Removed_Max'], agg_df['Removed_Min'], color='green', alpha=0.25)
    plt.show()