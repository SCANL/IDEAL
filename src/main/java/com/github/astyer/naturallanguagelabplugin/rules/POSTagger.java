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

/**
 * A class to connect to the POS tagger and tag identifiers
 */
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
    //this folows the singleton pattern
    static POSTagger instance;
    //we keep a cache of each identifier based on name, type and context. This should help speed things up
    // though we don't have checks to make sure that it doesn't get too big
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
        return name + "|" + type + "|" + context;
    }

    /**
     * Tag an identifier
     * @param id the identifier to tag
     * @return the Result
     */
    public POSResult tag(Identifier id){
        String name = id.getName();
        String type = id.getCanonicalType().toLowerCase(); //todo: remove before last .
        String context = id.getContext();
        String key = getKey(name, type, context);
        if(cache.containsKey(key)){
            //System.out.println(name + " is in the cache");
            return cache.get(key);
        }
        try {
            String urlStr = String.format("http://127.0.0.1:5000/%s/%s/%s", type, name, context);
            //System.out.println("Tagging "+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            //System.out.println("got response code of "+status);
            if(status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                //System.out.println("received content of "+content);
                in.close();
                con.disconnect();
                String posTags = Arrays.stream(content.toString().split(","))
                        .map(w -> w.split("\\|")[1])
                        .map(w -> w + "_")
                        .collect(Collectors.joining(""));
                String finalId = Arrays.stream(content.toString().split(","))
                        .map(w -> w.split("\\|")[0])
                        .map(w -> w + "_")
                        .collect(Collectors.joining(""));

                //System.out.println("final result of "+posTags);
                POSResult result = new POSResult(posTags, finalId);
                cache.put(key, result);
                return result;
            }
            else {
                System.err.println("Error while making connection to " + urlStr);
                System.err.println("Received status code of " + status);
            }
        }catch (Exception ex){
            System.err.println("Unable to retrieve POS tags for " + name);
            System.err.println(ex.getMessage());
        }
        return new POSResult("", "");
    }
}
