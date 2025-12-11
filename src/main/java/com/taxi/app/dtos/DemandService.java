package com.taxi.app.dtos;

// DemandService.java
public interface DemandService {
    DemandSnapshot getCurrentDemand(Long regionId, String equipmentCode);
}
