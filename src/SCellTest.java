import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Test.*;

class SCellTest {

    @Test
    void getOrder() {
    }

    @Test
    void testToString() {
    }

    @org.junit.jupiter.api.Test
    void setData() {
    }

    @org.junit.jupiter.api.Test
    void getData() {
    }

    @org.junit.jupiter.api.Test
    void getType() {
    }

    @org.junit.jupiter.api.Test
    void setType() {
    }

    @Test
    void setOrder() {
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


        // פונקציה טסט לבדוק חישוב פשוט
    }
    @Test
    public void isText(){
        assertTrue(SCell.isText("YOSI"));
        assertTrue(SCell.isText("HELOO"));
        assertFalse(SCell.isText("1234"));
        assertFalse(SCell.isText("=112"));

    }
    @Test
    public void testisForm(){
//       assertTrue(Cell.isForm("=67+67"));
        assertTrue(SCell.isForm("=7.0"));
        assertTrue(SCell.isForm("=(3+3)"));
        assertFalse(SCell.isForm("4.0"));

        assertFalse(SCell.isForm("3+3"));
        assertFalse(SCell.isForm("a0+3"));
    }
    @Test
    public void testComputeForm() {
//
        assertEquals(7.0, SCell.computeForm("3 + 4"));
        assertEquals(1.0, SCell.computeForm("5 - 4"));
        assertEquals(12.0, SCell.computeForm("3 * 4"));
        assertEquals(2.5, SCell.computeForm("5 / 2"));
        assertEquals(14.0, SCell.computeForm("(3 + 4) * 2"));
        assertEquals(3.0, SCell.computeForm("3 * (2 + 4) / 6"));
        assertEquals(600.0,SCell.computeForm("(2*3)*(10*10)"));


    }
}