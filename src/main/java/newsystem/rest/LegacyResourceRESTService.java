package newsystem.rest;

import newsystem.model.Project;
import newsystem.model.Task;
import newsystem.service.ProjectService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

@Path("/import")
@RequestScoped
public class LegacyResourceRESTService {
    // private static final String legacyURL = "http://192.168.99.100:10000/legacy/resources/project/";
    private static final String legacyURL = "http://localhost:10000/legacy/resources/project/";

    @Inject
    private Logger log;

    @Inject
    ProjectService service;

    @Inject
    private Validator validator;


    @GET
    @Path("/{legacyCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response importLegacyProject(@PathParam("legacyCode") String legacyCode) {
        log.info("importLegacyProject:"+legacyCode);

        JSONObject legacyProject = null;

        try {
            legacyProject = callLegacySystem(legacyCode);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.CONFLICT);
        }

        if (legacyProject == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        // create project
        log.info("Create new project");
        Project project = new Project();
        project.setLegacyCode(legacyProject.getString("projectId"));
        project.setName(legacyProject.getString("name"));
        Long startDate = legacyProject.getLong("startDate");
        Timestamp startDateTimestamp = new Timestamp(startDate);
        project.setStartDate(new Date(startDateTimestamp.getTime()));

        // process Tasks
        JSONArray tasks = legacyProject.getJSONArray("tasks");
        if (tasks != null) {
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject legacyTask = tasks.getJSONObject(i);

                // create task
                Task task = new Task();
                task.setName(legacyTask.getString("name"));
                if (legacyTask.has("description")) {
                    task.setDescription(legacyTask.getString("description"));
                }
                if (legacyTask.has("assignee")) {
                    task.setAssignee(legacyTask.getString("assignee"));
                }
                task.setStatus(legacyTask.getString("status"));
                Long creationDate = legacyTask.getLong("creationDate");
                Timestamp creationDateTimestamp = new Timestamp(creationDate);
                task.setCreationDate(new Date(creationDateTimestamp.getTime()));

                project.addTask(task);
            }

        }

        Response.ResponseBuilder builder = null;

        try {
            // Validate project using bean validation
            validateProject(project);

            service.create(project);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    private JSONObject callLegacySystem(String legacyCode) throws Exception {

        String url = legacyURL+legacyCode;
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();

        log.info("Sending 'GET' request to URL : " + url);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String strResponse = response.toString();
        log.info("Legacy system response:"+strResponse);

        JSONObject myResponse = null;
        if (strResponse != null && !"".equals(strResponse)) {
            myResponse = new JSONObject(response.toString());
        }

        return myResponse;

    }

    private void validateProject(Project project) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Project>> violations = validator.validate(project);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the project name
        if (projectNameAlreadyExists(project.getName())) {
            throw new ValidationException("Unique project name violation");
        }
    }

    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    private boolean projectNameAlreadyExists(String name) {
        Project project = null;
        try {
            project = service.findByName(name);
        } catch (NoResultException e) {
            // ignore
        } catch (EJBException e) {
            // ignore NoResultException
            if (e.getCausedByException().getClass() != NoResultException.class) {
                throw e;
            }
        }
        return project != null;
    }

}
