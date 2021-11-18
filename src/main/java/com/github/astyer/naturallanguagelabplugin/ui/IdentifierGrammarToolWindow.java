package com.github.astyer.naturallanguagelabplugin.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class IdentifierGrammarToolWindow {

    final String catalogueURL = "https://github.com/SCANL/identifier_name_structure_catalogue";
    final String knowMoreText = "These recommendations are all part of a catalogue of grammar patterns. This catalogue documents the identifier naming styles and patterns that have been discovered by software researchers. You can access this catalogue via the button below.";
    final Font titleFont = new Font(null, Font.BOLD, 16);

    String[] currentHeaders = {"Type", "Identifier", "Current Grammar Pattern" };
    String[] recommendedHeaders = {"Type", "Identifier", "Recommended", "Generic" };
    private String currentType = "";
    private String currentIdentifier = "";
    private String currentRecommended = "";
    private String currentGeneric = "";

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

    public IdentifierGrammarToolWindow() {
        setInitialTextAndStyling();
        createTables();
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

    private void createTables() {
        updateCurrentTable();
        updateRecommendedTable();
        updateOthersTable();
    }

    private void updateCurrentTable() {
        currentTable.setModel(new DefaultTableModel(
                new String[][] {{"List", "Dynamic Table Index", "NM NM N"}},
                currentHeaders
        ));
    }
    private void updateRecommendedTable() {
        recommendedTable.setModel(new DefaultTableModel(
                new String[][] {{currentType, currentIdentifier, currentRecommended, currentGeneric}},
                recommendedHeaders
        ));
    }
    private void updateOthersTable() {
        othersTable.setModel(new DefaultTableModel(
                new String[][] {{currentType, currentIdentifier, currentRecommended, currentGeneric}},
                recommendedHeaders
        ));
    }

    public void setIdentifierName(String currentIdentifierName) {
        currentIdentifier = currentIdentifierName;
        updateRecommendedTable();
        updateOthersTable();
    }

    public void setRecommendedGenericPattern(String suggestedPattern) {
        currentGeneric = suggestedPattern;
        updateRecommendedTable();
        updateOthersTable();
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }



}
