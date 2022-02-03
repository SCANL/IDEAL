package com.github.astyer.naturallanguagelabplugin.ui;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;
import com.github.astyer.naturallanguagelabplugin.rules.Result;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IdentifierGrammarToolWindow {

    final String catalogueURL = "https://github.com/SCANL/identifier_name_structure_catalogue";
    final String knowMoreText = "These recommendations are all part of a catalogue of grammar patterns. This catalogue documents the identifier naming styles and patterns that have been discovered by software researchers. You can access this catalogue via the button below.";
    final Font titleFont = new Font(null, Font.BOLD, 16);
    final String maxTextWidthStyling = "<html><body style='width: 300px'>"; //determines the min width of the tables unfortunately

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
    private final JTable[] tables = {currentTable, recommendedTable, othersTable};

    String[] currentHeaders = {"Type", "Identifier", "Current Grammar Pattern" };
    String[] recommendedHeaders = {"Type", "Identifier", "Recommended", "Generic" };
    private String type;
    private String identifierName;
    private String currentPattern;
    private String recommendedIdentifier;
    private String recommendedPattern;
    private String recommendedGenericPattern;
    private String[][] otherRecommendationsData = {};

    public static IdentifierGrammarToolWindow getInstance() {
        if (instance == null) {
            instance = new IdentifierGrammarToolWindow();
        }
        return instance;
    }

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

    private void setInitialTextAndStyling() {
        knowMoreValue.setText(maxTextWidthStyling + knowMoreText);
        for(JLabel titleLabel: titleLabels) {
            titleLabel.setFont(titleFont);
        }
        setInitialTableStyling();
    }

    private void setInitialTableStyling() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(JTable table: tables) {
            table.setEnabled(false);
            table.getTableHeader().setReorderingAllowed(false);
            table.getTableHeader().setResizingAllowed(true);
            table.setDefaultRenderer(Object.class, centerRenderer);
        }
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
                new String[][] {{type, recommendedIdentifier, recommendedPattern, recommendedGenericPattern}},
                recommendedHeaders
        ));
    }
    private void updateOthersTable() {
        othersTable.setModel(new DefaultTableModel(
                otherRecommendationsData,
                recommendedHeaders
        ));
        Dimension preferredTableSize = new Dimension(150,otherRecommendationsData.length * 16);
        othersTable.setPreferredSize(preferredTableSize);
        othersTable.setPreferredScrollableViewportSize(preferredTableSize);
    }

    public void setCurrentIdentifier(Result result) {
        Identifier id = result.getId();
        type = id.getCanonicalType();
        identifierName = id.getDisplayName();
        currentPattern = id.getPOS().replace('_', ' ');
        Result.Recommendation topRecommendation = result.getTopRecommendation();
        recommendedIdentifier = getRecommendedIdentifier(id, topRecommendation);
        recommendedPattern = getRecommendedPattern(id, topRecommendation);
        recommendedGenericPattern = topRecommendation.getName();
        setOtherRecommendationsData(id, result.getRecommendations());
        updateTables();
        explanationValue.setText(maxTextWidthStyling + topRecommendation.getExplanation());
        exampleValue.setText(maxTextWidthStyling + "Example:<br/>" + topRecommendation.getExample());
    }

    private void setOtherRecommendationsData(Identifier id, List<Result.Recommendation> otherRecommendations) {
        List<Result.Recommendation> otherRecsWithoutFirst = new ArrayList<>(otherRecommendations);
        otherRecsWithoutFirst.remove(0);
        otherRecommendationsData = new String[otherRecsWithoutFirst.size()][4];
        for(int i = 0; i < otherRecsWithoutFirst.size(); i++) {
            Result.Recommendation otherRecommendation = otherRecsWithoutFirst.get(i);
            otherRecommendationsData[i][0] = type;
            otherRecommendationsData[i][1] = getRecommendedIdentifier(id, otherRecommendation);
            otherRecommendationsData[i][2] = getRecommendedPattern(id, otherRecommendation);
            otherRecommendationsData[i][3] = otherRecommendation.getName();
        }
    }

    private String getRecommendedIdentifier(Identifier id, Result.Recommendation recommendation) {
        RecommendationAlg.Rec rec = recommendation.getRec();
        String[] splitIdentifier = id.getIdentiferSplit().split("_");
        List<Integer> correctWordIndexes = rec.getIndexesOfFinalId();
        StringBuilder recIdentifier = new StringBuilder("<html>");
        for(int i = 0; i < splitIdentifier.length; i++) {
            String word = splitIdentifier[i];
            if (!correctWordIndexes.contains(i)) {
                word = "<b><span style='color: red'>" + word + "</span></b>";
            }
            recIdentifier.append(" ").append(word);
        }
        return recIdentifier.toString();
    }

    // will break if current and rec patterns are different lengths ex. method named car will have rec V N where only V should be green
    private String getRecommendedPattern(Identifier id, Result.Recommendation recommendation) {
        RecommendationAlg.Rec rec = recommendation.getRec();
        String[] splitIdPOS = id.getPOS().split("_");
        String[] recPatternSplit = rec.getFinal().split("_");
        StringBuilder recPattern = new StringBuilder("<html>");
        for(int i = 0; i < recPatternSplit.length; i++) {
            String pos = recPatternSplit[i];
            if (i >= splitIdPOS.length || !recPatternSplit[i].equals(splitIdPOS[i])) {
                pos = "<b><span style='color: green'>" + pos + "</span></b>";
            }
            recPattern.append(" ").append(pos);
        }
        return recPattern.toString();
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
