import pandas as pd
import matplotlib.pyplot as plt


def add_plot(axes, df: pd.DataFrame, column: str, color: str, line_alpha=1.0, fill_alpha=0.2):
    axes.plot(df['Time'], df[column], label=column, color=color, alpha=line_alpha)
    axes.fill_between(df['Time'], df[column] + df[column + '_STD'], df[column] - df[column + '_STD'], color=color, alpha=fill_alpha)


def display_base_and_vac (agg_df: pd.DataFrame, agg_vac_df: pd.DataFrame):
    
    y_max = agg_df.iloc[0]['Susceptible'] + agg_df.iloc[0]['Infected']
    
    fig, axes = plt.subplots(2, 2, figsize=(12, 10))
    plt.ylim(0, y_max)
    
    legend_loc = 'upper right'
    
    # * No Vaccination
    
    add_plot(axes[0][0], agg_df, 'Susceptible', 'blue', line_alpha=0.25, fill_alpha=0.05)
    add_plot(axes[0][0], agg_df, 'Infected', 'orange')
    add_plot(axes[0][0], agg_df, 'Recovered', 'green', line_alpha=0.25, fill_alpha=0.05)
    add_plot(axes[0][0], agg_df, 'Hospitalised', 'brown')
    add_plot(axes[0][0], agg_df, 'Dead', 'red')

    axes[0][0].legend(loc=legend_loc)
    axes[0][0].set_title('Number of Individuals vs Time')
    axes[0][0].set(xlabel='Time', ylabel='Number of Individuals')
    
    add_plot(axes[1][0], agg_df, 'Hospitalised', 'orange')
    add_plot(axes[1][0], agg_df, 'Dead', 'red')
    
    axes[1][0].legend(loc=legend_loc)
    axes[1][0].set_title('Number of Individuals vs Time')
    axes[1][0].set(xlabel='Time', ylabel='Number of Individuals')
    
    # * Global Vaccination
    
    add_plot(axes[0][1], agg_vac_df, 'Susceptible', 'blue')
    add_plot(axes[0][1], agg_vac_df, 'Infected', 'red')
    add_plot(axes[0][1], agg_vac_df, 'Recovered', 'green')
    add_plot(axes[0][1], agg_vac_df, 'Vaccinated', 'purple')
    
    axes[0][1].legend(loc=legend_loc)
    axes[0][1].set_title('Number of Individuals vs Time')
    axes[0][1].set(xlabel='Time', ylabel='Number of Individuals')
    
    add_plot(axes[1][1], agg_vac_df, 'Hospitalised', 'orange')
    add_plot(axes[1][1], agg_vac_df, 'Dead', 'red')
    
    axes[1][1].legend(loc=legend_loc)
    axes[1][1].set_title('Number of Individuals vs Time')
    axes[1][1].set(xlabel='Time', ylabel='Number of Individuals')
    
    plt.setp(axes, ylim=(0, 2500))
    plt.show()
    

if __name__ == '__main__':
    agg_df = pd.read_csv('./data/no_vaccination.csv', index_col=None)
    
    agg_vac_df = pd.read_csv('./data/global_vaccination.csv', index_col=None)
    
    display_base_and_vac(agg_df, agg_vac_df)