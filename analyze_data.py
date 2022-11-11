import pandas as pd
import matplotlib.pyplot as plt


def aggregate_mathematical_sir ():
    
    fig, axes = plt.subplots(1, 2)
    
    # * Compartments

    agg_df = pd.read_csv('./data/stochastic_model.csv', index_col=None)
    math_df = pd.read_csv('./data/mathematical_model.csv', index_col=None)
    
    axes[0].plot(agg_df['Time'], agg_df['Susceptible'], label='Susceptible', color='blue')
    axes[0].plot(math_df['Time'], math_df['Susceptible'], linestyle='dashed', color='black')
    
    axes[0].plot(agg_df['Time'], agg_df['Infected'], label='Infected', color='red')
    axes[0].plot(math_df['Time'], math_df['Infected'], linestyle='dashed', color='black')
    
    axes[0].plot(agg_df['Time'], agg_df['Recovered'], label='Recovered', color='green')
    axes[0].plot(math_df['Time'], math_df['Recovered'], linestyle='dashed', color='black')
    
    # axes[0].plot(agg_df['Time'], agg_df['Vaccinated'], label='Recovered', color='purple')
    # axes[0].plot(math_df['Time'], math_df['Vaccinated'], linestyle='dashed', color='black')
    
    axes[0].legend(loc='upper right')
    axes[0].set_title('Number of Individuals vs Time')
    axes[0].set(xlabel='Time', ylabel='Number of Individuals')
    
    # * Show Cumulative Infected
    
    axes[1].plot(agg_df['Time'], agg_df['CumulativeInfected'], label='Recovered', color='green')
    axes[1].plot(math_df['Time'], math_df['CumulativeInfected'], linestyle='dashed', color='black')
    
    axes[1].legend(loc='upper right')
    axes[1].set_title('Cumulative Infected vs Time')
    axes[0].set(xlabel='Time', ylabel='Cumulative Infected')
    
    plt.show()


if __name__ == '__main__':
    aggregate_mathematical_sir()