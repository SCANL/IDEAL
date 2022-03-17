import com.github.astyer.naturallanguagelabplugin.IR.Method;
import com.github.astyer.naturallanguagelabplugin.IR.Variable;
import com.github.astyer.naturallanguagelabplugin.rules.AggregateRules;
import com.github.astyer.naturallanguagelabplugin.rules.Result;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static com.github.astyer.naturallanguagelabplugin.IR.IRFactory.IRType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MetricsTest {

    AggregateRules aggregateRules = new AggregateRules();

    /*
    @Test
    public void debugTest() throws Exception {
        List<Variable> vars = new ArrayList<Variable>();
        Variable varTE = new Variable("trainingExample", "trainingExample", "int", null, TYPE_OTHER);
        vars.add(varTE);

        Result r = aggregateRules.runAll(varTE);
        String str = "NM N";

        assertEquals(r.getTopRecommendation(), null);
    }

     */

    //Noun Phrase
    @Test
    public void rule1Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("training_example", "training_example", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("training_examples", "training_examples", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("database_connections", "database_connections", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("databaseConnections", "databaseConnections", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("primeNumber", "primeNumber", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("prime_number", "prime_number", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("primeNumbers", "primeNumbers", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("prime_numbers", "prime_numbers", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("test_examples", "test_examples", "int[]", null, TYPE_COLLECTION));
        vars.add(new Variable("get_vars", "get_vars", "int[]", null, TYPE_COLLECTION));
        String[] expectedResults = new String[]{"NM* NPL", "null", "null", "null", "NM* NPL", "NM* NPL", "null", "null", "null", "NM* NPL"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    //Plural Noun Phrase
    @Test
    public void rule2Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("trainingExample", "trainingExample", "int", null, TYPE_OTHER));
        vars.add(new Variable("userCount", "userCount", "int", null, TYPE_OTHER));
        vars.add(new Variable("training_examples", "training_examples", "int", null, TYPE_OTHER));
        vars.add(new Variable("userCounts", "userCounts", "int", null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "null", "NM* N", "NM* N"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    @Test
    public void rule2_1Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("training_example", "training_example", "int", null, TYPE_OTHER));
        vars.add(new Variable("training_examples", "training_examples", "int", null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "NM* N"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    @Test
    public void rule2_2Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("training_example", "training_example", "boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("magicBoolean", "magicBoolean", "boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("training_examples", "training_examples", "boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("is_example", "is_example", "boolean", null, TYPE_BOOLEAN));
        String[] expectedResults = new String[]{"V NM* N|NPL", "V NM* N|NPL", "V NM* N|NPL", "null"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    //Verb Phrase (variables)
    @Test
    public void rule3VarTests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("alive", "alive", "java.lang.boolean", null, TYPE_BOOLEAN)); //fail
        vars.add(new Variable("isVariable", "isVariable", "java.lang.boolean", null, TYPE_BOOLEAN)); //pass
        vars.add(new Variable("hasChildren", "hasChildren", "java.lang.boolean", null, TYPE_BOOLEAN)); //pass
        vars.add(new Variable("createChildren", "createChildren", "java.lang.boolean", null, TYPE_BOOLEAN)); //fail
        vars.add(new Variable("jumpAlive", "jumpAlive", "java.lang.boolean", null, TYPE_BOOLEAN)); //fail
        vars.add(new Variable("addChildren", "addChildren", "java.lang.boolean", null, TYPE_BOOLEAN)); //fail
        vars.add(new Variable("eatFood", "eatFood", "java.lang.boolean", null, TYPE_BOOLEAN)); //fail
        String[] expectedResults = new String[]{"V NM* N|NPL", "null", "null", "V NM* N|NPL", "V NM* N|NPL", "null", "null"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    //Verb Phrase (methods)
    @Test
    public void rule3MethTests() throws Exception{

        List<Method> meths = new ArrayList<Method>();
        meths.add(new Method("getUsers()", "getUsers", "java.lang.string", false, false, null, TYPE_OTHER)); //pass
        meths.add(new Method("addUser()", "addUser", "java.lang.string", false, false, null, TYPE_OTHER)); //pass
        meths.add(new Method("user()", "user", "java.lang.string", false, false, null, TYPE_OTHER)); //fail
        meths.add(new Method("with()", "with", "void", false, false, null, TYPE_OTHER)); //fail
        String[] expectedResults = new String[]{"null", "null", "V NM* N|NPL | V+ pt1 TODO: Update dynamically", "V NM* N|NPL | V+ pt1 TODO: Update dynamically"};
        for(int i = 0; i< meths.size(); i++){
            Method m = meths.get(i);
            Result r = aggregateRules.runAll(m);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + m.getName() + "\"");
                ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + m.getName() + "\"");
                ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    /*
    //Prepositional Phrase Patter
    @Test
    public void rule4Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("training_example", "training_example", "int", null, TYPE_OTHER));
        vars.add(new Variable("training_examples", "training_examples", "int", null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "NM* N"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    //Prep Phrase With Leading Noun
    @Test
    public void rule5Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("training_example", "training_example", "int", null, TYPE_OTHER));
        vars.add(new Variable("training_examples", "training_examples", "int", null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "NM* N"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    //Prep Phrase With Leading Verb
    @Test
    public void rule6Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("destroy_with_parent", "destroy_with_parent", "boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("convert_to_php_namespace", "convert_to_php_namespace", "java.lang.String", null, TYPE_OTHER));
        vars.add(new Variable("tessellate_To_Mesh()", "tessellate_To_Mesh", "void", null, TYPE_OTHER));
        vars.add(new Variable("save_As_Quadratic_Png()", "save_As_Quadratic_Png", "void", null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "NM* N"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }

    //Noun Phrase With Leading Determiner
    @Test
    public void rule7Tests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("all_invocation_matchers", "all_invocation_matchers", "List<int>", null, TYPE_COLLECTION)); //pass
        vars.add(new Variable("all_Open_Indices", "all_Open_Indices", "java.lang.string", null, TYPE_COLLECTION)); //pass
        vars.add(new Variable("is_a_empty", "is_a_empty", "int", null, TYPE_OTHER)); //pass
        vars.add(new Variable("matches_any_parent_categories()", "matches_any_parent_categories", "boolean", null, TYPE_BOOLEAN)); //pass
        String[] expectedResults = new String[]{"null", "null", "null", "null"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + v.getName() + "\"");
                ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }
    */

    //Verb Pattern
    @Test
    public void rule8Tests() throws Exception{

        List<Method> meths = new ArrayList<Method>();
        meths.add(new Method("run()", "run", "void", false, false, null, TYPE_OTHER)); //pass
        meths.add(new Method("getAdmins()", "getAdmins", "void", false, false, null, TYPE_OTHER)); //pass
        meths.add(new Method("users()", "users", "void", false, false, null, TYPE_OTHER)); //fail
        meths.add(new Method("with()", "with", "void", false, false, null, TYPE_OTHER)); //fail
        String[] expectedResults = new String[]{"null", "null", "V NM* N|NPL | V+ pt1 TODO: Update dynamically", "V NM* N|NPL | V+ pt1 TODO: Update dynamically"};
        for(int i = 0; i< meths.size(); i++){
            Method m = meths.get(i);
            Result r = aggregateRules.runAll(m);
            String expectedResult = expectedResults[i];
            PrintStream ps;
            if(r.getTopRecommendation().getRegexMatches()){
                ps =  expectedResult.equals("null")? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals("null")?"Success":"Fail") + ")");
                ps.println("Variable: \"" + m.getName() + "\"");
                ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
                ps.println("Expected Match: " + expectedResult);
                ps.println("Actual Match: " + "null");
                assertEquals(expectedResult, "null");
            } else {
                ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
                ps.println("Rule 1 Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
                ps.println("Variable: \"" + m.getName() + "\"");
                ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
                ps.println("Expected Recommendation: " + expectedResult);
                ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
                assertEquals(expectedResult, r.getTopRecommendation().getName());
            }
            System.out.println();
        }
    }
}