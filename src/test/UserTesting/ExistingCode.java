package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExistingCode {
    //merge 2 sorted lists
    static ArrayList<Integer> mergeSortedLists(ArrayList<Integer> originalFirstNumber, ArrayList<Integer> originalSecondNumber){
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
        x.addAll(mergeSortedLists(firstNumber, secondNumber));
        return x;
    }

    private static ArrayList<Integer> integerList(ArrayList<Double> doubleNums) {
        ArrayList<Integer> nums = new ArrayList<>();
        for(double num: doubleNums) {
            nums.add((int) num);
        }
        return nums;
    }

    public static void main(String[] args) {
        ArrayList list1 = integerList(new ArrayList(Arrays.asList(1.2, 3.0, 5.1, 8.2)));
        ArrayList list2 = integerList(new ArrayList(Arrays.asList(2.4,4.7,9.5,10.2)));
        List computed = mergeSortedLists(list1, list2);
        List expected = Arrays.asList(new double[]{1.2,2.4,3.0,4.7,5.1,8.2,9.5,10.2});
        Boolean error = false;
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
