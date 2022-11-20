package com.lucaswarwick02.networks;

import java.util.ArrayList;
import java.util.Random;

public class BarabasiAlbertNetwork extends AbstractNetwork {

    Random r = new Random();

    int m;

    public BarabasiAlbertNetwork(int m) {
        super();
        this.m = m;
    }

    @Override
    public void generateNetwork(int numberOfNodes) {
        this.nodes = new ArrayList<>();
    }
}