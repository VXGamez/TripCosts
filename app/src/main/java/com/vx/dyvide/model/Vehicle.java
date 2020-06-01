package com.vx.dyvide.model;

public class Vehicle {

    private int type;
    private String name;
    private int fuel;
    private float consum;

    public Vehicle(int type, String name, float consum, int fuel) {
        this.type = type;
        this.name = name;
        this.fuel = fuel;
        this.consum = consum;
    }

    public Vehicle() {
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getConsum() {
        return consum;
    }

    public void setConsum(float consum) {
        this.consum = consum;
    }
}
