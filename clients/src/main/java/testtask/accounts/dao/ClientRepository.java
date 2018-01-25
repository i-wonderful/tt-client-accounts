package testtask.accounts.dao;

import org.springframework.data.repository.CrudRepository;
import testtask.accounts.model.Client;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
public interface ClientRepository extends CrudRepository<ClientEntity, Long> {

}
