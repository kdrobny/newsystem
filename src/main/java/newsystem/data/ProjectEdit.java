package newsystem.data;

import newsystem.model.Project;
import newsystem.service.ProjectService;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ProjectEdit implements Serializable {

    private Project project;

    @EJB
    private ProjectService projectService;

    public String save() {
        projectService.update(project);
        return "/products?faces-redirect=true";
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}

