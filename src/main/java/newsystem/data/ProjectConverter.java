package newsystem.data;

import newsystem.model.Project;
import newsystem.service.ProjectService;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

@Named
@RequestScoped
public class ProjectConverter implements Converter {

    @EJB
    private ProjectService projectService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            Long id = Long.valueOf(value);
            return projectService.find(id);
        } catch (NumberFormatException e) {
            throw new ConverterException("The value is not a valid Project ID: " + value, e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Project) {
            Long id = ((Project) value).getId();
            return (id != null) ? String.valueOf(id) : null;
        } else {
            throw new ConverterException("The value is not a valid Project instance: " + value);
        }
    }

}