import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Node {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class S extends Node {

}

class Sif extends S {
    Node e, s1, s2;

    public Sif(Node e, Node s1, Node s2) {
        this.e = e;
        this.s1 = s1;
        this.s2 = s2;
    }
}

class Sprint extends S {
    Node e;

    public Sprint(Node e) {
        this.e = e;
    }
}

class Visitor {
    public void visit(Sif node) {
        System.out.println("Visited Sif node");
    }

    public void visit(Sprint node) {
        System.out.println("Visited Sprint node");
    }

    public void visit(Node node) {
        System.out.println("Visited Node");
    }
}

class Token {
    static int IF_KIND = 0;
    static int THEN_KIND = 1;
    static int ELSE_KIND = 2;
    static int PRINT_KIND = 3;
    static int IDENTIFIER_KIND = 4;

    public int kind;
    public String value;

    public Token(int kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token(kind: " + kind + ", value: '" + value + "')";
    }
}

class State {
    private Token[] tokens;
    private int index;

    public State(Token[] tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    public Token next() {
        if (index < tokens.length) {
            return tokens[index++];
        }
        throw new RuntimeException("No more tokens available");
    }
}

public class lexpl {

    static Token[] lexer(String input) {
        List<Token> tokens = new ArrayList<>();

        Pattern tokenPattern = Pattern.compile("\\b(if|then|else|print)\\b|[a-zA-Z_][a-zA-Z0-9_]*|\\s+");
        Matcher matcher = tokenPattern.matcher(input);

        while (matcher.find()) {
            String segment = matcher.group();

            if (segment.matches("\\s+")) {
                continue;
            }

            Token token;
            switch (segment) {
                case "if":
                    token = new Token(Token.IF_KIND, segment);
                    break;
                case "then":
                    token = new Token(Token.THEN_KIND, segment);
                    break;
                case "else":
                    token = new Token(Token.ELSE_KIND, segment);
                    break;
                case "print":
                    token = new Token(Token.PRINT_KIND, segment);
                    break;
                default:
                    token = new Token(Token.IDENTIFIER_KIND, segment);
                    break;
            }
            tokens.add(token);
        }

        return tokens.toArray(new Token[0]);
    }

    static Node parser(State state) {
        return s(state);
    }

    static Node s(State state) {
        Token token = state.next();
        System.out.println("Processing token: " + token);

        if (token.kind == Token.IF_KIND) {
            Node e = new Node();

            token = state.next();
            if (token.kind != Token.IDENTIFIER_KIND) {
                throw new RuntimeException("Expected an identifier after 'if', but got: " + token.value);
            }

            token = state.next();
            System.out.println("Expecting 'then' but got: " + token);
            if (token.kind != Token.THEN_KIND) {
                throw new RuntimeException("Expected 'then', but got: " + token.value);
            }

            Node s1 = s(state);

            token = state.next();
            System.out.println("Expecting 'else' but got: " + token);
            if (token.kind != Token.ELSE_KIND) {
                throw new RuntimeException("Expected 'else', but got: " + token.value);
            }

            Node s2 = s(state);
            return new Sif(e, s1, s2);

        } else if (token.kind == Token.PRINT_KIND) {
            Node e = new Node();

            token = state.next();
            if (token.kind != Token.IDENTIFIER_KIND) {
                throw new RuntimeException("Expected an identifier after 'print', but got: " + token.value);
            }

            System.out.println("Processed print statement with identifier: " + token.value);
            return new Sprint(e);

        } else {
            throw new RuntimeException("Unexpected token: " + token);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No input provided.");
            return;
        }

        String input = args[0];
        System.out.println("Input: " + input);

        Token[] tokens = lexer(input);

        System.out.println("Tokens:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        State state = new State(tokens);

        try {
            Node root = parser(state);
            Visitor v = new Visitor();
            root.accept(v);
            System.out.println("Parsing success: " + root);
        } catch (RuntimeException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}