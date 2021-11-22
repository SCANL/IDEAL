package com.kipust.regex;

abstract class Token {

    public static class TokConstant extends Token {
        String value;
        public TokConstant(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "'"+value+"'";
        }
    }
    public static class TokRange extends Token{
        String start, end;
        public TokRange(String start, String end){
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + "'" + start + '\'' + "-'" + end + '\'' + ']';
        }
    }
    public static class TokAsterisk extends Token{
        @Override
        public String toString() {
            return "*";
        }
    }
    public static class TokBar extends Token{
        @Override
        public String toString() {
            return "|";
        }
    }
    public static class TokOpenParen extends Token{
        @Override
        public String toString() {
            return "(";
        }
    }
    public static class TokCloseParen extends Token{
        @Override
        public String toString() {
            return ")";
        }
    }
    public static class TokAmpersand extends Token{
        @Override
        public String toString() {
            return "&";
        }
    }
    public static class TokComma extends Token{
        @Override
        public String toString() {
            return ",";
        }
    }
    public static class TokEOF extends Token{
        @Override
        public String toString() {
            return "EOF";
        }
    }
}
