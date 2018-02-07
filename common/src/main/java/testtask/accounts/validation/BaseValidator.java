package testtask.accounts.validation;

import java.util.List;
import testtask.accounts.exception.MicroserviceException;
import static testtask.accounts.exception.MicroserviceException.*;
import testtask.accounts.model.BaseModel;

/**
 *
 * @author Strannica
 */
public abstract class BaseValidator<E extends BaseModel, T extends MicroserviceException> {

    public abstract void validateItem(E item);

    protected abstract T createException(ErrorTypes errorType, String message);

    public <T> void validateNotNull(T id, String errorMessage) {
        if (id == null) {
            throw new MicroserviceException(ErrorTypes.null_argument, errorMessage);
        }
    }

    public void updateValidations(E item) {
        validateItem(item);
        if (item.getId() == null) {
            throw createException(ErrorTypes.validation, "Can't update {item} with null id");
        }
    }

    public void createValidations(E item) {
        validateItem(item);
        if (item.getId() != null) {
            throw createException(ErrorTypes.validation, "Can't create {item} with predefined id: " + item.getId());
        }
    }

    public void createValidations(List<E> items) {
        validateNotNull(items, "Can't create null {items}");
        items.forEach(this::createValidations);
    }

    public void updateValidations(List<E> items) {
        validateNotNull(items, "Null argument");
        items.forEach(this::updateValidations);
    }
}
