package testtask.accounts.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

}
