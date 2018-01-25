package testtask.accounts.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Alex Volobuev on 24.01.2018.
 */
@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
    List<AccountEntity> findByClientId(Long id);
}
