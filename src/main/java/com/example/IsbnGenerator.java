package com.example;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Random;

@ApplicationScoped
public class  IsbnGenerator {
    public String generateNumber() {
        return "13-45678-"+ Math.abs(new Random().nextInt());
    }
}
