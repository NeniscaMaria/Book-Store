package domain.validators;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
