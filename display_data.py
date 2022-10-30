import pandas as pd
import matplotlib.pyplot as plt
import sys
import os

if __name__ == '__main__':
    ROOT_PATH = sys.argv[1]
    FILE_NAME = sys.argv[2]
    df = pd.read_csv(os.path.join(ROOT_PATH, 'data', FILE_NAME), index_col=None)
    
    ax = plt.gca()

    df.plot(kind='line', x='Time', y='Susceptible', color='blue', ax=ax)
    df.plot(kind='line', x='Time', y='Infected', color='red', ax=ax)
    df.plot(kind='line', x='Time', y='Removed', color='green', ax=ax)

    plt.show()