package newsystem.rest;

import newsystem.model.Project;
import newsystem.model.Task;
import newsystem.service.ProjectService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;
@Path("/projects")
@RequestScoped
public class ProjectResourceRESTService {

    @Inject
    private Logger log;

    @Inject
    ProjectService service;

    @Inject
    private Validator validator;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> listAllProjects() {
        log.info("listAllProjects");
        List<Project> projects = service.findAllOrderedByName();

        return projects;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Project lookupProjectById(@PathParam("id") long id) {
        log.info("lookupProjectById:"+id);
        Project project = service.find(id);
        if (project == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return project;
    }

    @POST
    @Path("/{id:[0-9][0-9]*}/tasks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(Task task, @PathParam("id") long id) {
        log.info("createTask in project:"+id);

        Project project = service.find(id);
        if (project == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder = null;

        try {
            project.addTask(task);

            service.update(project);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        }  catch (ValidationException e) {
            // Handle the unique constrain violation
            // None
            Map<String, String> responseObj = new HashMap<>();
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

}