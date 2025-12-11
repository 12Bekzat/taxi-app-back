package com.taxi.app.dtos;

// DemandSnapshot.java
public class DemandSnapshot {
    private final double demandIndex; // 0..10

    public DemandSnapshot(double demandIndex) {
        this.demandIndex = demandIndex;
    }

    public double getDemandIndex() {
        return demandIndex;
    }
}

