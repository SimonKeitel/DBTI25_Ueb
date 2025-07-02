package fhwedel.Mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.*;
import java.time.LocalDate;

public class CRUDclientTest {

    private static MongoDatabase db;

    @BeforeAll
    static void initDB() {
        db = CRUDclient.getDB();
    }

    @BeforeEach
    static void cleanDB() {
        db.getCollection("books").drop();
        db.getCollection("reader").drop();
        db.getCollection("borrowed").drop();
    }

    @Test
    void testAufgabeA() {
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> reader = db.getCollection("reader");
        MongoCollection<Document> borrowed = db.getCollection("borrowed");

        Document b1 = new Document("invnr", 1).append("author", "Marc-Uwe Kling").append("title", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("publisher", "Ullstein-Verlag");
        assertNotNull(b1, "Das hinzuzufügende Buch sollte nicht null sein");
        books.insertOne(b1);
        assertEquals(1, books.countDocuments());

        Document r1 = new Document("lnr", 1).append("name", "Friedrich Funke").append("address", "Bahnhofstraße 17, 23758 Oldenburg");
        assertNotNull(r1, "Der hinzuzufügende Leser sollte nicht null sein");
        reader.insertOne(r1);
        assertEquals(1, reader.countDocuments());

        ArrayList<Document> bookList = new ArrayList<>();
        bookList.add(new Document("invnr", 2).append("author", "William Gibson").append("title", "Neuromancer").append("publisher", "Ace Books"));
        bookList.add(new Document("invnr", 3).append("author", "William Shakespeare").append("title", "Romeo und Julia").append("publisher", "Reclam"));
        bookList.add(new Document("invnr", 4).append("author", "F.Scott Fitzgerald").append("title", "The Great Gatsby").append("publisher", "Alma Classics"));
        bookList.add(new Document("invnr", 5).append("author", "Franz Kafka").append("title", "The Castle").append("publisher", "Penguin Classics"));
        bookList.add(new Document("invnr", 6).append("author", "George Orwell").append("title", "1984").append("publisher", "Anaconda"));
        
        books.insertMany(bookList);
        assertEquals(6, books.countDocuments());

        ArrayList<Document> readerList = new ArrayList<>();
        readerList.add(new Document("lnr", 2).append("name", "Ringo Kreisverkehr").append("address", "Elbchaussee 386, 22609 Hamburg"));
        readerList.add(new Document("lnr", 3).append("name", "Ruiner Wonkler").append("address", "Rezatstraße 7, 91522 Ansbach"));
        readerList.add(new Document("lnr", 4).append("name", "Peter Putsch").append("address", "Alter Postweg 15, 21075 Hamburg"));
        readerList.add(new Document("lnr", 5).append("name", "Leonardo Lutscho").append("address", "Rothenbaumchaussee 10, 20148 Hamburg"));
        readerList.add(new Document("lnr", 6).append("name", "Frank Feuerwerk").append("address", "Lübecker Straße 230, 19059 Schwerin"));

        reader.insertMany(readerList);
        assertEquals(6, reader.countDocuments());

        borrowed.insertOne(new Document("invnr", 1).append("lnr", 1).append("duedate", LocalDate.of(2025, 8, 15))); //TODO: datumskonstruktor nicht mehr aktuell
        assertEquals(1, borrowed.countDocuments());
    }

    @Test
    void testAufgabeB() {
        MongoCollection<Document> books = db.getCollection("books");

        Document b1 = new Document("invnr", 1).append("author", "Marc-Uwe Kling").append("title", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("publisher", "Ullstein-Verlag");
        books.insertOne(b1);
        
        Document searchedBook = books.find(new Document("author", "Marc-Uwe Kling")).first();
        assertNotNull(searchedBook, "Das gesuchte Buch sollte nicht mehr Null sein");
        assertEquals("Marc-Uwe Kling", searchedBook.get("author")); //TODO: testen
    }

    @Test
    void testAufgabeC() {
        MongoCollection<Document> books = db.getCollection("books");

        ArrayList<Document> bookList = new ArrayList<>();
        bookList.add(new Document("invnr", 1).append("author", "Marc-Uwe Kling").append("title", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("publisher", "Ullstein-Verlag"));
        bookList.add(new Document("invnr", 2).append("author", "William Gibson").append("title", "Neuromancer").append("publisher", "Ace Books"));
        bookList.add(new Document("invnr", 3).append("author", "William Shakespeare").append("title", "Romeo und Julia").append("publisher", "Reclam"));
        bookList.add(new Document("invnr", 4).append("author", "F.Scott Fitzgerald").append("title", "The Great Gatsby").append("publisher", "Alma Classics"));
        bookList.add(new Document("invnr", 5).append("author", "Franz Kafka").append("title", "The Castle").append("publisher", "Penguin Classics"));
        bookList.add(new Document("invnr", 6).append("author", "George Orwell").append("title", "1984").append("publisher", "Anaconda"));
        
        books.insertMany(bookList);
        
        Long foundBookAmount = books.countDocuments();
        assertEquals(6 , foundBookAmount, "Die Datenbank sollte 6 Bücher verwalten, verwaltet aber: " + foundBookAmount);
    }

    @Test
    void testAufgabeD() {
        MongoCollection<Document> borrowed = db.getCollection("borrowed");

        ArrayList<Document> borrows = new ArrayList<>();
        borrows.add(new Document("invnr", 1).append("lnr", 1).append("duedate", LocalDate.of(2025, 8, 15)));
        borrows.add(new Document("invnr", 2).append("lnr", 1).append("duedate", LocalDate.of(2025, 8, 15)));
        borrows.add(new Document("invnr", 3).append("lnr", 1).append("duedate", LocalDate.of(2025, 8, 15)));

        borrows.add(new Document("invnr", 4).append("lnr", 2).append("duedate", LocalDate.of(2025, 9, 3)));
        borrows.add(new Document("invnr", 5).append("lnr", 2).append("duedate", LocalDate.of(2025, 9, 3)));

        borrowed.insertMany(borrows);
        assertEquals(5 , borrowed.countDocuments(), "Die Datenbank sollte 5 Ausleihen verwalten");
        
        //Document[] b = borrowed.aggregate([{$group:{_id: '$lnr', total: {$sum:1}}}, {$match: {total: {$gt: 1}}}, {$sort: {total: -1}}]); //TODO: muss riesenquerry umbauen

        //TODO: Assert lnr erster eintrage = 1, 2. = 2


    }

    @Test
    void testAufgabeE() {
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> reader = db.getCollection("reader");
        MongoCollection<Document> borrowed = db.getCollection("borrowed");

        reader.insertOne(new Document("lnr", 1).append("name", "Friedrich Funke").append("address", "Bahnhofstraße 17, 23758 Oldenburg"));

        books.insertOne(new Document("invnr", 1).append("author", "Marc-Uwe Kling").append("title", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("publisher", "Ullstein-Verlag"));

        //Ausleihen
        borrowed.insertOne(new Document("invnr", 1).append("lnr", 1).append("duedate", LocalDate.of(2025, 8, 15)));
        assertEquals(1, borrowed.countDocuments(), "Es sollte momentan genau ein Buch ausgeliehen sein");

        borrowed.deleteOne(new Document("invnr", 1).append("lnr", 1));
        assertEquals(0, borrowed.countDocuments(), "Es sollte momentan kein Buch ausgeliehen sein");
    }

    @Test
    void testAufgabeF() {
        MongoCollection<Document> books = db.getCollection("books");
        MongoCollection<Document> reader = db.getCollection("reader");
        
        books.insertOne(new Document("invnr", 1).append("author", "Marc-Uwe Kling").append("title", "Die Känguru-Chroniken: Ansichten eines vorlauten Beuteltiers").append("publisher", "Ullstein-Verlag"));
        books.insertOne(new Document("invnr", 7).append("author", "Horst Evers").append("title", "Der König von Berlin").append("publisher", "Rohwolt-Verlag"));
        
        


        Document newReader = new Document("lnr", 7).append("name", " Heinz Müller").append("address", "Klopstockweg 17, 38124 Braunschweig").append("borrowed", "TODO");
    
        reader.insertOne(newReader);

      

    }

    @Test
    void testAufgabeG() {
        //TODO: wegen Array/Liostenbums aus der F nochmal schauen
    }


}
