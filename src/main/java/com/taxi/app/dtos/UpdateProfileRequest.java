package com.taxi.app.dtos;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String email; // телефон пока не трогаем, он логин
}