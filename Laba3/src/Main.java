import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static int currSymb = 0;
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
    public static FileWriter writer;
    static {
        try {
            writer = new FileWriter("output.txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wordState(String text) throws IOException {
        currToken = "IDENTIFIER";
        StringBuilder symbols = new StringBuilder();
        while((currSymb < text.length()) &&
                ((text.charAt(currSymb) >= 'A' && text.charAt(currSymb) <= 'z')
                        || (text.charAt(currSymb) >= '0' && text.charAt(currSymb) <= '9')
                        || (text.charAt(currSymb) == '_') || (text.charAt(currSymb) == '.')))
        {
            symbols.append(text.charAt(currSymb));
            currSymb += 1;
        }
        currLexem = symbols.toString();
        if(keywordList.contains(currLexem))
        {
            currToken = "KEYWORD";
        }
        writer.write(currLexem + "  -  " + currToken + "\n");

    }
    public static void numberState(String text) throws IOException {
        StringBuilder symbols = new StringBuilder();
        while((currSymb < text.length()) &&
                ((text.charAt(currSymb) >= '0' && text.charAt(currSymb) <= '9')
                        || (text.charAt(currSymb) == '.')
                        || (text.charAt(currSymb) == 'x')))
        {
            symbols.append(text.charAt(currSymb));
            currSymb += 1;
        }
        currLexem = symbols.toString();
        currToken = "NUM";
        writer.write(currLexem + "  -  " + currToken + "\n");

    }
    public static void spaceState(String text) throws IOException {
        currLexem = "\" \"";
        currToken = "SPACE";
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;
    }
    public static void operatorState(String text) throws IOException {
        currToken = "OPERATOR";
        currLexem = Character.toString(text.charAt(currSymb));
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;
    }
    public static void characterConstState(String text) throws IOException {
        currToken = "CHAR/STRING CONST";
        Character ch = text.charAt(currSymb);
        StringBuilder symbols = new StringBuilder();
        if(text.charAt(currSymb) == '"')
        {
            currSymb++;
            ch = text.charAt(currSymb);
            while (currSymb < text.length() && (!ch.equals('"'))) {
                symbols.append(text.charAt(currSymb));
                currSymb++;
                ch = text.charAt(currSymb);
            }
            currLexem = symbols.toString();
        }
        if(text.charAt(currSymb) == '\'')
        {
            currSymb++;
            while (currSymb < text.length() && (!ch.equals('\'')))
            {
                symbols.append(text.charAt(currSymb));
                currSymb++;
            }
            currLexem = symbols.toString();
        }
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;

    }
    public static void commentState(String text) throws IOException {
        currToken = "COMMENT";
        Character ch = text.charAt(currSymb);
        StringBuilder symbols = new StringBuilder();
        if(text.charAt(currSymb) == '#')
        {

            ch = text.charAt(currSymb);
            while (currSymb < text.length() && (!ch.equals('\r'))) {
                symbols.append(text.charAt(currSymb));
                currSymb++;
                ch = text.charAt(currSymb);

            }
            currLexem = symbols.toString();
        }
        writer.write(currLexem + "  -  " + currToken + "\n");

    }
    public static void bracketsState(String text) throws IOException {
        currToken = "BRACKET";
        currLexem = Character.toString(text.charAt(currSymb));
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;
    }
    public static void delimiterState(String text) throws IOException {
        currLexem = Character.toString(text.charAt(currSymb));
        currToken = "DELIM";
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;
    }
    public static void escapeState(String text) throws IOException {
        currToken = "ESCAPE CHAR";
        if(text.charAt(currSymb) == '\n')
        {
            currLexem = "\"\\n\"";
        }
        if(text.charAt(currSymb) == '\r')
        {
            currLexem = "\"\\r\"";
        }
        if(text.charAt(currSymb) == '\t')
        {
            currLexem = "\"\\t\"";
        }
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;
    }
    public static void undefinedState(String text) throws IOException {
        currLexem = Character.toString(text.charAt(currSymb));
        currToken = "undefined or special lexeme";
        writer.write(currLexem + "  -  " + currToken + "\n");
        currSymb++;
    }


    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get("input.txt")), StandardCharsets.UTF_8);
        currSymb = 0;
        while (currSymb < text.length())
        {
            if(((text.charAt(currSymb) >= 'A' && text.charAt(currSymb) <= 'z')) || (text.charAt(currSymb) == '_'))
            {
                wordState(text);
                continue;

            }
            if(text.charAt(currSymb) >= '0' && text.charAt(currSymb) <= '9')
            {
                numberState(text);
                continue;
            }
            if(text.charAt(currSymb) == '+' || text.charAt(currSymb) == '*' || text.charAt(currSymb) == '/'
                    || text.charAt(currSymb) == '%' || text.charAt(currSymb) == '^'
                    || text.charAt(currSymb) == '=')
            {
                operatorState(text);
                continue;
            }
            if(text.charAt(currSymb) == ' ')
            {
                spaceState(text);
                continue;
            }
            if((text.charAt(currSymb) == '\n') || (text.charAt(currSymb) == '\r') || (text.charAt(currSymb) == '\t'))
            {
                escapeState(text);
                continue;
            }
            if(text.charAt(currSymb) == '(' || text.charAt(currSymb) == ')'
                    || text.charAt(currSymb) == '{' || text.charAt(currSymb) == '}'
                    || text.charAt(currSymb) == '[' || text.charAt(currSymb) == ']')
            {
                bracketsState(text);
                continue;
            }
            if(text.charAt(currSymb) == ';' || text.charAt(currSymb) == ':'
                    || text.charAt(currSymb) == ',')
            {
                delimiterState(text);;
                continue;
            }
            if((text.charAt(currSymb) == '\"') || (text.charAt(currSymb) == '\''))
            {
                characterConstState(text);
                continue;
            }
            if(text.charAt(currSymb) == '#')
            {
                commentState(text);
                continue;
            }
            if(true)
            {
                undefinedState(text);
            }
            currSymb++;

        }
        System.out.println("Working!");
        writer.close();
    }
}