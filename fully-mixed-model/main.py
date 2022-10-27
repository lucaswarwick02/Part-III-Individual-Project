import random
import pandas as pd
import matplotlib.pyplot as plt


class FullyMixedSIR:
    def __init__(self, infection_rate: float, recovery_rate: float, population_size: int):
        self.INFECTION_RATE = infection_rate
        self.RECOVERY_RATE = recovery_rate
        self.population_size = population_size

        self.infectious = None
        self.removed = None
        self.susceptible = None

        self.time = None

        self.dataframe = None

    def initialise_population(self, initial_infected: int):
        self.susceptible = self.population_size - initial_infected
        self.infectious = initial_infected
        self.removed = 0

        self.time = 0

        self.dataframe = pd.DataFrame(columns=['Time', 'Susceptible', 'Infectious', 'Removed'])

        self.dataframe.loc[self.time] = [self.time, self.susceptible, self.infectious, self.removed]

    def increase_time(self):
        self.time += 1

        newly_infected = 0
        for i in range(self.susceptible):
            if random.random() <= self.INFECTION_RATE:
                newly_infected += 1

        newly_removed = 0
        for i in range(self.infectious):
            if random.random() <= self.RECOVERY_RATE:
                newly_removed += 1

        self.susceptible = self.susceptible - newly_infected
        self.infectious = self.infectious + newly_infected - newly_removed
        self.removed = self.removed + newly_removed

        self.dataframe.loc[self.time] = [self.time, self.susceptible, self.infectious, self.removed]

    def save_model(self, filename='fully_mixed_model.csv'):
        self.dataframe.to_csv(filename, index=False)

    def view_model(self):
        self.dataframe.plot(x='Time', y=['Susceptible', 'Infectious', 'Removed'], kind='line', figsize=(10, 10))
        plt.show()


if __name__ == '__main__':
    fully_mixed_model = FullyMixedSIR(infection_rate=0.04, recovery_rate=0.035, population_size=1000)
    fully_mixed_model.initialise_population(initial_infected=3)

    for t in range(100):
        fully_mixed_model.increase_time()

    fully_mixed_model.save_model()
    fully_mixed_model.view_model()
