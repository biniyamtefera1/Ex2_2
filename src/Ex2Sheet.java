//package assignments.ex2;
import java.io.*;

public class Ex2Sheet implements Sheet {
    private Cell[][] cells;

    /**
 * Constructor for creating a sheet with dimensions {@code w} by {@code h}.
 * The constructor initializes a 2D array of cells, where each cell is initialized
  * with the default value defined in {@link Ex2Utils#EMPTY_CELL}.
*
 * @param w the width of the sheet (number of columns).
 * @param h the height of the sheet (number of rows).
 */

    public  Ex2Sheet(int w, int h) {
        cells = new SCell[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                cells[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
            }
        }
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }
    /**
     * Checks if the given coordinates (x, y) are within the bounds of the sheet.
     *
     * @param x the x-coordinate (column index).
     * @param y the y-coordinate (row index).
     */

    @Override
    public boolean isIn(int x, int y) {
        return x >= 0 && y >= 0 && x < width() && y < height();
    }


    @Override
    public int width() {
        return cells.length;
    }

    @Override
    public int height() {
        return cells[0].length;
    }
    /**
     * Sets the value of the cell at the specified coordinates (x, y) to the given value.
     * The value can be a string, a number, or a formula (starting with '=').
     * If the value is null, it will be treated as an empty string.
     *
     * @param x the x-coordinate (column index) of the cell.
     * @param y the y-coordinate (row index) of the cell.
     * @param value the value to set in the cell. It can be a string, a number, or a formula.
     * @throws IllegalArgumentException if the coordinates (x, y) are out of bounds.
     */

    @Override
    public  void set(int x, int y, String value) {
        if (!isIn(x, y)) return;
        if (value == null) value = "";


        if (SCell.isNumber(value)) {
            cells[x][y] = new SCell(value);
            return;
        }


        if (value.startsWith("=")) {

            if (isProbablyValidFormula(value)) {
                cells[x][y] = new SCell(value);
            } else {
                cells[x][y] = new SCell(Ex2Utils.ERR_FORM);
            }
            return;
        }


        cells[x][y] = new SCell(value);
    }
    /**
     * Checks if the given value is probably a valid formula.
     * This is a basic validation that only checks the length of the value.
     * A formula must be at least two characters long (it should start with '=').
     *
     * @param val the value to check.
     * @return {@code true} if the value is likely a valid formula (length >= 2),
     *         {@code false} otherwise.
     */

    private boolean isProbablyValidFormula(String val) {
        if (val.length() < 2) return false;
        return true;
    }
    /**
     * Returns the cell at the specified coordinates (x, y).
     * If the coordinates are out of bounds, the method returns {@code null}.
     *
     * @param x the x-coordinate (column index) of the cell.
     * @param y the y-coordinate (row index) of the cell.
     * @return the cell at the specified coordinates if the coordinates are valid,
     *         or {@code null} if the coordinates are out of bounds.
     */

    @Override
    public Cell get(int x, int y) {
        if (isIn(x, y)) {
            return cells[x][y];
        }
        return null;
    }
    /**
     * Retrieves the cell based on a string index (e.g. "A1", "B2").
     * The string is parsed to extract the corresponding row and column.
     * If the coordinates are valid and within bounds, the method returns the corresponding cell.
     *
     * @param entry the string representing the cell index (e.g., "A1", "B2").
     * @return the cell corresponding to the parsed index if it is within bounds,
     *         or {@code null} if the index is invalid or out of bounds.
     */

    @Override
    public Cell get(String entry) {
        Index2D idx = parseIndex(entry);
        if (idx != null && isIn(idx.getX(), idx.getY())) {
            return cells[idx.getX()][idx.getY()];
        }
        return null;
    }
    /**
     * Returns the value of the cell at the specified coordinates (x, y).
     * The function uses a visited matrix to track cells that have already been evaluated,
     * in order to avoid infinite loops or circular dependencies in formulas.
     *
     * @param x the x-coordinate (column index) of the cell.
     * @param y the y-coordinate (row index) of the cell.
     * @return the value of the cell at the specified coordinates as a string.
     *         If the cell is a formula, it evaluates the formula and returns the result.
     */

    @Override
    public String value(int x, int y) {
        boolean[][] visited = new boolean[width()][height()];
        return evaluateCell(x, y, visited);
    }
    /**
     * Evaluates the value of the cell at the specified coordinates (x, y).
     * The function handles numbers, text, formulas, and prevents circular dependencies by tracking visited cells.
     * If the cell contains a formula, it will be evaluated recursively.
     *
     * @param x the x-coordinate (column index) of the cell.
     * @param y the y-coordinate (row index) of the cell.
     * @param visited a 2D boolean array that tracks visited cells to avoid infinite loops due to circular dependencies.
     * @return the evaluated value of the cell as a string. If the cell is empty, the function returns {@code Ex2Utils.EMPTY_CELL}.
     *         If the cell contains an invalid formula or a cycle is detected, it returns {@code Ex2Utils.ERR_CYCLE}.
     */

