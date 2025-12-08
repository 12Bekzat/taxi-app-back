package com.taxi.app.dtos;

import lombok.Data;

@Data
public class CustomerProfileDto {
    private String defaultAddress;
    private String idFrontUrl;
    private String idBackUrl;
}