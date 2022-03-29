package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.*;
import com.github.astyer.naturallanguagelabplugin.IR.Class;
import com.github.astyer.naturallanguagelabplugin.rules.Checkbox;
import com.github.astyer.naturallanguagelabplugin.rules.CheckboxResult;

import java.util.ArrayList;
import java.util.List;

import com.github.astyer.naturallanguagelabplugin.rules.ExplanationsAndExamples;
import com.github.astyer.naturallanguagelabplugin.rules.Rule;
import com.kipust.regex.Pattern;

public class RuleForest {
    static RuleForest instance = null;
    public static RuleForest getInstance(){
        if(instance == null){
            instance = new RuleForest();
        }
        return instance;
    }

    private RuleForest(){
        //create var
        RuleNode nmn = new RuleNode("NM* N", Pattern.Compile("&(*('NM_'),'N_')"), ExplanationsAndExamples.getExplanation(Rule.NMN), ExplanationsAndExamples.getExample(Rule.NMN));
        RuleNode vnmn = new RuleNode("V NM* N|NPL", Pattern.Compile("&('V_',&(*('NM_'),|('N_','NPL_')))"), ExplanationsAndExamples.getExplanation(Rule.VNMN), ExplanationsAndExamples.getExample(Rule.VNMN));//("V (NM )*(N|NPL)"));
        RuleNode nmnpl = new RuleNode("NM* NPL", Pattern.Compile("&(*('NM_'),'NPL_')"), ExplanationsAndExamples.getExplanation(Rule.NMNPL), ExplanationsAndExamples.getExample(Rule.NMNPL));//("(NM )*NPL"));

        nmn.addBranch(new RuleBranch(
                "Type of Bool",
                vnmn,
                new Checkbox(
                        "Type of Bool",
                        null,
                        null,
                        variable -> new CheckboxResult(variable.getType().equals(IRFactory.IRType.TYPE_BOOLEAN))
                )
        ));

        nmn.addBranch(new RuleBranch(
                "Type of Collection",
                nmnpl,
                new Checkbox(
                        "Type of Collection",
                        null,
                        null,
                        variable -> new CheckboxResult(variable.getType().equals(IRFactory.IRType.TYPE_COLLECTION))
                )
        ));
        this.varTree = nmn;

        //class tree
        this.classTree = new RuleNode("NM* N|NPL", Pattern.Compile("&(*('NM_'),|('N_','NPL_'))"), ExplanationsAndExamples.getExplanation(Rule.NMNNPL), ExplanationsAndExamples.getExample(Rule.NMNNPL));//("(NM )*(N|NPL)"));

        //method tree
        RuleNode methodRoot = new RuleNode("empty", Pattern.AcceptAny(), "", "");
        Pattern vplusPattern = Pattern.Compile("&('V_',*('V_'))");
        RuleNode weirdRulePt1 = new RuleNode(
                (Identifier id)->{
                    if(vplusPattern.match(id.getPOS()).success()){
                        return "V+";
                    }else{
                        return "V NM* N|NPL";
                    }
                },
//                "V NM* N|NPL | V+ pt1 TODO: Update dynamically",
                Pattern.Compile("|(&('V_',&(*('NM_'),|('N_','NPL_'))),&('V_',*('V_')))"),
                (Identifier id) ->{
                    if(vplusPattern.match(id.getPOS()).success()){
                        return "MINOR ALERT A " + ExplanationsAndExamples.getExplanation(Rule.VV1);
                    }else{
                        return "NO MINOR ALERT " + ExplanationsAndExamples.getExplanation(Rule.VV1);
                    }
                },
                (Identifier id) -> ExplanationsAndExamples.getExample(Rule.VV1)
        );//("(V (NM )*(N|NPL))|V+"));
        Pattern vnpPattern = Pattern.Compile("&('V_', &(*('NM_'), |('N_','NPL_')))");
        RuleNode weirdRulePt2 = new RuleNode(
            (Identifier id) -> {
                if(vnpPattern.match(id.getPOS()).success()){
                    return "V NM* N(PL)";
                }else{
                    return "V+";
                }
            },
            Pattern.Compile("|(&('V_',&(*('NM_'),|('N_','NPL_'))),&('V_',*('V_')))"),
            (Identifier id) -> {
                if(vnpPattern.match(id.getPOS()).success()){
                    return "MINOR ALERT B " + ExplanationsAndExamples.getExplanation(Rule.VV2);
                }else{
                    return "MINOR ALERT C " + ExplanationsAndExamples.getExplanation(Rule.VV2);
                }
            },
            (Identifier id) -> ExplanationsAndExamples.getExample(Rule.VV2)
        );//("(V (NM )*(N|NPL))|V+"));
        RuleNode prepNounPhrase = new RuleNode("(.*) P NM* N|NPL", Pattern.Compile("&(*(.),&('P_', &(*('NM_'), |('N_','NPL_'))))"), ExplanationsAndExamples.getExplanation(Rule.PNMN), ExplanationsAndExamples.getExample(Rule.PNMN));
//        RuleNode pnmn = new RuleNode("P NM* N|NPL", Pattern.Compile("&('P_',&(*('NM_'),|('N_','NPL_'))))"), ExplanationsAndExamples.getExplanation(Rule.PNMN), ExplanationsAndExamples.getExample(Rule.PNMN));//("P (NM )*(N|NPL)"));
        RuleNode VDT = new RuleNode("V* DT NM* N|NPL", Pattern.Compile("&('V_',&('DT_',&(*('NM_'), |('N_','NPL_'))))"), ExplanationsAndExamples.getExplanation(Rule.VDT), ExplanationsAndExamples.getExample(Rule.VDT));

        methodRoot.addBranch(new RuleBranch("No Void/Generics", weirdRulePt1, new Checkbox("No Void/Generics",null, method -> new CheckboxResult(!method.getType().equals(IRFactory.IRType.TYPE_VOID)), null)));
        methodRoot.addBranch(new RuleBranch("Void/Generics", weirdRulePt2, new Checkbox("Void/Generics", null, method -> new CheckboxResult(method.getType().equals(IRFactory.IRType.TYPE_VOID)), null)));
        weirdRulePt1.addBranch(new RuleBranch("Event Driven Code | Code Contains Casting", prepNounPhrase, new Checkbox("Event | Casting", null, method -> new CheckboxResult(method.performsEventDrivenFunctionality() | method.performsConversion()), null)));
//        weirdRulePt1.addBranch(new RuleBranch("Event Driven Code", pnmn, new Checkbox("Event Driven Code", null, method -> new CheckboxResult(method.performsEventDrivenFunctionality()), null)));
//        weirdRulePt1.addBranch(new RuleBranch("Code contains Casting", pnmn, new Checkbox("Code contains Casting", null, method -> new CheckboxResult(method.performsConversion()), null)));
        weirdRulePt1.addBranch(new RuleBranch("Loop in body", VDT, new Checkbox("Loop in body", null, null, null))); //todo:check for loops
        this.methodTree = methodRoot;

    }
    private RuleNode varTree;
    private RuleNode classTree;
    private RuleNode methodTree;




    public List<NodeResult> runIdentifier(Identifier id){
        if(id instanceof Variable) {
            List<NodeResult> results = varTree.checkIdentifier(id, 0);
            System.out.println(id.getName() + ": " + id.getPOS());
            for (NodeResult nr : results) {
                System.out.println(nr.getName() + ": " + nr.getDepth() + ": " + nr.isIdentifierMatchesRegex());
            }
            return results;
        }else if(id instanceof Method) {
            List<NodeResult> results = methodTree.checkIdentifier(id, 0);
            return results;
        }else if(id instanceof Class){
            List<NodeResult> results = classTree.checkIdentifier(id, 0);
            return results;
        }else{
            return new ArrayList<>();
        }
    }
}
