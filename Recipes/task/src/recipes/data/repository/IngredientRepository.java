package recipes.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.data.entity.Ingredient;
import recipes.data.entity.Recipe;

import javax.transaction.Transactional;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    @Transactional
    void deleteAllByRecipeId(long id);
}
