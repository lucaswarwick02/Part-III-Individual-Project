package com.lucaswarwick02.mains;

import com.lucaswarwick02.networks.AbstractNetwork;
import com.lucaswarwick02.networks.NetworkFactory;
import com.lucaswarwick02.networks.NetworkFactory.NetworkType;

public class TestMain {
    public static void main(String[] args) {
        AbstractNetwork ba = NetworkFactory.getNetwork(NetworkType.BARABASI_ALBERT);
        ba.generateNetwork();

        AbstractNetwork er = NetworkFactory.getNetwork(NetworkType.ERDOS_REYNI);
        er.generateNetwork();

        System.out.println("BA:");
        System.out.println("... <k> = " + ba.getAverageDegree());
        System.out.println("... Components = " + ba.calculateNumberOfComponents());

        System.out.println("ER:");
        System.out.println("... <k> = " + er.getAverageDegree());
        System.out.println("... Components = " + er.calculateNumberOfComponents());
    }
}
