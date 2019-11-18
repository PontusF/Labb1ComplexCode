package se.alten.schoolproject.rest;
import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.List;


@Stateless //gör att efter vi gjort ett anrop tömmer vi minnet. det vi kommer arbeta i om man inte har kundvagn.
@NoArgsConstructor //undvik @data och @toString med joins. förutom när man har en enda entitet.
@Path("/teacher")//är localhost:...
public class TeacherController {
    //cdi
    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showTeachers() {
        try {
            //lite underligt. borde gå via modellen.
            List teachers = sal.listAllTeachers();
            return Response.ok(teachers).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    @GET
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response showTeacher(@PathParam("email") String email){
        try{
            return sal.getTeacher(email);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response addTeacher(String teacherModel) {
        try {

            return sal.addTeacher(teacherModel);

        }
        catch ( Exception e ) {
            return Response.status(422).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addStudent")
    @Produces({"application/JSON"})
    public Response addStudentToSubject( String info){
        try{
            JsonReader reader = Json.createReader(new StringReader(info));

            JsonObject jsonObject = reader.readObject();

            String studentEmail;
            String teacherEmail;
            if ( jsonObject.containsKey("teacherEmail")) {
                teacherEmail = jsonObject.getString("teacherEmail");
            }else{
                return Response.status(404).build();
            }
            if ( jsonObject.containsKey("studentEmail")) {
                studentEmail = jsonObject.getString("studentEmail");
            }else{
                return Response.status(404).build();
            }
            return sal.addStudentToTeacher(studentEmail,teacherEmail);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeStudent")
    @Produces({"application/JSON"})
    public Response removeStudentFromSubject( String info){
        try{
            JsonReader reader = Json.createReader(new StringReader(info));

            JsonObject jsonObject = reader.readObject();

            String teacherEmail;
            String studentEmail;
            if ( jsonObject.containsKey("teacherEmail")) {
                teacherEmail = jsonObject.getString("teacherEmail");
            }else{
                return Response.status(404).entity("please fill in subject").build();

            }
            if ( jsonObject.containsKey("studentEmail")) {
                studentEmail = jsonObject.getString("studentEmail");
            }else{
                return Response.status(404).entity("please fill in studentEmail").build();
            }
            return sal.removeStudentFromTeacher(studentEmail,teacherEmail);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response deleteTeacher( @PathParam("email") String email) {
        try {
            boolean foundMatches = sal.removeTeacher(email);
            String body= Boolean.toString(foundMatches);
            return Response.ok(body).build();
        }
        catch ( Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    //public void updateStudent( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) {
    @PUT
    @Produces({"application/JSON"})
    public Response updateTeacher(String teacherModel) {
        try{
            return sal.updateTeacher(teacherModel);
        } catch(Exception e){
            return Response.status(422).build();
        }
    }

    @PATCH
    @Produces({"application/JSON"})
    public Response updatePartialTeacher(String teacherModel) {
        try{
            return sal.updateTeacherPartial(teacherModel);

        } catch(EJBTransactionRolledbackException e){
            return Response.status(422).build();
        }
        catch(Exception e){
            return Response.status(422).build();
        }
    }



}
