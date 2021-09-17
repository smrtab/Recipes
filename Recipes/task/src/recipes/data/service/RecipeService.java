package recipes.data.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.data.entity.Direction;
import recipes.data.entity.Ingredient;
import recipes.data.entity.Recipe;
import recipes.data.entity.User;
import recipes.data.repository.DirectionRepository;
import recipes.data.repository.IngredientRepository;
import recipes.data.repository.RecipeRepository;
import recipes.data.repository.UserRepository;
import recipes.data.request.RecipeRequest;
import recipes.data.response.RecipeResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecipeService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final DirectionRepository directionRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeService(
        UserRepository userRepository,
        RecipeRepository recipeRepository,
        DirectionRepository directionRepository,
        IngredientRepository ingredientRepository
    ) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.directionRepository = directionRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public long addNewRecipe(RecipeRequest request) {

        Recipe recipe = new Recipe();
        setRecipeProperties(recipe, request);

        return recipeRepository.save(recipe).getId();
    }

    public RecipeResponse getRecipe(long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Recipe doesn't exist"
            );
        }

        return mapRecipeToResponse(recipe.get());
    }

    public void deleteRecipe(long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        recipe.ifPresentOrElse(
            item -> {

                if (getOwner().getId() != item.getUser().getId()) {
                    throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Not an owner of the recipe"
                    );
                }

                recipeRepository.delete(item);
            },
            () -> {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe doesn't exist"
                );
            }
        );
    }

    public void updateRecipe(long id, RecipeRequest request) {

        Optional<Recipe> optionalRecipe
                = recipeRepository.findById(id);

        optionalRecipe.ifPresentOrElse(
            recipe -> {

                if (getOwner().getId() != recipe.getUser().getId()) {
                    throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Not an owner of the recipe"
                    );
                }

                recipeRepository.save(
                    setRecipeProperties(recipe, request)
                );
            },
            () -> {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe doesn't exist"
                );
            }
        );
    }

    public List<RecipeResponse> search(
        String category,
        String name
    ) {
        Optional<String> optionalCategory = Optional.ofNullable(category);
        Optional<String> optionalName = Optional.ofNullable(name);

        if (optionalCategory.isPresent() && optionalName.isPresent()
            || optionalCategory.isEmpty() && optionalName.isEmpty()
        ) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Wrong parameters count."
            );
        }

        List<RecipeResponse> response = new ArrayList<>();
        if (optionalCategory.isPresent()) {
            response = searchByCategory(optionalCategory.get());
        }

        if (optionalName.isPresent()) {
            response = searchByName(optionalName.get());
        }

        return response;
    }

    private List<RecipeResponse> searchByCategory(String category) {

        Optional<List<Recipe>> optionalRecipes
                = recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);

        return buildSearchResult(optionalRecipes);
    }

    private List<RecipeResponse> searchByName(String name) {

        Optional<List<Recipe>> optionalRecipes
                = recipeRepository.findAllByNameIgnoreCaseContainingOrderByDateDesc(name);

        return buildSearchResult(optionalRecipes);
    }

    private Recipe setRecipeProperties(
        Recipe recipe,
        RecipeRequest request
    ) {
        recipe.setName(request.getName());
        recipe.setDescription(request.getDescription());
        recipe.setCategory(request.getCategory());
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(getOwner());

        if (recipe.getId() != null) {
            directionRepository.deleteAllByRecipeId(recipe.getId());
            ingredientRepository.deleteAllByRecipeId(recipe.getId());
        }

        List<Ingredient> ingredientList = new ArrayList<>();
        for (String description: request.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setDescription(description);
            ingredient.setRecipe(recipe);
            ingredientList.add(ingredient);
        }

        List<Direction> directionList = new ArrayList<>();
        for (String description: request.getDirections()) {
            Direction direction = new Direction();
            direction.setDescription(description);
            direction.setRecipe(recipe);
            directionList.add(direction);
        }

        recipe.setIngredients(ingredientList);
        recipe.setDirections(directionList);

        return recipe;
    }

    private List<RecipeResponse> buildSearchResult(
        Optional<List<Recipe>> optionalRecipes
    ) {

        List<RecipeResponse> response = new ArrayList<>();

        if (optionalRecipes.isPresent()) {
            for (Recipe recipe : optionalRecipes.get()) {
                log.info(recipe.toString());
                response.add(mapRecipeToResponse(recipe));
            }
        }
        return response;
    }

    private RecipeResponse mapRecipeToResponse(Recipe recipe) {

        RecipeResponse response = new RecipeResponse();

        response.setName(recipe.getName());
        response.setDescription(recipe.getDescription());
        response.setCategory(recipe.getCategory());
        response.setDate(recipe.getDate());
        response.setIngredients(recipe.getIngredientsDescriptions());
        response.setDirections(recipe.getDirectionsDescriptions());

        return response;
    }

    public User getOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : (User) authentication.getPrincipal();
    }
}
