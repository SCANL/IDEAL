import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ExistingCode {
    //merge 2 sorted lists
    static ArrayList<Integer> sortedList(ArrayList<Integer> originalFirstNumber, ArrayList<Integer> originalSecondNumber){
        if(originalFirstNumber.size() == 0){
            return originalSecondNumber;
        }
        if(originalSecondNumber.size() == 0){
            return originalFirstNumber;
        }
        ArrayList<Integer> firstNumber = new ArrayList<>(originalFirstNumber);
        ArrayList<Integer> secondNumber = new ArrayList<>(originalSecondNumber);
        Integer firstNumberHeads = firstNumber.get(0);
        Integer secondNumberHeads = secondNumber.get(0);
        ArrayList<Integer> x /* write a new identifier! */ = new ArrayList<>();
        ArrayList<Integer> removeNumbers = firstNumberHeads > secondNumberHeads ? firstNumber : secondNumber;
        x.add(removeNumbers.get(0));
        removeNumbers.remove(0);
        x.addAll(sortedList(firstNumber, secondNumber));
        return x;
    }

    public static void main(String[] args) {
        ArrayList list1 = new ArrayList(Arrays.asList(1, 3, 5, 8));
        ArrayList list2 = new ArrayList(Arrays.asList(2,4,9,10));
        List computed = sortedList(list1, list2);
        List expected = Arrays.asList(new int[]{1,2,3,4,5,8,9,10});
        Boolean error = false;
        int a = 0;
        if(computed.size() == expected.size()){
            for(int index = 0; index < computed.size(); index++){
                if(computed.get(index) != expected.get(index)){
                    System.err.println("expected: "+ expected.get(index) +", computed: "+ computed.get(index));
                    error = true;
                }
            }
        }
        if(!error){
            System.out.println("Success!!!");
        }
    }
}
