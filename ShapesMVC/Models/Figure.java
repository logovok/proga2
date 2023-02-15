package Models;

import java.util.Arrays;
import java.util.function.Supplier;

public class Figure {
    private String shapeString = "";

    public String getShape() {
        return shapeString;
    }

    public int getHeight() {
        return shapeString.split("\n").length;
    }

    public Figure(Supplier<String> supplier) {
        shapeString += supplier.get();
    }

    public Figure indent(int indentation) {
        String indentStr = " ".repeat(indentation);
        return new Figure(() -> String.join("\n",
                Arrays.stream(this.shapeString
                                .split("\n"))
                        .map((val) -> indentStr + val)
                        .toArray(String[]::new)) + "\n");
    }


}
