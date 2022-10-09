import logging
import os

logging.basicConfig(format='%(asctime)s %(name)s - %(message)s', datefmt='%H:%M:%S', level=logging.INFO)
logger = logging.getLogger(__name__)


class MathematicalModel:
    def __init__(self, n: int, infection_rate: float, recovery_rate: float):
        """
        Initialise the object

        :param n: Total population
        :param infection_rate: Rate of susceptible agents becoming infected
        :param recovery_rate: Rate of infected agents becoming recovered
        """
        self.n = n
        self.infection_rate = infection_rate
        self.recovery_rate = recovery_rate

        # Values for S, I, R and t
        self.r = None
        self.i = None
        self.s = None
        self.t = None

    def initialise_model(self, initial_infected: int):
        """
        Initialise the model at t=0

        :param initial_infected: Number of initial infected agents
        """
        # Initialise the model with an `initial_infected` number of agents
        self.s = self.n - initial_infected
        self.i = initial_infected
        self.r = 0
        self.t = 0

        logger.info("initialised")

    def increase_time(self):
        """
        Increase `self.t` by 1. This will update `self.s`, `self.i` and `self.r`
        """
        self.t += 1

        new_infected = self.infection_rate * self.i * self.s / self.n
        new_recovered = self.recovery_rate * self.i

        new_s = self.s - new_infected
        new_i = self.i + new_infected - new_recovered
        new_r = self.r + new_recovered

        self.s = new_s
        self.i = new_i
        self.r = new_r

    def print_state(self):
        """
        Print the current state of the model
        """
        logger.info(f't = {self.t}')
        logger.info(f' - s = {self.s}')
        logger.info(f' - i = {self.i}')
        logger.info(f' - r = {self.r}')
        logger.info(f' - N = {self.s + self.i + self.r}')
