# Contents

- [Contents](#contents)
- [Introduction](#introduction)
  - [Generation](#generation)

# Introduction
This project was built for my Part III Individual Project at the University of Southampton. The project is on *How Vaccine Distribution Effects Transmission of a Virus*, with the practical aspect of it being based around comparing different immunization techniques, in conjunction with different underlying networks, to minimise the spread of an epidemic.

The project is split into two key parts, the generation of the data and the analysis of the data.

## Generation
The data is generated in a Maven project so that it utilises the speed benefits of Java. The output data itself depends on which batch file is run:
* `run_mathematical_plot.bat`: Runs and compares the mathematical derivations to a homogeneous stochastic simulation to varify the epidemic parameters are refined.
* `run_stochastic_plot.bat`: This will create a single run folder containing the results from the aggregate simulation as well as running python on them to visualise the data.
* `run_grid_search.bat`: Creates various different subfolders containing the different variations of the grid search parameters. The contents of which are the same as running a stochastic model, without the python plots.
