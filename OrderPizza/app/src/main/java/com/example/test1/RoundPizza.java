package com.example.test1;

public class RoundPizza extends Pizza{

    private String type="Round Pizza";
    public RoundPizza(String name, String phone, String date, String cheese, String topping) {
        super(name, phone, date, cheese, topping);
    }

    @Override
    public String toString(){
        return "Shape:"+type+"\n"+super.toString();
    }
}
