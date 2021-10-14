package com.github.astyer.naturallanguagelabplugin.ui;

import javax.swing.*;

public class IdentifierGrammarToolWindow {

    private JPanel myToolWindowContent;
    private JLabel identifierNameTitle;
    private JLabel identifierNameValue;
    private JLabel protoGrammarPatternValue;
    private JLabel protoGrammarPatternTitle;
    private JLabel suggestionTitle;
    private JLabel suggestionValue;
    private JLabel explanationTitle;
    private JLabel explanationValue;
    private JLabel othersTitle;
    private JLabel othersValue;

    public IdentifierGrammarToolWindow() {
        othersValue.setText("<html>GrammarPattern1<br>GrammarPattern2<br>GrammarPattern3</html>");
    }

    public void setIdentifierName(String currentIdentifierName) {
        identifierNameValue.setText(currentIdentifierName); //find a way to call this in the inspection somehow? need to use a listener instead maybe?
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
