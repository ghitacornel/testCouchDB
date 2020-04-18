package tests;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import model.ExampleDocument;
import org.junit.*;

import java.net.URL;

/**
 * see specific API queries on different criteria
 */
public class TestCRUD {

    private static CloudantClient client;

    @BeforeClass
    public static void setUp() throws Exception {

        client = ClientBuilder.url(new URL("http://localhost:5984"))
                .username("admin")
                .password("password")
                .build();

        System.out.println("Server Version: " + client.serverVersion());

        System.out.println("All my databases : ");
        for (String db : client.getAllDbs()) {
            System.out.println(db);
        }
    }

    @AfterClass
    public static void after() {
        client.shutdown();
    }

    private ExampleDocument initialDocument() {
        ExampleDocument document = new ExampleDocument();
        document.set_id("1");
        document.setContent("initial content");
        document.setExample(false);
        return document;
    }

    private ExampleDocument alteredDocument() {
        ExampleDocument document = new ExampleDocument();
        document.set_id("1");
        document.setContent("altered content");
        document.setExample(true);
        return document;
    }

    @Test
    public void test() {

        // Delete a database
        try {
            client.deleteDB("example_db");
        } catch (com.cloudant.client.org.lightcouch.NoDocumentException e) {
            System.out.println("OK, no database exists");
        }

        // Create a new database.
        client.createDB("example_db");

        // Get a Database instance to interact with, but don't create it if it doesn't already exist
        Database db = client.database("example_db", false);

        {// create
            ExampleDocument document = initialDocument();
            db.save(document);
            System.out.println("INSERT the document " + document);
        }

        // read
        {
            ExampleDocument actual = db.find(ExampleDocument.class, "1");
            assertDocumentsAreEquals(initialDocument(), actual);
            System.out.println("READ the document " + actual);
        }

        // update
        {
            ExampleDocument document = db.find(ExampleDocument.class, "1");
            document.setContent("altered content");
            document.setExample(true);
            db.update(document);
            System.out.println("UPDATE the document " + document);
        }

        // read
        {
            ExampleDocument actual = db.find(ExampleDocument.class, "1");
            assertDocumentsAreEquals(alteredDocument(), actual);
            System.out.println("READ the document " + actual);
        }

        // delete
        {
            ExampleDocument actual = db.find(ExampleDocument.class, "1");
            db.remove(actual);
            System.out.println("DELETE the document " + actual);
        }

        // read
        {
            boolean hasException = false;
            try {
                db.find(ExampleDocument.class, "1");
            } catch (com.cloudant.client.org.lightcouch.NoDocumentException e) {
                Assert.assertEquals("404 Object Not Found at http://localhost:5984/example_db/1. Error: not_found. Reason: deleted.", e.getMessage());
                hasException = true;
            }
            Assert.assertTrue(hasException);
        }
    }

    private void assertDocumentsAreEquals(ExampleDocument expected, ExampleDocument actual) {
        Assert.assertEquals(expected.get_id(), actual.get_id());
        Assert.assertEquals(expected.getContent(), actual.getContent());
        Assert.assertEquals(expected.isExample(), actual.isExample());

        // DO NOT COMPARE REVISIONS
        // Assert.assertEquals(expected.get_rev(), actual.get_rev());
    }
}
