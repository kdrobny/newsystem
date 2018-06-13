package newsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @NotNull
    @Size(min = 1, max = 250)
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 1, max = 500)
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Column(name = "creation_date")
    private Date creationDate;
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date startDate) {
        this.creationDate = startDate;
    }

    @NotNull
    @Size(min = 1, max = 250)
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Size(min = 1, max = 250)
    private String assignee;
    public String getAssignee() {
        return assignee;
    }
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_project")
    @XmlAttribute
    @XmlIDREF
    private Project project;
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) { // allow legacy system values
            creationDate = new Date();
        }
    }

}
