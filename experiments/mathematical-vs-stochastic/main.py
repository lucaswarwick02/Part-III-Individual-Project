from mathematical_model import MathematicalModel

if __name__ == '__main__':
    # Initialise the mathematical model
    mathematical_model = MathematicalModel(n=1000, infection_rate=0.4, recovery_rate=0.04)
    mathematical_model.initialise_model(3)

    # Define the duration of t
    SIMULATION_DURATION = 10

    # Simulate the mathematical model and print the state each time
    for i in range(1, SIMULATION_DURATION + 1):
        mathematical_model.increase_time()
        mathematical_model.print_state()
