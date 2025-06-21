package fhwedel.Mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.jupiter.api.*;

public class CRUDclientTest {

    private static MongoDatabase db;

    @BeforeAll
    static void initDB(){
        db = CRUDclient.getDB();
    }

    @BeforeEach
    static void cleanDB(){
        db.getCollection("books").drop();
        db.getCollection("reader").drop();
        db.getCollection("borrowed").drop();
    }

    @Test
    void testAufgabeA(){
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> reader = db.getCollection("reader");

        Document b1 = new Document("invnr", 1).append("author", "Marc-Uwe Kling").append("title", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("publisher", "Ullstein-Verlag");
        books.insertOne(b1);
        Document r1 = new Document("lnr", 1).append("name", "Friedrich Funke").append("address", "Bahnhofstraße 17, 23758 Oldenburg");
        reader.insertOne(r1);
    }

}
