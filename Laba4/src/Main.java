import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {


    public static List<FoundLexem> lexems = new ArrayList<FoundLexem>();
    public static String currToken = "undefined";
    public static String currLexem = "undefined";
    public static List<String> keywordList = Arrays.asList("const", "def", "False", "True",
            "None", "and", "assert", "break",
            "class", "continue", "del", "if",
            "else", "except", "finally",
            "for", "import", "in", "is",
            "lambda", "not", "or", "pass",
            "return", "raise", "try", "while",
            "list", "set");
    public static HashMap<String, String> regexDictionary = new HashMap<String, String>();
    public static FileWriter writer;
    static {
        try {
            writer = new FileWriter("output.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String GetSpaces(int length){
        String res = "";
        for (int i=0; i<length; i++){
            res += " ";
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get("input.txt")), StandardCharsets.UTF_8);
        regexDictionary.put("NUMBER", "\\d+(\\.\\d+)?");
        regexDictionary.put("IDENTIFIER", "[A-Za-z_][A-Za-z_0-9.]*");
        regexDictionary.put("OPERATOR", "[+-/*//%=]");
        regexDictionary.put("STRING_CONSTANT", "(\".*\") | (\'.*\')");
        regexDictionary.put("BRACKET", "[/{/}/[/]/(/)]");
        regexDictionary.put("DELIMITER", "[,:;]");
        regexDictionary.put("ESCAPE_CHARACTER", "[\n\t\r]");
        String commentRegex = "#.*[\n\r\t]";
        String commentString = "(\".*\")";

        try{
            Pattern regexComment = Pattern.compile(commentRegex);
            Matcher regexCommentMatcher = regexComment.matcher(text);
            while (regexCommentMatcher.find()) {
                String  matchText = regexCommentMatcher.group();
                int matchIndex = regexCommentMatcher.start();
                int matchEnd = regexCommentMatcher.end();
                lexems.add(new FoundLexem(matchIndex, matchText.substring(0, matchText.length()), "COMMENT"));
                text = text.substring(0, matchIndex) + GetSpaces(matchText.length())+ text.substring(matchEnd + 1);
            }

            Pattern regexString = Pattern.compile(commentString);
            Matcher regexStringMatcher = regexString.matcher(text);
            while (regexStringMatcher.find()) {
                String  matchText = regexStringMatcher.group();
                int matchIndex = regexStringMatcher.start();
                int matchEnd = regexStringMatcher.end();
                lexems.add(new FoundLexem(matchIndex, matchText.substring(0, matchText.length()), "STRING_CONST"));
                text = text.substring(0, matchIndex) + GetSpaces(matchText.length())+ text.substring(matchEnd + 1);
            }

            for( String keyword: keywordList){
                boolean notBreak = true;
                while(notBreak){
                    int index = text.indexOf(keyword);
                    if (index != -1){
                        lexems.add(new FoundLexem(index, text.substring(index, index + keyword.length()), "KEYWORD"));
                        text = text.substring(0, index) + GetSpaces(keyword.length())+ text.substring(index + keyword.length());
                    } else {
                        notBreak = false;
                    }
                }
            }

            for (var e : regexDictionary.entrySet()){
                String type = e.getKey();
                Pattern regex = Pattern.compile(e.getValue());
                Matcher regexMatcher = regex.matcher(text);
                while (regexMatcher.find()) {
                    String  matchText = regexMatcher.group();
                    int mathchIndex = regexMatcher.start();
                    lexems.add(new FoundLexem(mathchIndex, matchText, type));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(lexems);
        for (FoundLexem lexem : lexems){
            writer.write(lexem.Lexem + " : " + lexem.Type+ "\n");
        }
        System.out.println("Done!");
        writer.close();
    }
}