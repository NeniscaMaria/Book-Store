package SDI.server.validators;

import common.domain.ValidatorException;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
