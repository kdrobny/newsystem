package newsystem.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Project.class)
public abstract class Project_ {

	public static volatile SingularAttribute<Project, String> name;
	public static volatile SingularAttribute<Project, Long> id;
	public static volatile SingularAttribute<Project, String> legacyCode;
	public static volatile SingularAttribute<Project, Date> startDate;
	public static volatile ListAttribute<Project, Task> tasks;

}

