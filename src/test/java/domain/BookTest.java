package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class BookTest {
    private Book book;

    private static final Long ID = 1L;
    private static final Long NEW_ID = 2L;
    private static final String SERIAL_NUMBER = "serial1";
    private static final String NEW_SERIAL_NUMBER = "serial2";
    private static final String NAME = "book1";
    private static final String NEW_NAME = "new_book";
    private static final String AUTHOR = "author1";
    private static final String NEW_AUTHOR = "new_author";
    private static final int YEAR = 2013;
    private static final int NEW_YEAR = 2018;

    @Before
    public void setUp() throws Exception {
        book = new Book(SERIAL_NUMBER, NAME, AUTHOR, YEAR);
        book.setId(ID);
    }

    @After
    public void tearDown() throws Exception {
        book = null;
    }

    @Test
    public void testGetSerialNumber() throws Exception {
        assertEquals("Serial numbers should be equal", SERIAL_NUMBER, book.getSerialNumber());
    }

    @Test
    public void testSetSerialNumber() throws Exception {
        book.setSerialNumber(NEW_SERIAL_NUMBER);
        assertEquals("Serial numbers should be equal", NEW_SERIAL_NUMBER, book.getSerialNumber());
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals("Ids should be equal", ID, book.getId());
    }

    @Test
    public void testSetId() throws Exception {
        book.setId(NEW_ID);
        assertEquals("Ids should be equal", NEW_ID, book.getId());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Names should be be the same", NAME, book.getName());
    }

    @Test
    public void testSetName() throws Exception {
        book.setName(NEW_NAME);
        assertEquals("Names should be the same", NEW_NAME, book.getName());
    }

    @Test
    public void testGetAuthor() throws Exception {
        assertEquals("Authors should be the same", AUTHOR, book.getAuthor());
    }

    @Test
    public void testSetAuthor() throws Exception {
        book.setAuthor(NEW_AUTHOR);
        assertEquals("Authors should be the same", NEW_AUTHOR, book.getAuthor());
    }

    @Test
    public void testGetYear() throws Exception {
        assertEquals("Years should be equal", YEAR, book.getYear());
    }

    @Test
    public void testSetYear() throws Exception {
        book.setYear(NEW_YEAR);
        assertEquals("Years should be equal", NEW_YEAR, book.getYear());
    }

}