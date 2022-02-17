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



    @Test
    public void variableTests() throws Exception {
        List<Variable> vars = new ArrayList<Variable>();
        String[] expectedResults = new String[]{"null", "null", "NM* N", "NM* N", "V NM* N(PL)", "V NM* N(PL)", "V NM* N(PL)", "null", "null", "NM* N", "NM* NPL", "null", "null", "null", "NM* NPL", "NM* NPL", "null", "null", "null", "NM* NPL", "V NM* N(PL)", "null", "null", "V NM* N(PL)", "V NM* N(PL)", "V NM* N(PL)", "V NM* N(PL)"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + v.getName() + "\"");
            ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }

     */

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

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + v.getName() + "\"");
            ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }

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

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + v.getName() + "\"");
            ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
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

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + v.getName() + "\"");
            ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
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
        String[] expectedResults = new String[]{"V NM* N(PL)", "V NM* N(PL)", "V NM* N(PL)", "null"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + v.getName() + "\"");
            ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }

    @Test
    public void rule3VarTests() throws Exception{

        List<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable("alive", "alive", "java.lang.boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("isVariable", "isVariable", "java.lang.boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("hasChildren", "hasChildren", "java.lang.boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("createChildren", "createChildren", "java.lang.boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("jumpAlive", "jumpAlive", "java.lang.boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("addChildren", "addChildren", "java.lang.boolean", null, TYPE_BOOLEAN));
        vars.add(new Variable("eatFood", "eatFood", "java.lang.boolean", null, TYPE_BOOLEAN));
        String[] expectedResults = new String[]{"V NM* N(PL)", "null", "null", "V NM* N(PL)", "V NM* N(PL)", "V NM* N(PL)", "V NM* N(PL)"};

        for(int i = 0; i< vars.size(); i++){
            Variable v = vars.get(i);
            Result r = aggregateRules.runAll(v);
            String expectedResult = expectedResults[i];

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + v.getName() + "\"");
            ps.println("Canonical Type: \"" + v.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }

    @Test
    public void rule3MethTests() throws Exception{

        List<Method> meths = new ArrayList<Method>();
        meths.add(new Method("getUsers()", "getUsers", "java.lang.String", false, false, null, TYPE_OTHER));
        meths.add(new Method("addUser()", "addUser", "java.lang.String", false, false, null, TYPE_OTHER));
        meths.add(new Method("user()", "user", "java.lang.String", false, false, null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "null", "V NM* N(PL)|V+ pt1 TODO: Update dynamically"};
        for(int i = 0; i< meths.size(); i++){
            Method m = meths.get(i);
            Result r = aggregateRules.runAll(m);
            String expectedResult = expectedResults[i];

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + m.getName() + "\"");
            ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }

    @Test
    public void rule8Tests() throws Exception{

        List<Method> meths = new ArrayList<Method>();
        meths.add(new Method("run()", "run", "void", false, false, null, TYPE_OTHER));
        meths.add(new Method("getAdmins()", "getAdmins", "void", false, false, null, TYPE_OTHER));
        meths.add(new Method("users()", "users", "void", false, false, null, TYPE_OTHER));
        String[] expectedResults = new String[]{"null", "null", "V NM* N(PL)|V+ pt1 TODO: Update dynamically"};
        for(int i = 0; i< meths.size(); i++){
            Method m = meths.get(i);
            Result r = aggregateRules.runAll(m);
            String expectedResult = expectedResults[i];

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + m.getName() + "\"");
            ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }

    @Test
    public void otherMethodTests() throws Exception {
        List<Method> meths = new ArrayList<Method>();
        meths.add(new Method("rule_2()", "rule_2", "void", false, false, null, TYPE_OTHER));
        meths.add(new Method("rule_2_2()", "rule_2_2", "void", false, false, null, TYPE_OTHER));
        meths.add(new Method("rule2_1()", "rule2_1", "void", false, false, null, TYPE_OTHER));
        meths.add(new Method("rule1()", "rule1", "void", false, false, null, TYPE_OTHER));
        meths.add(new Method("rule3()", "rule_3", "void", false, false, null, TYPE_OTHER));
        String[] expectedResults = new String[]{"V NM* N(PL)|V+ pt1 TODO: Update dynamically", "V NM* N(PL)|V+ pt1 TODO: Update dynamically", "V NM* N(PL)|V+ pt1 TODO: Update dynamically", "V NM* N(PL)|V+ pt1 TODO: Update dynamically", "null", "null", "V NM* N(PL)|V+ pt1 TODO: Update dynamically", "V NM* N(PL)|V+ pt1 TODO: Update dynamically", "null", "null", "V NM* N(PL)|V+ pt1 TODO: Update dynamically"};

        for(int i = 0; i< meths.size(); i++){
            Method m = meths.get(i);
            Result r = aggregateRules.runAll(m);
            String expectedResult = expectedResults[i];

            //assertEquals(r.getTopRecommendation().getName(), expectedResult);

            PrintStream ps =  expectedResult.equals(r.getTopRecommendation())? System.out : System.err;
            ps.println("Metrics Testing");
            ps.println("Test: " + i + " (" + (expectedResult.equals(r.getTopRecommendation().getName())?"Success":"Fail") + ")");
            ps.println("Variable: \"" + m.getName() + "\"");
            ps.println("Canonical Type: \"" + m.getCanonicalType() + "\"");
            ps.println("Expected Recommendation: " + expectedResult);
            ps.println("Actual Recommendation: " + r.getTopRecommendation().getName());
            //assertEquals(expectedResult, r.getTopRecommendation().getName());
            System.out.println();
        }
    }
}