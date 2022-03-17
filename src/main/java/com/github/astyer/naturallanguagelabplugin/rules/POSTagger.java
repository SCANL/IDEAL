package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
            String pos = null;
            while ((inputLine = in.readLine()) != null) {
                String line[] = inputLine.split(" ");
                if(line[0].equals(name) && line[1].equals(type) && line[2].equals(context)){
                    pos = line[3];
                    break;
                }
            }
            System.out.println("received content of "+content);
            in.close();
            String posTags = Arrays.stream(content.toString().split(","))
                    .map(w -> w.split("\\|")[1])
                    .map(w -> w + "_")
                    .collect(Collectors.joining(""));
            String finalId = Arrays.stream(content.toString().split(","))
                    .map(w -> w.split("\\|")[0])
                    .map(w -> w + "_")
                    .collect(Collectors.joining(""));

            System.out.println("final result of "+posTags);
            POSResult result = new POSResult(posTags, finalId);
            cache.put(key, result);
            return result;
        }catch (Exception ex){
            System.err.println("Unable to retrieve POS tags for " + name);
            System.err.println(ex.getMessage());
        }
        return new POSResult("", "");
    }
}
