package seedu.address.model.patient;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.testutil.Assert;

public class RecordListTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Record(null, null, null, null));
    }

    @Test
    public void isValidRecordList() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> RecordList.isValidRecordList(null));

        ArrayList<Record> temp = new ArrayList<Record>();

        // valid recordLists
        temp.add(new Record("01/04/2018", "", "", ""));
        assertTrue(RecordList.isValidRecordList(new RecordList(temp))); // empty string
        temp.remove(0);
        temp.add(new Record("01/04/2018", " ", " ", " "));
        assertTrue(RecordList.isValidRecordList(new RecordList(temp))); // spaces only
        temp.remove(0);
        temp.add(new Record("01/04/2018", "b", "c", "d"));
        assertTrue(RecordList.isValidRecordList(new RecordList(temp))); // one character

        // invalid recordList
        temp.remove(0);
        Assert.assertThrows(IllegalArgumentException.class, () -> new RecordList("9th March 2017 s/ i/ t/"));
    }
}
