package com.github.astyer.naturallanguagelabplugin.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.io.IOException;
import java.net.URI;

public class IdentifierGrammarToolWindow {

    final String catalogueURL = "https://github.com/SCANL/identifier_name_structure_catalogue";

    private static IdentifierGrammarToolWindow instance = null;

    private JPanel myToolWindowContent;
    private JLabel identifierNameTitle;
    private JLabel identifierNameValue;
    private JLabel protoGrammarPatternValue;
    private JLabel protoGrammarPatternTitle;
    private JLabel suggestionTitle;
    private JLabel suggestionValue;
    private JLabel explanationTitle;
    private JLabel explanationValue;
    private JLabel exampleValue;
    private JLabel catalogueTitle;
    private JButton catalogueButton;
    private JLabel othersTitle;
    private JLabel othersValue;
    private JTable suggestedPattern;
    private JTable otherSuggestedPattern;
    private JTable currentPattern;

    public IdentifierGrammarToolWindow() {
        othersValue.setText("<html>GrammarPattern1<br>GrammarPattern2<br>GrammarPattern3</html>");
        catalogueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(URI.create(catalogueURL));
                } catch (IOException exc) {
                    System.err.println("Failed to navigate to SCANL catalogue");
                }
            }
        });
        createTable();

    }

    public static IdentifierGrammarToolWindow getInstance() {
        if (instance == null) {
            instance = new IdentifierGrammarToolWindow();
        }
        return instance;
    }

    public void setIdentifierName(String currentIdentifierName) {
        identifierNameValue.setText(currentIdentifierName);
    }

    public void setSuggestedPattern(String suggestedPattern) {
        suggestionValue.setText(suggestedPattern);
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createTable() {
        String[] currentHeaders = {"Type", "Identifier", "Current Grammar Pattern" };
        String[] suggestedHeaders = {"Type", "Identifier", "Recommended", "Generic" };

        currentPattern.setModel(new DefaultTableModel(
                new String[][] {{"List", "Dynamic Table Index", "NM NM N"}},
                currentHeaders
        ));
        suggestedPattern.setModel(new DefaultTableModel(
                new String[][] {{"List", "Dynamic Table Index", "NM NM NPL", "NM* NPL"}},
                suggestedHeaders
        ));
        otherSuggestedPattern.setModel(new DefaultTableModel(
                new String[][] {{"List", "Dynamic Table Index", "V NM NPL", "V NM* NPL"}},
                suggestedHeaders
        ));
    }

}
