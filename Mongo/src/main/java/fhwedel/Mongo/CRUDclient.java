package fhwedel.Mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

// Zum Ausführen in /Mongo/ ordner rein, compilen mit "mvn compile && mvn exec:java"

public class CRUDclient {
    // Verbindung zu unserer MongoDB herstellen
    private static String connectionString = "mongodb://localhost:21017";
    private static final MongoClient mongoClient = MongoClients.create(connectionString);
    private static MongoDatabase db = mongoClient.getDatabase("myDB");
   
    public static MongoDatabase getDB() {
      return db;
    } 

    public static void closeDB() {
      mongoClient.close();
    }
}