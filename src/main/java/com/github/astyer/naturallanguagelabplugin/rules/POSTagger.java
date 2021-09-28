package com.github.astyer.naturallanguagelabplugin.rules;

public class POSTagger {
    static POSTagger instance;

    public static POSTagger getInstance(){
        if(instance == null){
            instance = new POSTagger();
        }
        return instance;
    }

    private POSTagger(){

    }



    public String tag(String name){
        switch (name){
            case "training_examples":
                return "NM NPL";
            case "training_example":
                return "NM N";
            case "dynamic_Table_Index":
                return "NM NM N";
        }
        return "";
    }
}
