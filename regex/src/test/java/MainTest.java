import com.kipust.regex.Dfa;
import com.kipust.regex.Pattern;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    public void debugTest() throws Exception {
        String regex = "&(*('NM_'),'N_')";
        String str = "NM_N_V_";
        Pattern p = Pattern.Compile(regex);
        Dfa.DFAResult result = p.match(str);
    }

    @Test
    public void mainTest() throws Exception {
        String[] regexs = new String[]{             "&(*('NM '),'N')",  "&(*('NM '),'N')",  "&(*('NM '),'NPL')",   "&(*('NM '),'NPL')",    "&('V ',&(*('NM '),|('N','NPL')))",    "&('V ',&(*('NM '),|('N','NPL')))",   "&('V ',&(*('NM '), |('N','NPL')))"};
        String[] strs = new String[]{               "NM NM N",          "NM NM V N",        "NM NM NPL",           "NM NM N",              "V NM NM NPL",                         "V N",                                "NM NPL"};
        Boolean[] expectedResults = new Boolean[]{  true,               false,              true,                  false,                  true,                                  true,                                 false};

        for(int i = 0; i< regexs.length; i++){
            String regex = regexs[i];
            String str = strs[i];
            Boolean expectedResult = expectedResults[i];

            Pattern p = Pattern.Compile(regex);
            Dfa.DFAResult result = p.match(str);
            PrintStream ps =  expectedResult.equals(result.success())? System.out : System.err;
            ps.println("Test: " + i + " (" + (expectedResult.equals(result.success())?"Success":"Fail") + ")");
            ps.println("Regex: \"" + regex + "\"");
            ps.println("String: \"" + str + "\"");
            ps.println("Expected Result: " + (expectedResult ? "Match": "No Match"));
            ps.println("Actual Result: " + (result.success()?"Match":"No Match"));
            if(result instanceof Dfa.TrashResult){
                Dfa.TrashResult tr = (Dfa.TrashResult) result;
                ps.println("remaining tokens: " + tr.getRemainingTokens());
                ps.println("options: " + String.join(", ", (tr.getAcceptableOptions())));
                ps.println("index: " + tr.getIndex());
                ps.println("matched: \"" + str.substring(0, tr.getIndex()) + "\"");
            }
            assertEquals(expectedResult, result.success());
            System.out.println();
        }
    }
}