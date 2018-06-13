package newsystem.controller;

import newsystem.model.Project;
import newsystem.service.ProjectService;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Model
public class ProjectController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private ProjectService projectService;

    @Produces
    @Named
    private Project newProject;

    @PostConstruct
    public void initNewProject() {
        newProject = new Project();
    }

    public void create() {
        try {
            projectService.create(newProject);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Created!", "Creation successful");
            facesContext.addMessage(null, m);
            initNewProject();
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
