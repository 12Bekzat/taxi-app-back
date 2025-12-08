package com.taxi.app.dtos;

import lombok.Data;

@Data
public class AuthRequest {
    private String phone;
    private String password;
}
