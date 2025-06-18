## Lösungen zu Aufgabe 1
### a)
Einzelne Nutzer & Bücher anlegen: 
`db.books.insertOne({invnr: 1, author: 'Marc-Uwe Kling', title: 'Die Känguru Chroniken: Ansichten eines vorlauten Beuteltiers', publisher: 'Ullstein-Verlag'})`

`db.reader.insertOne({lnr: 1, name: 'Friedrich Funke', address: 'Bahnhofsstraße 17, 23758 Oldenburg'})`

Mehrere Nutzer & Bücher anlegen
`db.books.insertMany([{invnr: 2, author: 'William Gibson', title: 'Neuromancer', publisher: 'Ace Books'}, {invnr: 3, author: 'William Shakespeare', title: 'Romeo und Julia', publisher: 'Reclam'}, {invnr: 4, author: 'F.Scott Fitzgerald', title: 'The Great Gatsby', publisher: 'Alma Classics'}, {invnr: 5, author: 'Franz Kafka', title: 'The Castle', publisher: 'Penguin Classics'}])`

`db.reader.insertMany([{lnr: 2, name: 'Ringo Kreisverkehr', address: 'Elbchaussee 386, 22609 Hamburg'}, {lnr: 3, name: 'Ruiner Wonkler', address: 'Rezatstraße 7, 91522 Ansbach'}, {lnr: 4, name: 'Peter Putsch', address: 'Alter Postweg 15, 21075 Hamburg'}, {lnr: 5, name: 'Leonardo Lutscho', address: 'Rothenbaumchaussee 10, 20148 Hamburg'}])`

Ausleihen der Bücher:
`db.borrowed.insertOne({invnr: 1, lnr: 1, duedate: new Date(2025, 08, 15)})` Warum kommt dabei September raus >:/

### b)
Suche in Büchern nach Autor:
`db.books.find({author: 'Marc-Uwe Kling'})`  

### c)
Anzahl der Bücher in DB feststellen:
`db.books.countDocuments()`

### d)
Alle Leser, die mehr als 1 Buch ausgehliehen haben, absteigend sortiert: 
`db.borrowed.aggregate([{$group:{_id: '$lnr', total: {$sum:1}}}, {$match: {total: {$gt: 1}}}, {$sort: {total: -1}}])`
* aggregate: 
* group: 
    * `_id: '$lnr'`: Gruppiert die Elemente anhand des Wertes in ihrem lnr-Feld 
    * `total: {$sum:1}`: Summiert die Vorkommen des gruppierten Wertes auf, +1 bei jedem Vorkommen 
* match gt:1 : filtert die Ausgabe von Group, alles greater than 1 ist okay
* sort -1 : absteigende Sortierung

### e)
Ausleihen / Hinzufügen zum Ausgeliehen-Dokument
`db.borrowed.insertOne({invnr: 1, lnr: 1, duedate: new Date(2025, 08, 15)})*total: {$sum:1})` 
Zurückgeben / Löschen aus Ausgeliehen-Dokument
`db.borrowed.deleteOne({invnr: 1, lnr: 1})`

### f)
db.reader.insertOne({lnr: 6, name: 'Heinz Müller', address: 'Klopstockweg 17, 38124 Braunschweig'})
db.books.insertOne({invnr: 6, author: 'Horst Evers', title: 'Der König von Berlin', publisher: 'Rowohlt-Verlag'})

db.borrowed