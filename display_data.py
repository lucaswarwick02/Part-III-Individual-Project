import pandas as pd
import matplotlib.pyplot as plt
import matplotlib
import os

if __name__ == '__main__':
    PATH = r'C:\Users\Lucas\Documents\Projects\Part-III-Individual-Project\out\test.csv'
    df = pd.read_csv(PATH, index_col=None)
    
    ax = plt.gca()

    df.plot(kind='line', x='Time', y='Susceptible', color='blue', ax=ax)
    df.plot(kind='line', x='Time', y='Infected', color='red', ax=ax)
    df.plot(kind='line', x='Time', y='Removed', color='green', ax=ax)

    plt.show()