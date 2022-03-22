package com.kipust.regex;

interface Visitor {
    void visitZeroOrMore(AST.ZeroOrMore ast);
    void visitOr(AST.Or ast);
    void visitAnd(AST.And ast);
    void visitConstant(AST.Constant ast);
    void visitEmptySet(AST.EmptySet ast);
    void visitRange(AST.Range ast);
    void visitWildcard(AST.Wildcard ast);
}
