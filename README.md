# The Effect of Vaccine Distribution on the Transmission of a Virus

This project was built for my Part III Individual Project at the University of Southampton. The project is on *How Vaccine Distribution Effects Transmission of a Virus*, with the practical aspect of it being based around comparing different immunization techniques, in conjunction with different underlying networks, to minimise the spread of an epidemic.


## Project Structure
The source code for this project is split into two parts; a Maven project built in Java for generating and saving the results; and the Jupyter Notebook files for plotting and visualizing the results. These can be found in thr `network-models` and `notebooks` folders, respectively. 

Each strategies results can be found in their own notebook files, with additional notebooks for creating the figures seen in the final report for this module.
## Optimizations
When running any of the simualations, the project will run much quicker with multiple cores due to Java spawning as many threads as possible to run the simualtions in parallel. 

The project benefits from a powerfull CPU (single-core speed + number of threads). On a laptop (3200MHz/6 threads), 120 simulations (one runthrough) took ~100s whereas my PC (3750Mhz/12 threads) took ~12s. 
## Running Tests
The project has unit tests which are run before any type of strategy is run; however, can also be run separately via the `run_testing.bat` file located in the root directory. 
## Run Locally
Each strategy has it's own separate batch file which can be used to create a random folder and store the contents of the simulations in. The files are as follows:

- `run_results.bat`: Strategies 1, 2 and 3
- `run_heatmap.bat`: Strategy 4
- `run_mixed.bat`: Strategy 5
- `run_timedependent.bat`: Strategy 6
- `run_thresholddependent.bat`: Strategy 7
- `run_testing.bat`: Runs testing separtely
