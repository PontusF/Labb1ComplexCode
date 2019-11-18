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

**POST localhost:8080/school/teacher/add**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**

{
    "forename": "boi",
    "lastname": "joe",
    "email": "teach@example.com"
}

**GET localhost:8080/school/teacher**

**GET localhost:8080/school/teacher/teach@example.com**

**PUT localhost:8080/school/teacher**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"forename": "bosse",
	"lastname": "Svensson",
	"email": "teach@example.com"
}

**PATCH localhost:8080/school/teacher**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"forename": "bo",
	"email": "teach@example.com"
}


**POST localhost:8080/school/teacher/addStudent**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"teacherEmail":"teach1@example.com",
	"studentEmail":"example@example.com"
}

**DELETE localhost:8080/school/teacher/removeStudent**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"teacherEmail":"teach1@example.com",
	"studentEmail":"example@example.com"
}

**POST localhost:8080/school/subject/add**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"subject":"swedish"
}

**GET localhost:8080/school/subject**

**GET localhost:8080/school/subject/specific**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
["complex java", "swedish", "math"]

**DELETE localhost:8080/school/subject/swedish**

**POST localhost:8080/school/subject/addTeacher**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"subject":"swedish",
	"teacherEmail":"teach1@example.com"
}

**DELETE localhost:8080/school/subject/removeTeacher**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"subject":"swedish",
	"teacherEmail":"teach1@example.com"
}


**POST localhost:8080/school/subject/addStudent**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"subject":"swedish",
	"studentEmail":"example@example.com"
}


**REMOVE localhost:8080/school/subject/removeStudent**
**Header:** Content-Type Value: application/json
**body:** JSON
**exampleBody:**
{
	"subject":"swedish",
	"studentEmail":"example@example.com"
}






Jag har arbetat självständigt, med feedback och öppna disskusioner med Joel.

Jag får fortfarande inte pli på att det ibland inte funkar på min dator, även med din 
private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)


 standalone -c standalone-full.xml