package com.lucaswarwick02.mains;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.lucaswarwick02.HelperFunctions;
import com.lucaswarwick02.components.Epidemic;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;
import com.lucaswarwick02.vaccination.AbstractStrategy;
import com.lucaswarwick02.vaccination.StrategyFactory;
import com.lucaswarwick02.vaccination.StrategyFactory.StrategyType;

public class CriticalMain {
        public static void main(String[] args) {
                System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");

                // Store arguments from command line
                String rootFolder = args[0];

                // Create a new data folder for this current run-through
                File dataFolder = new File(rootFolder, "out");
                dataFolder.mkdir();

                runSectionBaseline("er_critical_baseline", dataFolder, NetworkType.ERDOS_REYNI);
                runSectionBaseline("ba_critical_baseline", dataFolder, NetworkType.BARABASI_ALBERT);

                runSection("critical_section_1a", dataFolder, NetworkType.ERDOS_REYNI,
                                StrategyType.RANDOM);
                runSection("critical_section_1b", dataFolder, NetworkType.BARABASI_ALBERT,
                                StrategyType.RANDOM);
                runSection("critical_section_2a", dataFolder, NetworkType.BARABASI_ALBERT,
                                StrategyType.HIGHEST);
                runSection("critical_section_2b", dataFolder, NetworkType.BARABASI_ALBERT,
                                StrategyType.LOWEST);
                runSection("critical_section_3a", dataFolder, NetworkType.BARABASI_ALBERT,
                                StrategyType.OLDEST);
                runSection("critical_section_3b", dataFolder, NetworkType.BARABASI_ALBERT,
                                StrategyType.YOUNGEST);
        }

        private static void runSectionBaseline(String folderName, File dataFolder, NetworkType networkType) {
                File runFolder = new File(dataFolder, folderName);
                runFolder.mkdir();

                determineCriticalPointBaseline(runFolder, networkType);
        }

        private static void runSection(String folderName, File dataFolder, NetworkType networkType,
                        StrategyType strategyType) {
                File runFolder = new File(dataFolder, folderName);
                runFolder.mkdir();

                float[] rhos = new float[] { 0f, 0.15f, 0.30f, 0.45f };

                for (float rho : rhos) {
                        determineCriticalPoint(runFolder, networkType, strategyType, 0, rho);
                }
        }

        private static void determineCriticalPointBaseline(File runFolder, NetworkType networkType) {
                float infectionRateLowerBount = 0f;
                float infectionRateUpperBound = 0.03f;
                float infectionStepSize = 0.0025f;

                float recoveryRate = 0.06f;
                float hospitalisationRate = 0.04f;
                float mortalityRate = 0.1f;

                Map<Float, Double> results = new HashMap<>();

                HelperFunctions.LOGGER.info(String.format("Running: %s", networkType.toString()));

                for (float infectionRate : HelperFunctions.createIntervals(infectionRateLowerBount,
                                infectionRateUpperBound,
                                infectionStepSize)) {
                        AbstractStrategy strategy = StrategyFactory.getStrategy(StrategyType.RANDOM, 0);

                        Epidemic epidemic = new Epidemic();
                        epidemic.infectionRate = infectionRate;
                        epidemic.recoveryRate = recoveryRate;
                        epidemic.hospitalisationRate = hospitalisationRate;
                        epidemic.mortalityRate = mortalityRate;

                        HelperFunctions.LOGGER.info(String.format("... Infection Rate: %.04f", infectionRate));

                        Map<String, double[]> totals = HelperFunctions.stochasticSimulationTotals(networkType, strategy,
                                        epidemic,
                                        true);

                        double totalInfected = totals.get("Infected")[totals.get("Infected").length - 1];

                        results.put(infectionRate, totalInfected);
                }

                // Save results to a file within runFolder
                String header = "InfectionRate,TotalInfected";
                try (PrintWriter writer = new PrintWriter(new File(runFolder,
                                String.format("%s.csv", networkType.toString())),
                                "UTF-8")) {
                        writer.write(header + "\n");
                        results.forEach((infectionRate, totalInfected) -> writer
                                        .write(String.format("%.04f,%f%n", infectionRate, totalInfected)));
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private static void determineCriticalPoint(File runFolder, NetworkType networkType, StrategyType strategyType,
                        int timeDelay, float rho) {
                float infectionRateLowerBount = 0f;
                float infectionRateUpperBound = 0.03f;
                float infectionStepSize = 0.0025f;

                float recoveryRate = 0.06f;
                float hospitalisationRate = 0.04f;
                float mortalityRate = 0.1f;

                Map<Float, Double> results = new HashMap<>();

                HelperFunctions.LOGGER.info(String.format("Running: %s, %s, %d, %.03f", networkType.toString(),
                                strategyType.toString(), timeDelay, rho));

                for (float infectionRate : HelperFunctions.createIntervals(infectionRateLowerBount,
                                infectionRateUpperBound,
                                infectionStepSize)) {
                        AbstractStrategy strategy = StrategyFactory.getStrategy(strategyType, rho);

                        Epidemic epidemic = new Epidemic();
                        epidemic.infectionRate = infectionRate;
                        epidemic.recoveryRate = recoveryRate;
                        epidemic.hospitalisationRate = hospitalisationRate;
                        epidemic.mortalityRate = mortalityRate;

                        HelperFunctions.LOGGER.info(String.format("... Infection Rate: %.04f", infectionRate));

                        Map<String, double[]> totals = HelperFunctions.stochasticSimulationTotals(networkType, strategy,
                                        epidemic,
                                        true);

                        double totalInfected = totals.get("Infected")[totals.get("Infected").length - 1];

                        results.put(infectionRate, totalInfected);
                }

                HelperFunctions.LOGGER.info("... Done");

                // Save results to a file within runFolder
                String header = "InfectionRate,TotalInfected";
                try (PrintWriter writer = new PrintWriter(new File(runFolder,
                                String.format("%s_%s_%d_%.03f.csv", networkType.toString(), strategyType.toString(),
                                                timeDelay, rho)),
                                "UTF-8")) {
                        writer.write(header + "\n");
                        results.forEach((infectionRate, totalInfected) -> writer
                                        .write(String.format("%.04f,%f%n", infectionRate, totalInfected)));
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
