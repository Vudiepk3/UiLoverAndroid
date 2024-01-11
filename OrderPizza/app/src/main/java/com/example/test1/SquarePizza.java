package com.example.test1;

public class SquarePizza extends Pizza{

    private String type="Square";
    public SquarePizza(String name, String phone, String date, String cheese, String topping) {
        super(name, phone, date, cheese, topping);
    }

    @Override
    public String toString(){
        return "Shape:"+type+"\n"+super.toString();
    }
}
