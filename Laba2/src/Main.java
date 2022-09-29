import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class Automation {
    private static class Transition {
        public char ch = 'a';
        public int to = 0;

        Transition(char ch, int to) {
            this.ch = ch;
            this.to = to;
        }
    }

    public int current_state = 0;
    ArrayList<Integer> final_states = new ArrayList<Integer>();

    Map<Integer, ArrayList<Transition>> transitions = new HashMap<Integer, ArrayList<Transition>>();

    public int nextState(char ch) throws Exception {
        var current_state_transactions = transitions.get(current_state);
        boolean contain = false;
        for (var transaction : current_state_transactions) {
            if (transaction.ch == ch) {
                current_state = transaction.to;
                contain = true;
                break;
            }
        }

        if (!contain) {
            throw new Exception("Invalid transaction");
        }

        return current_state;
    }

    public boolean isStateFinal() {
        return final_states.contains(current_state);
    }

    public static Automation ParseAutomation(String str) {
        var automation = new Automation();
        var lines = str.split("\r\n");
        automation.current_state = Integer.parseInt(lines[2]);
        var final_states = lines[3].split(" ");
        for (var state : final_states) {
            automation.final_states.add(Integer.parseInt(state));
        }
        for (int i = 4; i < lines.length; ++i) {
            var line = lines[i].split(" ");
            int from = Integer.parseInt(line[0]);
            char ch = line[1].charAt(0);
            int to = Integer.parseInt(line[2]);
            automation.transitions.putIfAbsent(from, new ArrayList<Transition>());
            automation.transitions.get(from).add(new Transition(ch, to));
        }
        return automation;
    }

    public Set<Character> possibleTransactionChar() {
        var possible_transactions = transitions.get(current_state);
        var res = new HashSet<Character>();
        for (var transition : possible_transactions) {
            res.add(transition.ch);
        }
        return res;
    }

    public static String MinimalSupportedWord(Automation first, Automation second) throws Exception {
        if(first.isStateFinal() && second.isStateFinal()) {
            return "";
        }
        if (first.isStateFinal() || second.isStateFinal()) {
            throw new Exception("One of automation rich end");
        }
        var first_chs = first.possibleTransactionChar();
        var second_chs = second.possibleTransactionChar();
        first_chs.retainAll(second_chs);
        if (first_chs.isEmpty()) {
            throw new Exception("There is no common words");
        }
        String shortest_word = "";
        char transaction_ch = '\0';
        int first_state = first.current_state;
        int second_state = second.current_state;
        for (char ch : first_chs) {
            try {
                first.current_state = first_state;
                second.current_state = second_state;
                first.nextState(ch);
                second.nextState(ch);
                String current = MinimalSupportedWord(first, second);
                if (transaction_ch == '\0' || current.length() < shortest_word.length()) {
                    shortest_word = current;
                    transaction_ch = ch;
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        if(transaction_ch == '\0'){
            throw new Exception("There is no common words");
        }
        return transaction_ch + shortest_word;
    }
}

public class Main {
    private static final String PATH = "test.txt";

    public static void main(String[] args) {
        String content;
        try {
            content = Files.readString(Paths.get(PATH), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        var automations_as_string = content.split("\r\n\r\n");
        var first = Automation.ParseAutomation(automations_as_string[0]);
        var second = Automation.ParseAutomation(automations_as_string[1]);
        String word;
        try {
            word = Automation.MinimalSupportedWord(first, second);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }
        System.out.println(word);
    }
}
