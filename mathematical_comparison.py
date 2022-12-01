import pandas as pd
import matplotlib.pyplot as plt


if __name__ == '__main__':
    equations_df = pd.read_csv('./records/mathematical_comparison/equations.csv')
    simulations_df = pd.read_csv('./records/mathematical_comparison/simulations.csv')
    
    plt.figure(figsize=(8, 5))
    
    # * Plot the Aggregate Simulations
    plt.fill_between(simulations_df['Time'], simulations_df['Susceptible'] + simulations_df['Susceptible_STD'], simulations_df['Susceptible'] - simulations_df['Susceptible_STD'], color='orange', alpha=0.05)  # type: ignore
    plt.fill_between(simulations_df['Time'], simulations_df['Infected'] + simulations_df['Infected_STD'], simulations_df['Infected'] - simulations_df['Infected_STD'], color='green', alpha=0.2)  # type: ignore
    plt.fill_between(simulations_df['Time'], simulations_df['Recovered'] + simulations_df['Recovered_STD'], simulations_df['Recovered'] - simulations_df['Recovered_STD'], color='brown', alpha=0.05)  # type: ignore
    plt.fill_between(simulations_df['Time'], simulations_df['Hospitalised'] + simulations_df['Hospitalised_STD'], simulations_df['Hospitalised'] - simulations_df['Hospitalised_STD'], color='blue', alpha=0.2)  # type: ignore
    plt.fill_between(simulations_df['Time'], simulations_df['Dead'] + simulations_df['Dead_STD'], simulations_df['Dead'] - simulations_df['Dead_STD'], color='red', alpha=0.2)  # type: ignore
    
    # * Plot the mathematical results
    plt.plot(equations_df['Time'], equations_df['Susceptible'], color='orange', label='Susceptible', alpha=0.25)
    plt.plot(equations_df['Time'], equations_df['Infected'], color='green', label='Infected')
    plt.plot(equations_df['Time'], equations_df['Recovered'], color='brown', label='Recovered', alpha=0.25)
    plt.plot(equations_df['Time'], equations_df['Hospitalised'], color='blue', label='Hospitalised')
    plt.plot(equations_df['Time'], equations_df['Dead'], color='red', label='Dead')
    
    plt.legend(loc='upper right')
    plt.show()