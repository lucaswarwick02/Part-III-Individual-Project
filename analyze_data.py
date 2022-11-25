import pandas as pd
import matplotlib.pyplot as plt


def add_plot(axes, df: pd.DataFrame, column: str, color: str):
    axes.plot(df['Time'], df[column], label=column, color=color)
    axes.fill_between(df['Time'], df[column] + df[column + '_STD'], df[column] - df[column + '_STD'], color=color, alpha=0.2)


def display_base_and_vac (agg_df: pd.DataFrame, agg_vac_df: pd.DataFrame):
    
    y_max = agg_df.iloc[0]['Susceptible'] + agg_df.iloc[0]['Infected']
    
    fig, axes = plt.subplots(1, 3, figsize=(14, 4))
    
    legend_loc = 'upper right'
    
    # * No Vaccination
    
    add_plot(axes[0], agg_df, 'Susceptible', 'blue')
    add_plot(axes[0], agg_df, 'Infected', 'orange')
    add_plot(axes[0], agg_df, 'Recovered', 'green')
    add_plot(axes[0], agg_df, 'Hospitalised', 'yellow')
    add_plot(axes[0], agg_df, 'Dead', 'red')

    axes[0].legend(loc=legend_loc)
    axes[0].set_title('Number of Individuals vs Time')
    axes[0].set(xlabel='Time', ylabel='Number of Individuals')
    
    # * Global Vaccination
    
    add_plot(axes[1], agg_vac_df, 'Susceptible', 'blue')
    add_plot(axes[1], agg_vac_df, 'Infected', 'orange')
    add_plot(axes[1], agg_vac_df, 'Recovered', 'green')
    add_plot(axes[1], agg_vac_df, 'Vaccinated', 'purple')
    add_plot(axes[1], agg_vac_df, 'Hospitalised', 'yellow')
    add_plot(axes[1], agg_vac_df, 'Dead', 'red')
    
    axes[1].legend(loc=legend_loc)
    axes[1].set_title('Number of Individuals vs Time')
    axes[1].set(xlabel='Time', ylabel='Number of Individuals')
    
    # * Cumulative Infected vs Time
    
    axes[2].plot(agg_df['Time'], agg_df['CumulativeInfected'], linestyle='dashed', label='No Vaccination', color='black')
    axes[2].plot(agg_vac_df['Time'], agg_vac_df['CumulativeInfected'], label='Global Vacciantion', color='black')
    
    axes[2].legend(loc=legend_loc)
    axes[2].set_title('Cumulative Infected vs Time')
    axes[2].set(xlabel='Time', ylabel='Cumulative Infected')
    plt.ylim(0, y_max)
    
    plt.show()
    

if __name__ == '__main__':
    agg_df = pd.read_csv('./data/no_vaccination.csv', index_col=None)
    
    agg_vac_df = pd.read_csv('./data/global_vaccination.csv', index_col=None)
    
    display_base_and_vac(agg_df, agg_vac_df)