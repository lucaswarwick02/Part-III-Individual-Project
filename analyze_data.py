import pandas as pd
import matplotlib.pyplot as plt


def display_base_and_vac (agg_df: pd.DataFrame, math_df: pd.DataFrame, agg_vac_df: pd.DataFrame, math_vac_df: pd.DataFrame):
    
    fig, axes = plt.subplots(1, 2, figsize=(12, 5))
    
    axes[0].plot(agg_df['Time'], agg_df['Susceptible'], label='Susceptible', color='blue')
    axes[0].fill_between(agg_df['Time'], agg_df['Susceptible'] + agg_df['Susceptible_STD'], agg_df['Susceptible'] - agg_df['Susceptible_STD'], color='blue', alpha=0.2)  # type: ignore
    axes[0].plot(math_df['Time'], math_df['Susceptible'], linestyle='dashed', color='blue')
    
    axes[0].plot(agg_df['Time'], agg_df['Infected'], label='Infected', color='red')
    axes[0].fill_between(agg_df['Time'], agg_df['Infected'] + agg_df['Infected_STD'], agg_df['Infected'] - agg_df['Infected_STD'], color='red', alpha=0.2)  # type: ignore
    axes[0].plot(math_df['Time'], math_df['Infected'], linestyle='dashed', color='red')
    
    axes[0].plot(agg_df['Time'], agg_df['Recovered'], label='Recovered', color='green')
    axes[0].fill_between(agg_df['Time'], agg_df['Recovered'] + agg_df['Recovered_STD'], agg_df['Recovered'] - agg_df['Recovered_STD'], color='green', alpha=0.2)  # type: ignore
    axes[0].plot(math_df['Time'], math_df['Recovered'], linestyle='dashed', color='green')
    
    axes[0].legend(loc='upper right')
    axes[0].set_title('Number of Individuals vs Time')
    axes[0].set(xlabel='Time', ylabel='Number of Individuals')
    
    axes[1].plot(agg_vac_df['Time'], agg_vac_df['Susceptible'], label='Susceptible', color='blue')
    axes[1].fill_between(agg_vac_df['Time'], agg_vac_df['Susceptible'] + agg_vac_df['Susceptible_STD'], agg_vac_df['Susceptible'] - agg_vac_df['Susceptible_STD'], color='blue', alpha=0.2)  # type: ignore
    axes[1].plot(math_vac_df['Time'], math_vac_df['Susceptible'], linestyle='dashed', color='blue')
    
    axes[1].plot(agg_vac_df['Time'], agg_vac_df['Infected'], label='Infected', color='red')
    axes[1].fill_between(agg_vac_df['Time'], agg_vac_df['Infected'] + agg_vac_df['Infected_STD'], agg_vac_df['Infected'] - agg_vac_df['Infected_STD'], color='red', alpha=0.2)  # type: ignore
    axes[1].plot(math_vac_df['Time'], math_vac_df['Infected'], linestyle='dashed', color='red')

    axes[1].plot(agg_vac_df['Time'], agg_vac_df['Recovered'], label='Recovered', color='green')
    axes[1].fill_between(agg_vac_df['Time'], agg_vac_df['Recovered'] + agg_vac_df['Recovered_STD'], agg_vac_df['Recovered'] - agg_vac_df['Recovered_STD'], color='green', alpha=0.2)  # type: ignore
    axes[1].plot(math_vac_df['Time'], math_vac_df['Recovered'], linestyle='dashed', color='green')
    
    axes[1].plot(agg_vac_df['Time'], agg_vac_df['Vaccinated'], label='Vaccinated', color='purple')
    axes[1].fill_between(agg_vac_df['Time'], agg_vac_df['Vaccinated'] + agg_vac_df['Vaccinated_STD'], agg_vac_df['Vaccinated'] - agg_vac_df['Vaccinated_STD'], color='purple', alpha=0.2)  # type: ignore
    axes[1].plot(math_vac_df['Time'], math_vac_df['Vaccinated'], linestyle='dashed', color='purple')
    
    axes[1].legend(loc='upper right')
    axes[1].set_title('Number of Individuals vs Time')
    axes[1].set(xlabel='Time', ylabel='Number of Individuals')
    
    plt.show()


if __name__ == '__main__':
    agg_df = pd.read_csv('./data/stochastic_model.csv', index_col=None)
    math_df = pd.read_csv('./data/mathematical_model.csv', index_col=None)
    
    agg_vac_df = pd.read_csv('./data/stochastic_vac_model.csv', index_col=None)
    math_vac_df = pd.read_csv('./data/mathematical_vac_model.csv', index_col=None)
    
    display_base_and_vac(agg_df, math_df, agg_vac_df, math_vac_df)