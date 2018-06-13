package newsystem.data;

import newsystem.model.Project;
import newsystem.service.ProjectService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@RequestScoped
public class ProjectListProducer {

    @Inject
    ProjectService service;

    private List<Project> projects;

    @Produces
    @Named
    public List<Project> getProjects() {
        return projects;
    }

    public void onProjectListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Project project) {
        retrieveAllProjectsOrderedByName();
    }

    @PostConstruct
    public void retrieveAllProjectsOrderedByName() {
        projects = service.findAllOrderedByName();
    }

}
