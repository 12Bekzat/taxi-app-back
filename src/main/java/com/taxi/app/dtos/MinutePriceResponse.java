package com.taxi.app.dtos;

public record MinutePriceResponse(
        String equipmentCode,
        Long basePricePerMinute,   // что лежит в БД, т/мин
        Double weatherCoef,
        Double demandCoef,
        Double aiCoef,
        Integer finalPricePerMinute,  // уже с учётом всех коэффициентов
        String currency               // "KZT"
) {}

