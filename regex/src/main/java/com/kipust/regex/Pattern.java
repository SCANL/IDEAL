package com.kipust.regex;

public class Pattern {
    public static Pattern AcceptAny(){
        return new Pattern(new Dfa());
    }
    public static Pattern Compile(String regex){
        try {
            Lexer l = new Lexer(regex);
            Parser p = new Parser(l);
            AST ast = p.parse();
            Dfa dfa = new Dfa(ast);
            return new Pattern(dfa);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    private Dfa dfa;
    private Pattern (Dfa dfa){
        this.dfa = dfa;
    }

    public Dfa.DFAResult match(String input){
        return dfa.run(input);
    }
}
