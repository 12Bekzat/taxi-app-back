package com.taxi.app.services;

import com.taxi.app.dtos.DemandService;
import com.taxi.app.dtos.DemandSnapshot;
import org.springframework.stereotype.Service;

@Service
public class StubDemandService implements DemandService {

    @Override
    public DemandSnapshot getCurrentDemand(Long regionId, String equipmentCode) {
        if (regionId == null) {
            return new DemandSnapshot(5.0); // дефолт: средний спрос
        }

        long mod = Math.abs(regionId % 3);

        double idx;
        if (mod == 0) {
            idx = 8.0; // высокий
        } else if (mod == 1) {
            idx = 5.0; // средний
        } else {
            idx = 2.0; // низкий
        }

        return new DemandSnapshot(idx);
    }
}
