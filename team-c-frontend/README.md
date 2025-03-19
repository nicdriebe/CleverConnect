# CleverConnect

## Inhaltsverzeichnis
- [Über das Projekt](#über-das-projekt)
- [Installation](#installation)
- [Ausführung der Anwendung](#ausführung-der-anwendung)
- [Anleitung zur Nutzung der Anwendung](#anleitung-zur-nutzung-der-anwendung)
- [Technologien](#technologien)
- [Languages](#languages)
- [Bekannte Probleme](#bekannte-probleme)
- [Mitwirkende im Projekt](#mitwirkende-im-projekt)


## Über das Projekt
Die ideale Plattform der HTW Berlin, die Studierende mit externen Experten vernetzt.<br><br>
Für Studierende: Finde mit nur weniges Klicks die passende Zweitbetreuung für deine Bachelorarbeit.<br>
Für Externe: Erweitere dein Netzwerk, biete deine Expertise an und unterstütze Studierende bei ihrer Bachelorarbeit.

##### Das Projekt ist im 3.Semesters des Studiengangs Informatik und Wirtschaft der HTW Berlin entstanden.


## Installation

Schauen Sie sich gerne zusätzlich noch die README des Backend-Repositories an, dort erhalten Sie weitere Informationen, wie zum Thema "PostgresSQL-Datenbank anlegen":<br>


### Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass die folgenden Voraussetzungen erfüllt sind:

- Git ist auf Ihrem Computer installiert. Wenn nicht, können Sie es von [dieser Website](https://git-scm.com/downloads) herunterladen und installieren.
- Sie haben einen GitHub-Account und die Zugriffsrechte auf das Repository.

### Repository herunterladen

- Um die Anwendung auf Ihrem lokalen System auszuführen, müssen Sie das Repository des Frontends und des Backends herunterladen. Befolgen Sie die folgenden Schritte, um die Repository herunterzuladen und die Anwendung zu starten.
- Öffnen Sie die Kommandozeile und navigieren Sie zu dem Ordner, in dem Sie das Repository klonen möchten. Führen Sie dann den folgenden Befehl aus, um die Repository des Frontends herunterzuladen:
  git clone https://github.com/nicdriebe/CleverConnect.git und drücken Sie die Eingabetaste.


## Ausführung der Anwendung

Um unsere Anwendung erfolgreich zu starten, ist es wichtig, dass sowohl das Frontend als auch das Backend in Ihrer Integrierten Entwicklungsumgebung (IDE), wie zum Beispiel IntelliJ IDEA, korrekt eingerichtet und geöffnet sind. Folgen Sie diesen Schritten, um die Anwendung zu starten und auf Ihrem lokalen System auszuführen.

### Vorbereitung Ihrer IDE
Stellen Sie sicher, dass Ihre IDE für die Ausführung von Java-Projekten konfiguriert ist und alle erforderlichen Abhängigkeiten installiert sind. Öffnen Sie sowohl das Frontend- als auch das Backend-Projekt in Ihrer IDE, um eine reibungslose Ausführung zu gewährleisten.

#### Anwendung starten
Sie haben zwei Möglichkeiten, die Anwendung zu starten:

#### Option 1: Start über die Befehlszeile

- Für Windows-Nutzer: Öffnen Sie die Befehlszeile im Projektverzeichnis und führen Sie mvnw.cmd aus. Für Mac & Linux-Nutzer: Verwenden Sie ./mvnw im Terminal.
- Nachdem der Server gestartet wurde, öffnen Sie Ihren Webbrowser und gehen Sie zu http://localhost:8080, um auf die Anwendung zuzugreifen.

#### Option 2: Start über die IDE

- Verwenden Sie die "Run"-Funktion Ihrer IDE, um das Projekt ohne die Befehlszeile zu starten. Dies kann üblicherweise über einen grünen "Run"-Button oder durch Ausführung des Projekts über das Menü geschehen.
- Sobald das Projekt läuft, besuchen Sie http://localhost:8080 in Ihrem Webbrowser, um die Anwendung zu nutzen.

## Anleitung zur Nutzung der Anwendung

Herzlich Willkommen auf unserer Startseite!

Hier befindet sich der Login-Bereich. Wenn Sie bereits ein Konto haben, können Sie sich hier einloggen. Falls Sie neu hier sind und sich registrieren möchten, klicken Sie auf den "Hier registrieren"-Button.

![startseite.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fstartseite.png)

Hier haben Sie die Möglichkeit, sich zu registrieren – entweder als Studierende/r oder Externe/r.

![popup_registrieren.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fpopup_registrieren.png)

Je nach Auswahl landen Sie in dem Registrierungsformular des Studierenden oder Externen.
Nach erfolgreicher Registrierung erhalten Sie eine Bestätigungsemail an die angegebene Email-Adresse.

Registrierungsformular-Student:

![registrierung_student.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fregistrierung_student.png)

Ein eingeloggter Student sieht die Übersichtsliste aller Externen. <br>
Kontaktieren des Externen funktioniert, indem Sie auf den "Email schreiben"-Button klicken. Außerdem erhalten Sie weitere Informationen des Externen unter "More Info".

![studierende-view.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fstudierende-view.png)

Registrierungsformular-Extern:
Der Externe kann seine Verfügbarkeiten und durch das Dropdown-Menü mehrere Fachgebiete angeben.

![registrierungs_formular_ohne_beschreibung.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fregistrierungs_formular_ohne_beschreibung.png)

Bei Eingabe eines Bachelorthemas öffnet sich ein weiteres Feld "Beschreibung", wo weitere Informationen zum Bachelorthema angegeben werden können.

![registrierungsformular_mit_beschreibung.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fregistrierungsformular_mit_beschreibung.png)

Eingeloggte Externe sehen nur ihre eigenen Daten, welche bearbeitbar sind. <br>
(Die Checkboxen haben noch keine Funktionen.)

![extern-view.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fextern-view.png)

Externe können ihr Passwort ändern.

![Extern-view-passwort.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2FExtern-view-passwort.png)

Externe können ihr Profil löschen.

![extern-view-profil löschen.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fextern-view-profil%20l%C3%B6schen.png)

Administratoren (Professoren) sehen eine Übersichtsliste aller Externe und können nach Nachname und/oder Registrierungsdatum sortieren.

![prof-view.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fprof-view.png)

Mit Klick auf "Neue User/in anlegen" können Admins Externe anlegen.

![prof-create-user.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fprof-create-user.png)

Zudem können Admins Externe löschen.

![prof-user-delete.png](frontend%2Fthemes%2Fbatchmatch1%2Fviews%2Fprof-user-delete.png)

Zum Schluss können sich alle Rollen mit dem "Logout"-Button ausloggen.

## Technologien
- Vaadin
- SpringBoot
- Maven
- PostgreSQL
- Postman
- Git
- H2 Datenbank
- OpenAPI/Swagger
- JPA/Hibernate

## Languages
- Java 
- HTML
- CSS
- SQL

## Bekannte Probleme

## Mitwirkende im Projekt
Fürüze Saritoprak, Sabine Matthies, Sude Ural, Nicole Driebe, Julia Neubert, Vivian Grace Glaubig


