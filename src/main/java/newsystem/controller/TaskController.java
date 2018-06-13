package newsystem.controller;

import newsystem.data.ProjectEdit;
import newsystem.model.Task;
import newsystem.service.ProjectService;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import static javax.ws.rs.core.HttpHeaders.USER_AGENT;

@Model
public class TaskController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectEdit projectEdit;

    @Produces
    @Named
    private Task newTask;

    @PostConstruct
    public void initNewTask() {
        newTask = new Task();
    }

    public void create() {
        try {
            newTask.setProject(projectEdit.getProject());
            projectService.createTask(newTask);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Created!", "Creation successful");
            facesContext.addMessage(null, m);
            initNewTask();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Creation unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    // only usable for testing
    public void createPost() {
        try {
            String url = "http://localhost:8080/newsystem/rest/projects/"+projectEdit.getProject().getId()+"/tasks";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            // add header
            post.setHeader("User-Agent", USER_AGENT);
            post.setHeader("Content-type", "application/json");

            // json param
            JSONObject json = new JSONObject();
            json.put("name", newTask.getName());
            json.put("description", newTask.getDescription());
            json.put("assignee", newTask.getAssignee());
            json.put("status", newTask.getStatus());
            StringEntity params = new StringEntity(json.toString());
            post.setEntity(params);

            HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Creation unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Creation failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
