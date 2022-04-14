package com.github.astyer.naturallanguagelabplugin.ui;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.github.astyer.naturallanguagelabplugin.extensions.PersistenceService;
import com.github.astyer.naturallanguagelabplugin.rules.Recommendation.RecommendationAlg;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import com.github.astyer.naturallanguagelabplugin.rules.TagNames;
import org.javatuples.Pair;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdentifierGrammarToolWindow {

    final String catalogueURL = "https://github.com/SCANL/identifier_name_structure_catalogue";
    final String knowMoreText = "These recommendations are all part of a catalogue of grammar patterns. This catalogue documents the identifier naming styles and patterns that have been discovered by software researchers. You can access this catalogue via the button below.";
    final Font titleFont = new Font(null, Font.BOLD, 16);
    final String maxTextWidthStyling = "<html><body style='width: 315px'>"; //determines the min width of the tables unfortunately
    final String noWrapStyling = "<html><body style='white-space: nowrap'>";

    private static IdentifierGrammarToolWindow instance = null;
    private Project project;
    private PersistenceService persistenceService;

    private JPanel myToolWindowContent;

    private JLabel currentTitle;
    private JTable currentTable;

    private JLabel recommendedTitle;
    private JTable recommendedTable;
    private JLabel recDescription;

    private JLabel exampleTitle;
    private JLabel exampleValue;

    private JLabel explanationTitle;
    private JLabel explanationValue;

    private JLabel knowMoreTitle;
    private JLabel knowMoreValue;
    private JButton catalogueButton;
    private JButton ignoreButton;

    private final JLabel[] titleLabels = {currentTitle, recommendedTitle, exampleTitle, explanationTitle, knowMoreTitle};
    private final JTable[] tables = {currentTable, recommendedTable};

    String[] currentHeaders = {"Type", "Identifier", "Current Grammar Pattern" };
    String[] recommendedHeaders = {"Type", "Identifier", "Recommended", "Generic" };
    private String type = "";
    private String identifierName = "";
    private String currentPattern = "";
    private String currentContext = "";
    private String recommendedIdentifier = "";
    private String recommendedPattern = "";
    private String recommendedGenericPattern = "";

    private Result currentResult;
    private PsiFile currentFile;

    public static IdentifierGrammarToolWindow getInstance() {
        if (instance == null) {
            instance = new IdentifierGrammarToolWindow();
        }
        return instance;
    }

    public IdentifierGrammarToolWindow() {
        setInitialTextAndStyling();
        updateTables(false);
        catalogueButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(URI.create(catalogueURL));
            } catch (IOException exc) {
                System.err.println("Failed to navigate to SCANL catalogue");
            }
        });
        ignoreButton.addActionListener(e -> {
            String ignoreKey = PersistenceService.getIgnoreKey(identifierName, type, currentContext, recommendedGenericPattern);
            if (!persistenceService.identifierIsIgnored(ignoreKey)) {
                persistenceService.ignoreIdentifier(ignoreKey);
            } else {
                persistenceService.unignoreIdentifier(ignoreKey);
            }
            // Refresh the file so the inspection is run again
            DaemonCodeAnalyzer.getInstance(project).restart(currentFile);
            // Update the sidebar
            this.setCurrentIdentifier(currentResult, currentFile);
        });
    }

    public void passProject(Project p) {
        project = p;
        persistenceService = project.getService(PersistenceService.class);
    }

    private void setInitialTextAndStyling() {
        recDescription.setText("");
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

    private void updateTables(boolean currentlyIgnoring) {
        updateCurrentTable();
        updateRecommendedTable(currentlyIgnoring);
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
    private void updateRecommendedTable(boolean currentlyIgnoring) {
        if (!currentlyIgnoring) {
            recommendedTable.setModel(new DefaultTableModel(
                    new String[][]{{type, recommendedIdentifier, recommendedPattern, recommendedGenericPattern}},
                    recommendedHeaders
            ));
        } else {
            recommendedTable.setModel(new DefaultTableModel(
                    new String[][]{{"Ignored", "Ignored", "Ignored", "Ignored"}},
                    recommendedHeaders
            ));
        }
    }

    public void setCurrentIdentifier(Result result, PsiFile psiFile) {
        Identifier id = result.getId();
        type = id.getCanonicalType();
        identifierName = id.getDisplayName();
        currentPattern = id.getPOS().replace('_', ' ');
        currentContext = id.getContext();
        currentResult = result;
        currentFile = psiFile;
        Result.Recommendation topRecommendation = result.getTopRecommendation();
        recommendedIdentifier = getRecommendedIdentifier(topRecommendation);
        recommendedPattern = getRecommendedPattern(topRecommendation);
        recommendedGenericPattern = topRecommendation.getName();

        String ignoreKey = PersistenceService.getIgnoreKey(identifierName, type, currentContext, topRecommendation.getName());
        if (!persistenceService.identifierIsIgnored(ignoreKey)) {
            updateTables(false);
            explanationValue.setText(maxTextWidthStyling + topRecommendation.getExplanation());
            exampleValue.setText(maxTextWidthStyling + topRecommendation.getExample());
            recDescription.setText(maxTextWidthStyling + getRecommendationDescription(recommendedIdentifier, recommendedPattern));
            ignoreButton.setText("Ignore Recommendation");
        } else {
            updateTables(true);
            explanationValue.setText("Identifier is currently being ignored.");
            exampleValue.setText("Identifier is currently being ignored.");
            recDescription.setText("");
            ignoreButton.setText("Stop Ignoring Recommendation");
        }
    }

    private String getRecommendedIdentifier(Result.Recommendation recommendation) {
        RecommendationAlg.Rec rec = recommendation.getRec();
        if(rec == null) {
            return "";
        }
        ArrayList<RecommendationAlg.WordPos> currentID = new ArrayList(rec.getOriginal());
        Integer offset = 0;
        for (RecommendationAlg.Rec.Change change : rec.getChanges()) {
            Pair<ArrayList<RecommendationAlg.WordPos>, Integer> result = change.getColoredId(currentID, offset);
            currentID = result.getValue0();
            offset = result.getValue1();
        }
        return noWrapStyling + currentID.stream().map(wordPos -> wordPos.getWord().replaceAll("_", "")).collect(Collectors.joining(" "));
    }

    private String getRecommendedPattern(Result.Recommendation recommendation) {
        RecommendationAlg.Rec rec = recommendation.getRec();
        if(rec == null) {
            return "";
        }
        ArrayList<RecommendationAlg.WordPos> currentID = new ArrayList(rec.getOriginal());
        Integer offset = 0;
        for (RecommendationAlg.Rec.Change change : rec.getChanges()) {
            Pair<ArrayList<RecommendationAlg.WordPos>, Integer> result = change.getColoredPOS(currentID, offset);
            currentID = result.getValue0();
            offset = result.getValue1();
        }
        return noWrapStyling + currentID.stream().map(wordPos -> wordPos.getPos().replaceAll("_", "")).collect(Collectors.joining(" "));
    }

    private String getRecommendationDescription(String coloredIdentifier, String coloredRecPattern) {
        ArrayList<String> removedWords = getColoredStrings(coloredIdentifier, "red");
        ArrayList<String> addedPos = getColoredStrings(coloredRecPattern, "green");
        if(removedWords.isEmpty() && addedPos.isEmpty()) {
            return "No changes suggested.";
        }
        String removedWordsStr = formatStringList(removedWords.stream().map(word -> "\"" + word + "\"").collect(Collectors.toList()));
        if(addedPos.isEmpty()) {
            return "We suggest removing " + removedWordsStr + " from your identifier.";
        }
        String addedPosStr = formatStringList(addedPos.stream().map(TagNames::getName).collect(Collectors.toList()));
        if(removedWords.isEmpty()) {
            return "We suggest adding " + addedPosStr + " to your identifier.";
        }
        return "We suggest removing " + removedWordsStr + " and adding " + addedPosStr + " to your identifier.";
    }

    private ArrayList<String> getColoredStrings(String inputString, String color) {
        ArrayList<String> coloredStrings = new ArrayList<>();
        String[] splitInput = inputString.split(color + "'>");
        for(String str: splitInput) {
            int spanStart = str.indexOf("</span>");
            if(spanStart != -1) {
                coloredStrings.add(str.substring(0, spanStart));
            }
        }
        return coloredStrings;
    }

    private String formatStringList(List<String> strings){
        String formattedString = "";
        if(strings.size() < 3) {
            formattedString = String.join(" and ", strings);
        }
        else {
            formattedString = String.join(", ", strings);
            int lastCommaIndex = formattedString.lastIndexOf(',');
            formattedString = formattedString.substring(0, lastCommaIndex) + ", and" + formattedString.substring(lastCommaIndex+1);
        }
        return formattedString;
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
