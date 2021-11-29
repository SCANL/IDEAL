package com.github.astyer.naturallanguagelabplugin.rules;

public class Result{
    boolean pass;
    public String recommendation;
    public int priority;


    public Result(){
        pass = true;
    }
    public Result(String recommendation, int priority){
        this.recommendation = recommendation;
        this.priority = priority;
        pass = false;
    }
    public Boolean passes(){
        return pass;
    }

    @Override
    public String toString(){
        if(pass){
            return "PASS";
        }else{
            return "FAIL" + "(" + recommendation + ") : " + priority;
        }
    }
}
