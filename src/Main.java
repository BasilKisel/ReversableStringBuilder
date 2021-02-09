import static java.lang.System.out;

import string.builder.ReversibleStringBuilder;

public class Main {
    public static void main(String... args) {
        ReversibleStringBuilder rsb = new ReversibleStringBuilder("Hello");
        out.println(rsb.toString() + " - initial state");
        rsb.Undo();
        out.println(rsb.toString() + " - Undo on initial state");
        rsb.append('!');
        out.println(rsb.toString() + " - Append '!'");
        rsb.Undo();
        out.println(rsb.toString() + " - Undo appending '!'");
        rsb.append('!');
        out.println(rsb.toString() + " - Append '!' once again");
        rsb.insert(5, ", World");
        out.println(rsb.toString() + " - Insert ', World'");
        rsb.Undo();
        out.println(rsb.toString() + " - Undo inserting ', World'");
        rsb.Undo();
        out.println(rsb.toString() + " - Undo appending '!'");
        rsb.Undo();
        out.println(rsb.toString() + " - Undo on initial state again");
    }
}