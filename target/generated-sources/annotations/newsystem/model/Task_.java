package newsystem.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Task.class)
public abstract class Task_ {

	public static volatile SingularAttribute<Task, String> name;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, Project> project;
	public static volatile SingularAttribute<Task, Long> id;
	public static volatile SingularAttribute<Task, String> assignee;
	public static volatile SingularAttribute<Task, Date> creationDate;
	public static volatile SingularAttribute<Task, String> status;

}

