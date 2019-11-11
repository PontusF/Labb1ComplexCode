Pontus Fredriksson

Använd det modifierade school.cli scriptet. Där hittar du även detaljerna för SQL.
Deploya precis som du gått igenom;
    deploya via InteliJ>Run>EditConfigurations>Maven:
        wildfly:undeploy clean:clean wildfly:deploy

EndPoints och dess info:
**GET localhost:8080/school/student**


**GET localhost:8080/school/student/exampleMail@example.com**


**POST localhost:8080/school/student/add**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"forename": "petra",
	"lastname": "annadottir",
	"email": "exampleMail@example.com"
}


**PUT localhost:8080/school/student**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"forename": "Lisa",
	"lastname": "Svensson",
	"email": "exampleMail@example.com"
}


**PATCH localhost:8080/school/student**
**body:** JSON
**exampleBody:**
{
	"forename": "Karin",
	"email": "exampleMail@example.com"
}

**DELETE localhost:8080/school/student/exampleMail@example.com**




Jag har arbetat självständigt, med feedback och öppna disskusioner med Joel.
Ibland(verkar vara random) när jag startar wildfly(via cmd eller när jag deployar i IntelliJ
uppstår följande problem:
Kommunikation till servern fungerar som den ska, men felkoderna i StudentController
returneras alltid till Insomnia oavsett.
Inga felmedellanden uppstår i WildFly loggen.
Så det ser ofta ut som programmet inte fungerar fastän databasen uppdateras.
Ungefär 66-75% av gångerna jag startar wildFly uppstår det.
Om det händer: Stäng av och sätt på wildfly några gånger tills det fungerar.
När det väl fungerar funkar det tills ny deploy/serverstart.

Jag misstänker att det är på grund av Att 100% av CPUN används under start av WildFly och att datorn missar
detlajer ibland. Jag har inte funnit en annan lösning en att testa att starta flera ggr.

En annan sak jag inte får att fungera är att post och patch Även om jag Kör en flush()
I början av Min StudentTransaction.getStudent() så fårUpdateStudent i DAOen infon från innan
Updaten.
Vet inte om min dator är skräp eller vad det är.


 standalone -c standalone-full.xml