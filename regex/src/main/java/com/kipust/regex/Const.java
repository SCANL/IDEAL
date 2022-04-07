package com.kipust.regex;

public class Const {
    public static class Value extends Const{
        String value;
        public Value(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return  value;
        }
    }

    public static class Wildcard extends Const{
        public Wildcard(){

        }
        @Override
        public String toString(){
            return ".";
        }
    }
}
