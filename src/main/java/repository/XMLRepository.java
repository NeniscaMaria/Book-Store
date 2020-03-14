package repository;

import domain.Client;
import domain.validators.Validator;
import domain.validators.ValidatorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class XMLRepository extends InMemoryRepository<Long,Client> {
    private String fileName;

    public XMLRepository(Validator<Client> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        return;
    }

    @Override
    public Optional<Client> save(domain.Client entity) throws ValidatorException {
        return null;
    }

    private void saveToFile(domain.Client entity) {
    }

    private void writeAllToFile(){
        return;
    }

    public Optional<Client> update(Client client){
       return null;
    }

    public Optional<Client> delete(Long ID){
        return null;
    }
}
