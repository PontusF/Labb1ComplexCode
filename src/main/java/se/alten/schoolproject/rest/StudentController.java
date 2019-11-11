package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import se.alten.schoolproject.dao.SchoolAccessLocal;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Stateless //gör att efter vi gjort ett anrop tömmer vi minnet. det vi kommer arbeta i om man inte har kundvagn.
@NoArgsConstructor //undvik @data och @toString med joins. förutom när man har en enda entitet.
@Path("/student")//är localhost:...
public class StudentController {
    //cdi
    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showStudents() {
        try {
            //lite underligt. borde gå via modellen.
            List students = sal.listAllStudents();
            return Response.ok(students).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    @GET
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response showStudent(@PathParam("email") String email){
        try{
            return sal.getStudent(email);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    public Response addStudent(String studentModel) {
        try {

            return sal.addStudent(studentModel);

        }
        catch ( Exception e ) {
            return Response.status(422).build();
        }
    }

    @DELETE
    @Path("{email}")
    @Produces({"application/JSON"})
    public Response deleteUser( @PathParam("email") String email) {
        try {
            boolean foundMatches = sal.removeStudent(email);
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
    public Response updateStudent(String studentModel) {
        try{
            return sal.updateStudent(studentModel);
        } catch(Exception e){
            return Response.status(422).build();
        }
    }

    @PATCH
    @Produces({"application/JSON"})
    public Response updatePartialAStudent(String studentModel) {
        try{
            return sal.updateStudentPartial(studentModel);

        } catch(EJBTransactionRolledbackException e){
            return Response.status(422).build();
        }
        catch(Exception e){
            return Response.status(422).build();
        }
    }



}
