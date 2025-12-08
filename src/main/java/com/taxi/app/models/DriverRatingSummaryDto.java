package com.taxi.app.models;
import com.taxi.app.repos.DriverRatingDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DriverRatingSummaryDto {
    private double averageScore;
    private long ratingsCount;
    private List<DriverRatingDto> ratings;
}