    private String evaluateCell(int x, int y, boolean[][] visited) {
        if (!isIn(x, y)) return Ex2Utils.EMPTY_CELL;
        if (visited[x][y]) return Ex2Utils.ERR_CYCLE;
        visited[x][y] = true;

        Cell c = cells[x][y];
        if (c == null || c.getData() == null) return Ex2Utils.EMPTY_CELL;

        String data = c.getData();

        if (SCell.isNumber(data)) {
            return data;
        }
        if (data.equals(Ex2Utils.ERR_FORM)) {
            return Ex2Utils.ERR_FORM;
        }
        if (data.startsWith("=")) {
            return evaluateFormula(data, visited);
        }
        return data; // text
    }

    /**
     * Evaluates a formula string, resolving tokens (such as cell references) and computing the result.
     * The function processes the formula, checks for circular dependencies, and calculates the final result.
     *
     * @param data the formula string to evaluate, starting with an '=' symbol (e.g., "=A1+B1").
     * @param visited a 2D boolean array that tracks visited cells to prevent circular dependencies.
     * @return the computed result of the formula as a string. If there's a circular dependency, it returns {@code Ex2Utils.ERR_CYCLE}.
     *         If the formula is invalid, it returns {@code Ex2Utils.ERR_FORM}.
     */

    private String evaluateFormula(String data, boolean[][] visited) {
        try {
            String expr = data.substring(1);
            String result = "";
            String token = "";
            for (int i = 0; i < expr.length(); i++) {
                char ch = expr.charAt(i);
                if (Character.isLetterOrDigit(ch)) {
                    token += ch;
                } else {
                    if (!token.isEmpty()) {
                        String sub = resolveToken(token, visited);
                        if (Ex2Utils.ERR_CYCLE.equals(sub)) return Ex2Utils.ERR_CYCLE;
                        result += sub;
                        token = "";
                    }
                    result += ch;
                }
            }
            if (!token.isEmpty()) {
                String sub = resolveToken(token, visited);
                if (Ex2Utils.ERR_CYCLE.equals(sub)) return Ex2Utils.ERR_CYCLE;
                result += sub;
            }
            double val = SCell.computeForm("=" + result);
            return String.valueOf(val);
        } catch (Exception e) {
            return Ex2Utils.ERR_FORM;
        }
    }
    /**
     * Resolves a token, which can either be a cell reference (e.g., "A1") or a literal value (e.g., "5").
     * If the token is a cell reference, it evaluates the cell's value and returns it.
     * If the token is a literal value, it simply returns the token.
     * If a circular dependency is detected, it returns {@code Ex2Utils.ERR_CYCLE}.
     * If the formula evaluation results in an error, it returns "0".
     *
     * @param token the token to resolve, which could be a cell reference or a literal value.
     * @param visited a 2D boolean array used to track visited cells and prevent circular dependencies.
     * @return the resolved value of the token. If it's a cell reference, the function returns its evaluated value.
     *         If the token is a literal, it returns the token as is.
     *         If a circular dependency is detected, it returns {@code Ex2Utils.ERR_CYCLE}.
     *         If there is an evaluation error, it returns "0".
     */


    private String resolveToken(String token, boolean[][] visited) {
        if (isCellReference(token)) {
            Index2D idx = parseIndex(token);
            if (idx != null) {
                String val = evaluateCell(idx.getX(), idx.getY(), visited);
                if (Ex2Utils.ERR_CYCLE.equals(val)) return Ex2Utils.ERR_CYCLE;
                if (SCell.isNumber(val)) return val;
                if (val.equals(Ex2Utils.ERR_FORM)) return "0";
                return "0";
            }
            return "0";
        }
        return token;
    }
    /**
     * Checks whether the provided token is a valid cell reference.
     * A valid cell reference is a string that starts with one or more uppercase letters (A-Z),
     * followed by one or more digits (e.g., "A1", "B10", "Z99").
     *
     * @param token the token to check, which should represent a cell reference (e.g., "A1", "B12").
     * @return {@code true} if the token is a valid cell reference, {@code false} otherwise.
     */

    private boolean isCellReference(String token) {
        if (token == null || token.isEmpty()) return false;
        int i = 0;
        while (i < token.length() && Character.isLetter(token.charAt(i))) {
            if (token.charAt(i) < 'A' || token.charAt(i) > 'Z') return false;
            i++;
        }
        if (i == 0 || i == token.length()) return false;
        while (i < token.length()) {
            if (!Character.isDigit(token.charAt(i))) return false;
            i++;
        }
        return true;
    }
    /**
     * Parses a cell reference string (e.g., "A1", "B12", "Z99") into an {@code Index2D} object.
     * The string should consist of a column part (letters) and a row part (digits).
     * The column part is interpreted as a base-26 number with 'A' = 0, 'B' = 1, ..., 'Z' = 25.
     * The row part is parsed as an integer (1-based index).
     *
     * @param entry the cell reference string to parse, e.g., "A1", "B12", "Z99".
     * @return an {@code Index2D} representing the column and row indices, or {@code null} if the input is invalid.
     */

