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
    final String noWrapStyling = "<html><body style='white-space: nowrap'>";

    private static IdentifierGrammarToolWindow instance = null;

    private JPanel myToolWindowContent;

    private JLabel currentTitle;
    private JTable currentTable;

    private JLabel recommendedTitle;
    private JTable recommendedTable;

    private JLabel exampleTitle;
    private JLabel exampleValue;

    private JLabel explanationTitle;
    private JLabel explanationValue;

    private JLabel knowMoreTitle;
    private JLabel knowMoreValue;
    private JButton catalogueButton;

    private final JLabel[] titleLabels = {currentTitle, recommendedTitle, exampleTitle, explanationTitle, knowMoreTitle};
    private final JTable[] tables = {currentTable, recommendedTable};

    String[] currentHeaders = {"Type", "Identifier", "Current Grammar Pattern" };
    String[] recommendedHeaders = {"Type", "Identifier", "Recommended", "Generic" };
    private String type = "";
    private String identifierName = "";
    private String currentPattern = "";
    private String recommendedIdentifier = "";
    private String recommendedPattern = "";
    private String recommendedGenericPattern = "";

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
        exampleValue.setText("");
        explanationValue.setText("");
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
    }

    private void updateCurrentTable() {
        currentTable.setModel(new DefaultTableModel(
                new String[][] {{
                    type,
                    identifierName,
                    currentPattern
                }},
                currentHeaders
        ));
    }
    private void updateRecommendedTable() {
        recommendedTable.setModel(new DefaultTableModel(
                new String[][] {{type, recommendedIdentifier, recommendedPattern, recommendedGenericPattern}},
                recommendedHeaders
        ));
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
        updateTables();
        explanationValue.setText(maxTextWidthStyling + topRecommendation.getExplanation());
        exampleValue.setText(maxTextWidthStyling + topRecommendation.getExample());
    }

    private String getRecommendedIdentifier(Identifier id, Result.Recommendation recommendation) {
        RecommendationAlg.Rec rec = recommendation.getRec();
        if (rec == null){
            return "";
        }
        String currentID = rec.getOriginal().getId();
        int offset = 0;
        for (RecommendationAlg.Rec.Change change : rec.getChanges()) {
            if (change instanceof RecommendationAlg.Rec.Remove) {
                RecommendationAlg.Rec.Remove remove = (RecommendationAlg.Rec.Remove) change;
                String ch = "<b><span style='color: red'>" + currentID.substring((remove.idIndex + offset), (remove.idIndex + offset) + remove.idLength) + "</span></b>";
                int tempOffset = offset + (ch).length();
                currentID = currentID.substring(0, remove.idIndex + offset) + ch + currentID.substring((remove.idIndex + offset) + remove.idLength);
                offset = tempOffset;
            } else if (change instanceof RecommendationAlg.Rec.Insert) {
                RecommendationAlg.Rec.Insert insert = (RecommendationAlg.Rec.Insert) change;
                currentID = currentID.substring(0, insert.idIndex) + "_" + currentID.substring(insert.idIndex);
            }
        }
        return noWrapStyling + currentID.replaceAll("\\.\\d*_", " ").replaceAll("_", "");
    }

    private String getRecommendedPattern(Identifier id, Result.Recommendation recommendation) {
        RecommendationAlg.Rec rec = recommendation.getRec();
        if (rec == null){
            return "";
        }
        String currentPOS = rec.getOriginal().getPosTags();
        int offset = 0;
        for(RecommendationAlg.Rec.Change change: rec.getChanges()){
            if(change instanceof RecommendationAlg.Rec.Insert){
                RecommendationAlg.Rec.Insert insert = (RecommendationAlg.Rec.Insert) change;
                currentPOS = currentPOS.substring(0, insert.index +offset) + "<b><span style='color: green'>" + insert.change + "</span></b>" + currentPOS.substring(insert.index+offset);
                offset += ("<b><span style='color: green'>" + "</span></b>").length();
            }else if(change instanceof RecommendationAlg.Rec.Remove) {
                RecommendationAlg.Rec.Remove remove = (RecommendationAlg.Rec.Remove) change;
                currentPOS = currentPOS.substring(0, remove.index + offset) + currentPOS.substring(remove.index + offset + remove.length);
            }
        }
        return noWrapStyling + currentPOS.replaceAll("_", " ");
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
