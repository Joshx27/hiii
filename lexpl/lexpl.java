import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class Node {
    public abstract void accept(Visitor visitor);
}

class S extends Node {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class Sif extends S {
    private final Node condition;
    private final Node thenBranch;
    private final Node elseBranch;

    public Sif(Node condition, Node thenBranch, Node elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Node getCondition() {
        return condition;
    }

    public Node getThenBranch() {
        return thenBranch;
    }

    public Node getElseBranch() {
        return elseBranch;
    }
}

class Sprint extends S {
    private final Node expression;

    public Sprint(Node expression) {
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Node getExpression() {
        return expression;
    }
}

class Visitor {
    public void visit(Sif node) {
        System.out.println("Visited Sif node with condition: " + node.getCondition());
        System.out.println("Then branch: ");
        node.getThenBranch().accept(this);
        System.out.println("Else branch: ");
        node.getElseBranch().accept(this);
    }

    public void visit(Sprint node) {
        System.out.println("Visited Sprint node with expression: " + node.getExpression());
    }

    public void visit(S node) {
        System.out.println("Visited S node");
    }
}

class Token {
    static final int IF_KIND = 0;
    static final int THEN_KIND = 1;
    static final int ELSE_KIND = 2;
    static final int PRINT_KIND = 3;
    static final int IDENTIFIER_KIND = 4;

    private final int kind;
    private final String value;

    public Token(int kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    public int getKind() {
        return kind;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Token(kind: %d, value: '%s')", kind, value);
    }
}

class State {
    private final Token[] tokens;
    private int index = 0;

    public State(Token[] tokens) {
        this.tokens = tokens;
    }

    public Token next() {
        if (index < tokens.length) {
            return tokens[index++];
        }
        throw new RuntimeException("No more tokens available");
    }
}

public class lexpl {

    private static final Pattern TOKEN_PATTERN = Pattern
            .compile("\\b(if|then|else|print)\\b|[a-zA-Z_][a-zA-Z0-9_]*|\\s+");

    public static Token[] lexer(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);

        while (matcher.find()) {
            String segment = matcher.group();
            if (segment.trim().isEmpty()) {
                continue;
            }
            tokens.add(createToken(segment));
        }
        return tokens.toArray(new Token[0]);
    }

    private static Token createToken(String segment) {
        switch (segment) {
            case "if":
                return new Token(Token.IF_KIND, segment);
            case "then":
                return new Token(Token.THEN_KIND, segment);
            case "else":
                return new Token(Token.ELSE_KIND, segment);
            case "print":
                return new Token(Token.PRINT_KIND, segment);
            default:
                return new Token(Token.IDENTIFIER_KIND, segment);
        }
    }

    public static Node parser(State state) {
        return parseS(state);
    }

    private static Node parseS(State state) {
        Token token = state.next();
        System.out.println("Processing token: " + token);

        switch (token.getKind()) {
            case Token.IF_KIND:
                return parseIf(state);
            case Token.PRINT_KIND:
                return parsePrint(state);
            default:
                throw new RuntimeException("Unexpected token: " + token);
        }
    }

    private static Node parseIf(State state) {
        Node condition = new Sprint(new Node() { // Placeholder for actual condition parsing
            @Override
            public void accept(Visitor visitor) {
                // Accept method for placeholder condition node
            }
        });

        ensureNextTokenIs(state, Token.IDENTIFIER_KIND, "Expected an identifier after 'if'");
        ensureNextTokenIs(state, Token.THEN_KIND, "Expected 'then'");

        Node thenBranch = parseS(state);
        ensureNextTokenIs(state, Token.ELSE_KIND, "Expected 'else'");

        Node elseBranch = parseS(state);
        return new Sif(condition, thenBranch, elseBranch);
    }

    private static Node parsePrint(State state) {
        ensureNextTokenIs(state, Token.IDENTIFIER_KIND, "Expected an identifier after 'print'");
        return new Sprint(new Node() { // Placeholder for actual expression parsing
            @Override
            public void accept(Visitor visitor) {
                // Accept method for placeholder expression node
            }
        });
    }

    private static void ensureNextTokenIs(State state, int expectedKind, String errorMessage) {
        Token token = state.next();
        if (token.getKind() != expectedKind) {
            throw new RuntimeException(errorMessage + ", but got: " + token.getValue());
        }
        System.out.println("Processed token: " + token);
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
            Visitor visitor = new Visitor();
            root.accept(visitor);
            System.out.println("Parsing success: " + root);
        } catch (RuntimeException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}
