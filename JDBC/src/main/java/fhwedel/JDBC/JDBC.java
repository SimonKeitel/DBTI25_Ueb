package fhwedel.JDBC;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.cj.protocol.Resultset;
import java.sql.Connection;

public class JDBC {
    public static void main(String[] args) {

        try {
            // Öffnen der Datenbankverbindung - Aufgabe 1
            Connection con = connectDB();

            // Erstellen eines neuen Datensatzes - Aufgabe 2
            createNewEmployee(con, 417, "Krause", "Hendrik", "it1", "d13", "tkk");

            // Ausgeben der Personen-Tabelle - Aufgabe 3
            showEmployees(con);

            //Aktualisieren des Gehalts - Aufgabe 4
            updateSalary(con, "it1", 0.1);  

            //Löschen eines Mitarbeiters - Aufgabe 5
            deleteEmployee(con, "Tietze", "Lutz");


        } catch (Exception e) {
            System.out.println("Beim Öffnen der Datenbankverbindung oder einer SQL Operation ist eine Exception aufgetreten: " + "\n" + e);
        }
        
    }

    private static Connection connectDB() throws SQLException{
        String databaseName = "firma";
        String user = "root";
        String password = "password";
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/" + databaseName, user, password); 
    }

    private static void createNewEmployee(Connection con, int pnr, String name, String vorname, String geh_stufe, String abt_nr, String krankenkasse) {
        try {
            String sqlString = "insert into personal VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sqlString);
            // Setzen der Werte für das Vorbereitete SQL Statement
            statement.setInt(1, pnr);
            statement.setString(2, name);
            statement.setString(3, vorname);
            statement.setString(4, geh_stufe);
            statement.setString(5, abt_nr);
            statement.setString(6, krankenkasse);
            // Ausführen des SQL Statements
            statement.executeUpdate();
            
            // Schließen des PreparedStatement, freigeben von Ressourcen
            statement.close();
        } catch (SQLException e) {
            System.out.println("Beim Erstellen eines neuen Mitarbeiters ist eine SQL Exception aufgetreten: " + "\n" + e);
        }
    }

    private static void showEmployees(Connection con) {
        try {
            String tableName = "personal";
            String sqlString = "select * from " + tableName;
            Statement statement = con.createStatement();

            ResultSet results = statement.executeQuery(sqlString);

            printResults(results);
        } catch (SQLException e) {
            System.out.println("Beim Tabelle auslesen ist eine SQL Exception aufgetreten: " + "\n" + e);
        }
    }

    private static void printResults(ResultSet results) {
        try {
            //https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html
        
            ResultSetMetaData md = results.getMetaData();
            int columnAmount = md.getColumnCount(); 

            // Iteriert über die Zeilen des Ergebnisses
            while(results.next()) {
                //Iteriert über die Spalten für jede einzelne Zeile
                for (int i = 0; i < columnAmount; i++) {
                    System.out.println(md.getColumnName(i+1)+ ": " + results.getObject(i+1) + ";");
                } 
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Beim Iterieren durch die Ergebnisse ist eine SQL Exception aufgetreten: " + "\n" + e);
        }
    }

    private static void updateSalary(Connection con, String gehaltsStufe, double erhoehungProzent) {
        try {
            //Gesuchte Gehaltstufen-Zeile aus der DB holen
            String sqlStrign = "select betrag from gehalt where geh_stufe = \"" + gehaltsStufe + "\"";
            ResultSet aktuellesGehaltRS = con.createStatement().executeQuery(sqlStrign);

            //auf erste Ergebnis springen
            aktuellesGehaltRS.next();
            int aktuellesGehalt = aktuellesGehaltRS.getInt(1);
            int aktualisiertesGehalt = (int) (aktuellesGehalt * (1+erhoehungProzent));

            sqlStrign = "update gehalt set betrag = ? where geh_stufe = ?";
            PreparedStatement statement = con.prepareStatement(sqlStrign);
            statement.setInt(1, aktualisiertesGehalt);
            statement.setString(2, gehaltsStufe);
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Beim Aktualisieren des Gehalts ist eine SQL Exception aufgetreten: " + "\n" + e);
        }
    }

    private static void deleteEmployee(Connection con, String name, String vorname) {
        try {
            String sqlString = "delete from personal where name = ? and vorname = ?";
            PreparedStatement statement = con.prepareStatement(sqlString);
            statement.setString(1, name);
            statement.setString(2, vorname);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Beim Löschen eines Mitarbeiters ist eine SQL Exception aufgetreten: " + "\n" + e);
        }
    }
}