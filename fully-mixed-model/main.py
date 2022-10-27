import random


class FullyMixedSIR:
    def __init__(self, infection_rate: float, recovery_rate: float, population_size: int):
        self.INFECTION_RATE = infection_rate
        self.RECOVERY_RATE = recovery_rate
        self.population_size = population_size

        self.infectious = None
        self.removed = None
        self.susceptible = None

        self.time = None

    def initialisePopulation(self, initial_infected: int):
        self.susceptible = self.population_size - initial_infected
        self.infectious = initial_infected
        self.removed = 0

        self.time = 0

        print(f"S: {self.susceptible}, I: {self.infectious}, R: {self.removed}")

    def increaseTime(self):
        newly_infected = 0
        for i in range(self.susceptible):
            if random.random() >= self.INFECTION_RATE:
                newly_infected += 1

        newly_removed = 0
        for i in range(self.infectious):
            if random.random() >= self.RECOVERY_RATE:
                newly_removed += 1

        self.susceptible = self.susceptible - newly_infected
        self.infectious = self.infectious + newly_infected - newly_removed
        self.removed = self.removed + newly_removed

        print(f"S: {self.susceptible}, I: {self.infectious}, R: {self.removed}")


if __name__ == '__main__':
    fully_mixed_model = FullyMixedSIR(infection_rate=0.4, recovery_rate=0.04, population_size=1000)
    fully_mixed_model.initialisePopulation(initial_infected=3)

    for t in range(10):
        fully_mixed_model.increaseTime()