package com.example.test1;

public class Pizza {
    private String name,phone,date,cheese,topping;

    public Pizza(String name,String phone,String date,String topping){
        this.name=name;
        this.phone=phone;
        this.date=date;
        this.topping=topping;
        this.cheese="";
    }

    public Pizza(String name,String phone,String date,String cheese,String topping){
        this.name=name;
        this.phone=phone;
        this.date=date;
        this.cheese=cheese;
        this.topping=topping;
    }

    @Override
    public String toString(){

        return "Customer detail:"+name+"-"+phone+"-"+date+"\nCheese:"+cheese+"\nTopping:"+topping;
    }
}
