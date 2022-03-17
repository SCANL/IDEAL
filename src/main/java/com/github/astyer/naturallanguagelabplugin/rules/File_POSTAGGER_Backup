package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class POSTagger {
    public static class POSResult{
        String posTags;
        String Id;
        public POSResult(String posTags, String Id){
            this.posTags = posTags;
            this.Id = Id;
        }
        public String getPosTags(){
            return posTags;
        }
        public String getId(){
            return Id;
        }
    }
    static POSTagger instance;
    Map<String, POSResult> cache;

    public static POSTagger getInstance(){
        if(instance == null){
            instance = new POSTagger();
        }
        return instance;
    }

    private POSTagger(){
        cache = new HashMap<>();
    }

    private String getKey(String name, String type, String context){
        return name  + "|" + type +"|"+context;
    }

    public POSResult tag(Identifier id){
        String name = id.getName();
        String type = id.getCanonicalType().toLowerCase(); //todo: remove before last .
        String context = id.getContext();
        String key = getKey(name, type, context);
        if(cache.containsKey(key)){
            System.out.println(name + " is in the cache");
            return cache.get(key);
        }
        try {
            File file = new File("src/test/java/POSMock.txt");
            BufferedReader in = new BufferedReader(new FileReader(file));
            String inputLine;
            StringBuilder content = new StringBuilder();
            String posTags = "";
            String finalId = "";
            while ((inputLine = in.readLine()) != null) {
                String line[] = inputLine.split(" ");
                if(line[0].equals(name) && line[1].equals(type) && line[2].equals(context)){
                    posTags = line[3];
                    finalId = line[4];
                    break;
                }
            }
            in.close();
            System.out.println(finalId + " tagged as " + posTags);
            POSResult result = new POSResult(posTags, finalId);
            if(!posTags.equals("") || !finalId.equals("")){
                cache.put(key, result);
            }
            return result;
        }catch (Exception ex){
            System.err.println("Unable to retrieve POS tags for " + name);
            System.err.println(ex.getMessage());
        }
        return new POSResult("", "");
    }
}
