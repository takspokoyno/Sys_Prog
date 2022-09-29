import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    public static String validText(String text)
    {
        text = text.replaceAll("[^A-Za-zА-Яа-я\\і\\І\\Ї\\ї\\Є\\є\\']", " ");
        return text;
    }

    public static ArrayList<String> wordsList(String text)
    {
        String[] arr = text.split("\\s+");
        ArrayList<String> result = new ArrayList<String>();

        for (int i = 0; i < arr.length; i++){
            if (arr[i].length() > 30){
                arr[i] = arr[i].substring(0,29);
            }
            result.add(arr[i]);
        }

        return result;
    }

    public static  ArrayList<ArrayList<String>> createMaxPairs(ArrayList<String> words)
    {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        int max = 1;

        for(int i = 0; i < (words.size() - 1); i++){
            for(int j = (i + 1); j < words.size(); j++){
                String shorter = (words.get(i).length() <= words.get(j).length()) ? words.get(i) : words.get(j);
                int counter = 0;
                for(int k = 0; k < shorter.length(); k++){
                    if (words.get(i).charAt(k) != words.get(j).charAt(k)){
                        counter++;
                    }
                }
                if (counter == max){
                    ArrayList<String> currentPair = new ArrayList<String>();
                    currentPair.add(words.get(i));
                    currentPair.add(words.get(j));
                    result.add(currentPair);
                } else if (counter > max) {
                    result.clear();
                    max = counter;
                    ArrayList<String> currentPair = new ArrayList<String>();
                    currentPair.add(words.get(i));
                    currentPair.add(words.get(j));
                    result.add(currentPair);
                }
            }
        }

        return result;
    }

   public static ArrayList<String> mergePairs(ArrayList<ArrayList<String>> pairs){
        Set<String> set = new LinkedHashSet<>(pairs.get(0));
        for (int i = 1; i < pairs.size(); i++){
            set.addAll(pairs.get(i));
        }
        ArrayList<String> result = new ArrayList<>(set);
        return result;
    }

    public static ArrayList<ArrayList<String>> AllSubsets(ArrayList<String> set) {

        int maxSize = set.size();
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        Bulean bulean = new Bulean(maxSize);

        while (bulean.getSum10() > 2) {
            ArrayList<String> currSet = new ArrayList<String>();
            for (int i = 0; i < maxSize; i++) {
                if (bulean.getArr()[i] == 1) {
                    currSet.add(set.get(i));
                }
            }
            if (currSet.size() != 1) {
                result.add(currSet);
            }
            bulean.decr();
        }
        return result;
    }

    public static ArrayList<ArrayList<String>> foo(ArrayList<ArrayList<String>> subsets){

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < subsets.size(); i++){
            if (createMaxPairs(subsets.get(i)).size() == (subsets.get(i).size() * (subsets.get(i).size() - 1) / 2)){
                result.add(subsets.get(i));
            }
        }

        //var countPairs = createMaxPairs(subsets.get(0));
        //int expected = (subsets.get(0).size() * (subsets.get(0).size() - 1) / 2);

        int maxSize = result.get(0).size();

        ArrayList<ArrayList<String>> result2 = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < result.size(); i++){
            if (result.get(i).size() == maxSize){
                result2.add(result.get(i));
            }
        }

        return result2;
    }

    public static void main(String[] args) throws Exception
    {
        String data = readFileAsString("C:\\Users\\vladb\\Desktop\\SysProg\\Sys_Prog\\Laba1\\test.txt"); //текст з файлу
        data = validText(data);
        ArrayList<String> words = wordsList(data); // масив окремих слів
        ArrayList<ArrayList<String>> pairs = createMaxPairs(words); // пари з найбільшою відстанню
        ArrayList<String> max_words = mergePairs(pairs); // множина слів максимальної відстані
        ArrayList<ArrayList<String>> subsets = AllSubsets(max_words); // список усіх підмножин
        subsets.sort((ArrayList<String> a, ArrayList<String> b) -> b.size() - a.size());
        ArrayList<ArrayList<String>> answer = foo(subsets);
        System.out.print(answer);
    }
}

