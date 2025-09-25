package overlook_hotel.overlook_hotel.controller;

import org.springframework.ui.Model;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractEntityController<T, F> {
    protected F filterFields;
    protected F focusedField;
    protected T focusedEntity;

    public AbstractEntityController() {
        this.filterFields = null;
        this.focusedField = null;
        this.focusedEntity = null;
    }

    protected void resetFocusedField(Function<F, F> resetFunction) {
        this.focusedField = resetFunction.apply(this.focusedField);
        this.focusedEntity = null;
    }

    protected void populateFocusField(Long id, Function<Long, T> findById, Function<T, F> entityToField, Function<F, F> resetFunction) {
        if (id != null) {
            this.focusedEntity = findById.apply(id);
            if (this.focusedEntity != null) {
                this.focusedField = entityToField.apply(this.focusedEntity);
            } else {
                this.focusedField = resetFunction.apply(this.focusedField);
                this.focusedEntity = null;
            }
        } else {
            this.focusedField = resetFunction.apply(this.focusedField);
            this.focusedEntity = null;
        }
    }

    protected void populateModel(Model model, List<T> entities, String entityType, List<String> columns, List<?> enumValues) {
        model.addAttribute("clients", entities);
        model.addAttribute("focusedClient", focusedEntity);
        model.addAttribute("focusField", focusedField);
        model.addAttribute("filterField", filterFields != null ? filterFields : createEmptyFilterFields());
        model.addAttribute("title", entityType.equals("client") ? "Clients" : "Employés");
        model.addAttribute("titlePage", entityType.equals("client") ? "Gestion des clients" : "Gestion des employés");
        model.addAttribute("columns", columns);
        model.addAttribute("rows", entities);
        model.addAttribute("entityType", entityType);
        if (enumValues != null) {
            model.addAttribute("jobEnumValues", enumValues);
        }
    }

    // Helper to create an empty FilterFields instance
    protected F createEmptyFilterFields() {
        try {
            return (F) Class.forName("overlook_hotel.overlook_hotel.model.FilterFields").getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
