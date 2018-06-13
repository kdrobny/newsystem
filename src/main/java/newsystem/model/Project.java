package newsystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Project {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @XmlAttribute
    @XmlID
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Size(min = 1, max = 250)
    @Column(name = "legacy_code")
    private String legacyCode;
    public String getLegacyCode() { return legacyCode; }
    public void setLegacyCode(String legacyCode) {
        this.legacyCode = legacyCode;
    }

    @NotNull
    @Size(min = 1, max = 250)
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Column(name = "start_date")
    private Date startDate;
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setProject(null);
    }

    @PrePersist
    protected void onCreate() {
        if (startDate == null) { // allow legacy system values
            startDate = new Date();
        }
    }

}