    /**
     * Parses a cell reference string (e.g., "A1", "b12", "z99") into an {@code Index2D} object.
     * The string should consist of a column part (letters) and a row part (digits).
     * The column part is interpreted as a base-26 number with 'A' = 0, 'B' = 1, ..., 'Z' = 25.
     * The row part is parsed as an integer (1-based index).
     *
     * @param entry the cell reference string to parse, e.g., "A1", "b12", "z99".
     * @return an {@code Index2D} representing the column and row indices, or {@code null} if the input is invalid.
     */
    private Index2D parseIndex(String entry) {
        // Check if the entry is null or empty, return null if so
        if (entry == null || entry.isEmpty()) return null;

        String colPart = "";
        String rowPart = "";
        for (int i = 0; i < entry.length(); i++) {
            char ch = entry.charAt(i);
            if (Character.isLetter(ch)) {
                colPart += Character.toUpperCase(ch); // Convert lowercase letters to uppercase
            } else if (Character.isDigit(ch)) {
                rowPart += ch;
            }
        }
        if (colPart.isEmpty() || rowPart.isEmpty()) return null;
        int x = 0;
        for (int i = 0; i < colPart.length(); i++) {
            x = x * 26 + (colPart.charAt(i) - 'A');
        }

        try {
            int y = Integer.parseInt(rowPart);
            return new CellEntry(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    /**
     * Evaluates the value of a cell at position (x, y) by calling the {@code value} method.
     * This method returns the calculated value of the specified cell in the sheet.
     *
     * @param x the column index of the cell to evaluate (0-based).
     * @param y the row index of the cell to evaluate (0-based).
     * @return the value of the cell as a string, or an error message if the cell cannot be evaluated.
     */


    @Override
    public String eval(int x, int y) {
        return value(x, y);
    }
    /**
     * Evaluates the value of all cells in the sheet by calling the {@code value(i, j)} method
     * for each cell in the grid. This method iterates through each row and column of the sheet
     * and evaluates the value of each cell.
     *
     * This method does not return anything but will update the internal state of each cell
     * with its evaluated value.
     */

    @Override
    public void eval() {
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                value(i, j);
            }
        }
    }
    /**
     * Computes the depth of each cell in the sheet.
     * The depth of a cell refers to the number of steps required to reach it,
     * taking into account the dependencies between cells (such as formulas that reference other cells).
     * This method computes the depth for all cells in the sheet and returns a 2D array representing
     * the depth of each cell.
     *
     * @return a 2D array of integers representing the depth of each cell.
     */

    @Override
    public int[][] depth() {
        int[][] d = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                d[i][j] = computeDepth(i, j);
            }
        }
        return d;
    }
    /**
     * Computes the depth of a given cell at position (x, y).
     * The depth is defined as the number of steps required to evaluate the cell,
     * where a cell with a number or text has a depth of 0, and a cell with a formula has a depth of 1.
     *
     * @param x the column index of the cell (0-based).
     * @param y the row index of the cell (0-based).
     * @return the depth of the cell: 0 for numbers and text, 1 for formulas, or an error constant for invalid cases.
     */

    private int computeDepth(int x, int y) {
        Cell c = get(x, y);
        if (c == null) return 0;
        String data = c.getData();
        if (data == null) return 0;
        if (SCell.isNumber(data) || SCell.isText(data)) return 0;
        if (data.startsWith("=")) return 1;
        return Ex2Utils.ERR;
    }
    /**
     * Saves the current spreadsheet (the grid of cells) to a file.
     * The file is saved in CSV format where each line contains the row, column,
     * and the data of a non-empty cell. The first line of the file contains a header.
     *
     * @param fileName the name of the file to save the spreadsheet to.
     * @throws IOException if there is an issue writing to the file.
     */

    @Override
    public void save(String fileName) throws IOException {
        BufferedWriter w = null;
        try {
            w = new BufferedWriter(new FileWriter(fileName));
            w.write("I2CS ArielU: SpreadSheet (Ex2) assignment\n");
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    Cell cell = cells[i][j];
                    if (cell != null && !Ex2Utils.EMPTY_CELL.equals(cell.getData())) {
                        w.write(i + "," + j + "," + cell.getData() + "\n");
                    }
                }
            }
        } finally {
            if (w != null) w.close();
        }
    }
    /**
     * Loads the spreadsheet data from a file.
     * The file should be in CSV format where each line contains the row, column,
     * and the data of a non-empty cell. The first line of the file is ignored as it is a header.
     * The spreadsheet is populated based on the data from the file.
     *
     * @param fileName the name of the file to load the spreadsheet from.
     * @throws IOException if there is an issue reading from the file.
     */

    @Override
    public void load(String fileName) throws IOException {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(fileName));
            r.readLine();
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    cells[i][j] = new SCell(Ex2Utils.EMPTY_CELL);
                }
            }
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    int xx = Integer.parseInt(parts[0]);
                    int yy = Integer.parseInt(parts[1]);
                    set(xx, yy, parts[2]);
                }
            }
        } finally {
            if (r != null) r.close();
        }
    }
}