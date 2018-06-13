package newsystem.service;

import newsystem.model.Project;
import newsystem.model.Task;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ProjectService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Project> projectEventSrc;

    public Project find(Long id) {
        Query q = this.em.createQuery("SELECT p FROM Project p LEFT OUTER JOIN FETCH p.tasks t WHERE p.id = :id");
        q.setParameter("id", id);
        return (Project) q.getSingleResult();
    }

    public List<Project> findAllOrderedByName() {

        TypedQuery<Project> query = em.createQuery("select distinct p from Project p left outer join fetch p.tasks order by p.name"
                , Project.class);
        return query.getResultList();

    }

    public Project findByName(String name) {
        Query q = this.em.createQuery("SELECT p FROM Project p WHERE p.name = :name");
        q.setParameter("name", name);
        return (Project) q.getSingleResult();
    }

    public List<Task> findAllTasksOrderedByName(Project project) {
        TypedQuery<Task> query = em.createQuery("select t from Task t where t.project = :project order by t.name"
                , Task.class);
        return query.getResultList();
    }

    public void create(Project project) {
        log.info("Creating " + project.getName());
        em.persist(project);
        projectEventSrc.fire(project);
    }

    public void update(Project project) {
        log.info("Update project " + project.getName());
        em.merge(project);
    }

    public void createTask(Task task) throws Exception {
        log.info("Creating " + task.getName());
        em.persist(task);
    }

}
