package recipes.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.data.entity.Recipe;
import recipes.data.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    Optional<List<Recipe>> findAllByUserIdAndCategoryIgnoreCaseOrderByDateDesc(Long id, String category);
    Optional<List<Recipe>> findAllByUserIdAndNameIgnoreCaseContainingOrderByDateDesc(Long id, String name);
    Optional<List<Recipe>> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);
    Optional<List<Recipe>> findAllByNameIgnoreCaseContainingOrderByDateDesc(String name);
}
