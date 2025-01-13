import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Ex2Test {

    /**
     * Tests for CellEntry
     */

    CellEntry entry = new CellEntry(5, 5);

    @Test
    public void TestToString() {
        assertEquals("F5", entry.toString());
    }

    @Test
    public void TestisValid() {

        assertTrue(entry.isValid());
    }

    @Test
    public void TestgetX() {
        assertEquals(5, entry.getX());
    }

    @Test
    public void TestgetY() {
        assertEquals(5, entry.getY());
    }


    /**
     * Tests for SCell
     */

    @Test
    public void testDetectTypeWithNull() {
        SCell cell = new SCell("");
        assertEquals(Ex2Utils.TEXT, cell.detectType(null));
    }

    @Test
    public void testDetectTypeWithEmptyString() {
        SCell cell = new SCell("");
        assertEquals(Ex2Utils.TEXT, cell.detectType(""));
    }

    @Test
    public void testDetectTypeWithNumber() {
        SCell cell = new SCell("");
        assertEquals(Ex2Utils.NUMBER, cell.detectType("123"));
        assertEquals(Ex2Utils.NUMBER, cell.detectType("123.45"));
    }

    @Test
    public void testDetectTypeWithFormula() {
        SCell cell = new SCell("");
        assertEquals(Ex2Utils.FORM, cell.detectType("=A1+B1"));
    }

    @Test
    public void testDetectTypeWithText() {
        SCell cell = new SCell("");
        assertEquals(Ex2Utils.TEXT, cell.detectType("Hello"));
        assertEquals(Ex2Utils.TEXT, cell.detectType("   Some Text "));
    }

    @Test
    public void testDetectTypeWithInvalidFormat() {
        SCell cell = new SCell("");
        assertEquals(Ex2Utils.ERR_FORM_FORMAT, cell.detectType("=++A1"));
    }


    @Test
    public void testIsNumber() {
        // יצירת מופע של המחלקה שמכילה את הפונקציה isNumber

        //
        assertTrue(SCell.isNumber("123"));
        assertTrue(SCell.isNumber("3.14"));
        assertTrue(SCell.isNumber("-10"));
        assertTrue(SCell.isNumber("0.5"));


        //
        assertFalse(SCell.isNumber("abc"));
        assertFalse(SCell.isNumber("123abc"));
        assertFalse(SCell.isNumber(" "));
        assertFalse(SCell.isNumber("!@#$"));



    }

    @Test
    public void TestisText() {
        assertTrue(SCell.isText("YOSI"));
        assertTrue(SCell.isText("HELOO"));
        assertFalse(SCell.isText("1234"));
        assertFalse(SCell.isText("=112"));

    }

    @Test
    public void TestisForm() {
//       assertTrue(Cell.isForm("=67+67"));
        assertTrue(SCell.isForm("=7.0"));
        assertTrue(SCell.isForm("=(3+3)"));
        assertFalse(SCell.isForm("4.0"));

        assertFalse(SCell.isForm("3+3"));
        assertFalse(SCell.isForm("a0+3"));

        assertFalse(SCell.isForm("A1+B1"));
        assertFalse(SCell.isForm("=++A1"));
        assertTrue(SCell.isForm("=A1+"));
        assertFalse(SCell.isForm("=A1+(B1"));
        assertFalse(SCell.isForm("=A1-+B1"));
    }

    @Test
    public void TestComputeForm() {
//
        assertEquals(7.0, SCell.computeForm("3 + 4"));
        assertEquals(1.0, SCell.computeForm("5 - 4"));
        assertEquals(12.0, SCell.computeForm("3 * 4"));
        assertEquals(2.5, SCell.computeForm("5 / 2"));
        assertEquals(14.0, SCell.computeForm("(3 + 4) * 2"));
        assertEquals(3.0, SCell.computeForm("3 * (2 + 4) / 6"));
        assertEquals(600.0, SCell.computeForm("(2*3)*(10*10)"));
    }


    @Test
    public void Testset() {
        Ex2Sheet sheet = new Ex2Sheet();
        sheet.set(1, 1, "5");
        assertEquals("5.0", sheet.get(1, 1).getData());
    }

    @Test
    public void TestIsProbablyValidFormula() {
        Ex2Sheet sheet = new Ex2Sheet();
        sheet.set(0, 0, "=A1+B1");
        assertEquals("=A1+B1", sheet.get(0, 0).getData(), "The formula should be '=A1+B1'");

    }
    SCell cell = new SCell("");
    @Test
    void TestsetData() {
        
    }

    @Test
    void TestgetData() {
    }

    @Test
    void TestgetType() {
    }

    @Test
    void TestsetType() {
    }
    /**
     * Tests for Ex2sheet
     */
        @Test
        public void testDefaultConstructor() {
            Ex2Sheet sheet = new Ex2Sheet(); // Use default constructor


            assertEquals(Ex2Utils.WIDTH, sheet.width());
            assertEquals(Ex2Utils.HEIGHT, sheet.height());

            for (int i = 0; i < sheet.width(); i++) {
                for (int j = 0; j < sheet.height(); j++) {
                    assertEquals(Ex2Utils.EMPTY_CELL, sheet.get(i, j).getData());
                }
            }
        }
    @Test
    public void testOutOfBounds() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(3, 3, "OutOfBounds");
        assertNull(sheet.get(3, 3));
    }
    @Test
    public void testFormulaEvaluation() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 0, "5");
        sheet.set(1, 0, "3");
        sheet.set(2, 0, "=A0+B0");
        String result = sheet.value(2, 0);
        assertEquals("8.0", result);
    }
    @Test
    public void testSetAndGet() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(0, 0, "Hello");
        assertEquals("Hello", sheet.get(0, 0).getData());

        // Test setting a number value
        sheet.set(1, 1, "42");
        assertEquals("42.0", sheet.get(1, 1).getData());

        // Test setting a formula value (e.g., =A1+B1)
        sheet.set(2, 2, "=A1+B1");
        assertEquals("=A1+B1", sheet.get(2, 2).getData());
    }
    @Test
    public void testDepth() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 0, "5");
        sheet.set(1, 0, "3");
        sheet.set(2, 0, "=A1+A2");  // נוסחה דורשת depth=1

        int[][] depth = sheet.depth();

        assertEquals(0, depth[0][0]);
        assertEquals(0, depth[1][0]);
        assertEquals(1, depth[2][0]);
    }

    @Test
    public void testSave() throws IOException {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 0, "Hello");
        sheet.set(1, 1, "42");

        String fileName = "spreadsheet_test.csv";
        sheet.save(fileName);

        File file = new File(fileName);
        assertTrue(file.exists());


        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        assertNotNull(line);
        reader.close();
    }
    @Test
    public void testLoad() throws IOException {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 0, "Hello");
        sheet.set(1, 1, "42");

        String fileName = "spreadsheet_test.csv";
        sheet.save(fileName);

        Ex2Sheet loadedSheet = new Ex2Sheet(3, 3);
        loadedSheet.load(fileName);

        assertEquals("Hello", loadedSheet.get(0, 0).getData());
        assertEquals("42.0", loadedSheet.get(1, 1).getData());
    }
    @Test
    public void testIsIn() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        assertTrue(sheet.isIn(0, 0));
        assertTrue(sheet.isIn(2, 2));
        assertFalse(sheet.isIn(3, 3));
        assertFalse(sheet.isIn(-1, -1));
    }

}



