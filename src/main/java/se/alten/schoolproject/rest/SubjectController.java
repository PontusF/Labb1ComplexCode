package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import org.hornetq.utils.json.JSONArray;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.model.SubjectModel;

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

@Stateless
@NoArgsConstructor
@Path("/subject")
public class SubjectController {

    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response listSubjects() {
        try {
            List subject = sal.listAllSubjects();
            return Response.ok(subject).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/specific")
    @Produces({"application/JSON"})
    public Response getSubject( List titles){
        try{
            return sal.getSubjects(titles);
        }
        catch(Exception e){
            return Response.status(404).build();
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

            String subject;
            String studentEmail;
            if ( jsonObject.containsKey("subject")) {
                subject = jsonObject.getString("subject");
            }else{
                return Response.status(404).build();
            }
            if ( jsonObject.containsKey("studentEmail")) {
                studentEmail = jsonObject.getString("studentEmail");
            }else{
                return Response.status(404).build();
            }
            return sal.addStudentToSubject(studentEmail,subject);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addTeacher")
    @Produces({"application/JSON"})
    public Response addTeacherToSubject( String info){
        try{
            JsonReader reader = Json.createReader(new StringReader(info));

            JsonObject jsonObject = reader.readObject();

            String subject;
            String teacherEmail;
            if ( jsonObject.containsKey("subject")) {
                subject = jsonObject.getString("subject");
            }else{
                return Response.status(404).build();
            }
            if ( jsonObject.containsKey("teacherEmail")) {
                teacherEmail = jsonObject.getString("teacherEmail");
            }else{
                return Response.status(404).build();
            }
            return sal.addTeacherToSubject(teacherEmail,subject);
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

            String subject;
            String studentEmail;
            if ( jsonObject.containsKey("subject")) {
                subject = jsonObject.getString("subject");
            }else{
                return Response.status(404).entity("please fill in subject").build();

            }
            if ( jsonObject.containsKey("studentEmail")) {
                studentEmail = jsonObject.getString("studentEmail");
            }else{
                return Response.status(404).entity("please fill in studentEmail").build();
            }
            return sal.removeStudentFromSubject(studentEmail,subject);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeTeacher")
    @Produces({"application/JSON"})
    public Response removeTeacherFromSubject( String info){
        try{
            JsonReader reader = Json.createReader(new StringReader(info));

            JsonObject jsonObject = reader.readObject();

            String subject;
            String teacherEmail;
            if ( jsonObject.containsKey("subject")) {
                subject = jsonObject.getString("subject");
            }else{
                return Response.status(404).entity("please fill in subject").build();

            }
            if ( jsonObject.containsKey("teacherEmail")) {
                teacherEmail = jsonObject.getString("teacherEmail");
            }else{
                return Response.status(404).entity("please fill in teacherEmail").build();
            }
            return sal.removeTeacherFromSubject(teacherEmail,subject);
        }
        catch(Exception e){
            return Response.status(404).build();
        }
    }


    @POST
    @Path("/add")
    @Produces({"application/JSON"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSubject(String subjects) {
        try {
            return sal.addSubject(subjects);
        } catch (Exception e ) {
            return Response.status(404).build();
        }
    }
    @DELETE
    @Path("{subject}")
    @Produces({"application/JSON"})
    public Response deleteSubject( @PathParam("subject") String subject) {
        try {
            boolean foundMatches = sal.removeSubject(subject);
            String body= Boolean.toString(foundMatches);
            return Response.ok(body).build();
        }
        catch ( Exception e ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}