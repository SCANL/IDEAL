package com.github.astyer.naturallanguagelabplugin.ui;

import com.github.astyer.naturallanguagelabplugin.rules.Result;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class IdentifierGrammarToolWindow {

    final String catalogueURL = "https://github.com/SCANL/identifier_name_structure_catalogue";
    final String knowMoreText = "These recommendations are all part of a catalogue of grammar patterns. This catalogue documents the identifier naming styles and patterns that have been discovered by software researchers. You can access this catalogue via the button below.";
    final Font titleFont = new Font(null, Font.BOLD, 16);

    private static IdentifierGrammarToolWindow instance = null;

    private JPanel myToolWindowContent;

    private JLabel currentTitle;
    private JTable currentTable;

    private JLabel recommendedTitle;
    private JTable recommendedTable;

    private JLabel explanationTitle;
    private JLabel explanationValue;
    private JLabel exampleValue;

    private JLabel othersTitle;
    private JTable othersTable;

    private JLabel knowMoreTitle;
    private JLabel knowMoreValue;
    private JButton catalogueButton;

    private final JLabel[] titleLabels = {currentTitle, recommendedTitle, explanationTitle, othersTitle, knowMoreTitle};

    String[] currentHeaders = {"Type", "Identifier", "Current Grammar Pattern" };
    String[] recommendedHeaders = {"Type", "Identifier", "Recommended", "Generic" };
    private String type;
    private String identifierName;
    private String currentPattern;
    private String recommendedPattern;
    private String recommendedGenericPattern;
    private String example;
    private String explanation;

    public IdentifierGrammarToolWindow() {
        setInitialTextAndStyling();
        updateTables();
        catalogueButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(URI.create(catalogueURL));
            } catch (IOException exc) {
                System.err.println("Failed to navigate to SCANL catalogue");
            }
        });
    }

    public static IdentifierGrammarToolWindow getInstance() {
        if (instance == null) {
            instance = new IdentifierGrammarToolWindow();
        }
        return instance;
    }

    private void setInitialTextAndStyling() {
        for (JLabel titleLabel : titleLabels) {
            titleLabel.setFont(titleFont);
        }
        knowMoreValue.setText("<html><body style='width: 275px'>" + knowMoreText); //determines the min width of the tables unfortunately
    }

    private void updateTables() {
        updateCurrentTable();
        updateRecommendedTable();
        updateOthersTable();
    }

    private void updateCurrentTable() {
        currentTable.setModel(new DefaultTableModel(
                new String[][] {{type, identifierName, currentPattern}},
                currentHeaders
        ));
    }
    private void updateRecommendedTable() {
        recommendedTable.setModel(new DefaultTableModel(
                new String[][] {{type, identifierName, recommendedPattern, recommendedGenericPattern}},
                recommendedHeaders
        ));
    }
    private void updateOthersTable() {
        othersTable.setModel(new DefaultTableModel(
                new String[][] {{"", "", "", ""}},
                recommendedHeaders
        ));
    }

    public void setCurrentIdentifier(Result result) {
        Result.Recommendation topRecommendation = result.getTopRecommendation();
        recommendedGenericPattern = (topRecommendation == null) ? "" : topRecommendation.getName();
        identifierName = result.getId().getName();
        currentPattern = result.getId().getPOS().replace('_', ' ');
        updateTables();
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
