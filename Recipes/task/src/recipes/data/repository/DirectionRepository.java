package recipes.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.data.entity.Direction;
import recipes.data.entity.Recipe;

import javax.transaction.Transactional;

@Repository
public interface DirectionRepository extends CrudRepository<Direction, Long> {
    @Transactional
    void deleteAllByRecipeId(long id);
}
