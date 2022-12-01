import pandas as pd
import matplotlib.pyplot as plt
import sys
import os


def add_plot(axes, df: pd.DataFrame, column: str, color: str, line_alpha=1.0, fill_alpha=0.2, ignore_std=False):
    axes.plot(df['Time'], df[column], label=column, color=color, alpha=line_alpha)
    
    if not ignore_std:
        axes.fill_between(df['Time'], df[column] + df[column + '_STD'], df[column] - df[column + '_STD'], color=color, alpha=fill_alpha)
    

def create_states_plot(df: pd.DataFrame, axes: plt.Axes):
    add_plot(axes, df, 'Susceptible', 'orange', line_alpha=0.25, fill_alpha=0.05)
    add_plot(axes, df, 'Infected', 'green')
    add_plot(axes, df, 'Recovered', 'brown', line_alpha=0.25, fill_alpha=0.05)
    add_plot(axes, df, 'Hospitalised', 'blue')
    add_plot(axes, df, 'Dead', 'red')
    
    axes.legend(loc='upper right')
    axes.set(title='Number of Individuals vs Time', xlabel='Time', ylabel='Number of Individuals')


def create_totals_plot(df: pd.DataFrame, axes: plt.Axes):
    add_plot(axes, df, 'Infected', 'green', ignore_std=False)
    add_plot(axes, df, 'Hospitalised', 'blue', ignore_std=False)
    add_plot(axes, df, 'Dead', 'red', ignore_std=False)
    
    axes.legend(loc='upper right')
    axes.set(title='Total Individuals vs Time', xlabel='Time', ylabel='Total Individuals')


if __name__ == '__main__':
    ROOT_FOLDER = sys.argv[1]
    RUN = sys.argv[2]

    states_df = pd.read_csv(os.path.join(ROOT_FOLDER, 'out', RUN, 'states.csv'), index_col=None)
    totals_df = pd.read_csv(os.path.join(ROOT_FOLDER, 'out', RUN, 'totals.csv'), index_col=None)
    
    fig, axes = plt.subplots(1, 2, figsize=(10, 4))
    
    create_states_plot(states_df, axes[0])
    create_totals_plot(totals_df, axes[1])
    
    plt.setp(axes, ylim=(0, 2500))
    
    plt.savefig(os.path.join(ROOT_FOLDER, 'out', RUN, 'plots.png'), bbox_inches='tight')