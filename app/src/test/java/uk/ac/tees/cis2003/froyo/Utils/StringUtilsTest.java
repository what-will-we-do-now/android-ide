package uk.ac.tees.cis2003.froyo.Utils;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StringUtilsTest {
    @Test
    public void indexOfDifference_noDiff() {
        assertEquals("indexOfDifference: No Difference - \"test\"", -1, StringUtils.indexOfDifference("test", "test"));
        assertEquals("indexOfDifference: No Difference - \"\"", -1, StringUtils.indexOfDifference("", ""));
    }

    @Test
    public void indexOfDifference_InvalidValues() {
        assertEquals("indexOfDifference: Invalid Values - v1 null", -1, StringUtils.indexOfDifference(null, "test"));
        assertEquals("indexOfDifference: Invalid Values - v2 null", -1, StringUtils.indexOfDifference("test", null));
        assertEquals("indexOfDifference: Invalid Values - Both null", -1, StringUtils.indexOfDifference(null, null));
        assertEquals("indexOfDifference: Invalid Values - null and \"null\"", -1, StringUtils.indexOfDifference(null, "null"));
    }

    @Test
    public void indexOfDifference_ValidValues() {
        assertEquals("indexOfDifference: Valid Values - \"test\" and \"\"", 0, StringUtils.indexOfDifference("test", ""));
        assertEquals("indexOfDifference: Valid Values - \"test\" and \"tempo\"", 2, StringUtils.indexOfDifference("test", "tempo"));
    }

    @Test
    public void numberOfLines_InvalidValue() {
        assertEquals("numberOfLines: Invalid Values - null", 0, StringUtils.numberOfLines(null));
    }

    @Test
    public void numberOfLines_ValidValues() {
        assertEquals("numberOfLines: Valid Values - one line", 1, StringUtils.numberOfLines("this is test case"));
        assertEquals("numberOfLines: Valid Values - two line", 2, StringUtils.numberOfLines("this is\ntest case"));
        assertEquals("numberOfLines: Valid Values - two lines with following ", 3, StringUtils.numberOfLines("this is\ntest case\n"));
    }

    @Test
    public void getIndexesOf_InvalidValues(){
        assertThrows("getIndexOf: Invalid Values - null text, match word \"test\"", NullPointerException.class, new ThrowingRunnable(){
            @Override
            public void run() {
                StringUtils.getIndexesOf(null, "test");
            }
        });

        assertThrows("getIndexOf: Invalid Values - \"test\" text, match null", NullPointerException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                StringUtils.getIndexesOf("test", null);
            }
        });
    }
}