package com.kipust.regex;

import java.util.HashSet;
import java.util.Set;

class FindConstantsVisitor implements Visitor {
    Set<String> constants = new HashSet<>();

    public FindConstantsVisitor(){

    }

    @Override
    public void visitZeroOrMore(AST.ZeroOrMore ast) {
        ast.child.accept(this);
    }

    @Override
    public void visitOr(AST.Or ast) {
        ast.right.accept(this);
        ast.left.accept(this);
    }

    @Override
    public void visitAnd(AST.And ast) {
        ast.right.accept(this);
        ast.left.accept(this);
    }

    @Override
    public void visitConstant(AST.Constant ast) {
        constants.add(ast.value);
    }

    @Override
    public void visitEmptySet(AST.EmptySet ast) {

    }

    @Override
    public void visitRange(AST.Range ast) {
        for(int i = ast.start; i <=ast.end; i++){
            constants.add(Character.toString((char)i));
        }
    }
}
