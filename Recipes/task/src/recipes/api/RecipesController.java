package recipes.api;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.data.entity.Recipe;
import recipes.data.request.RecipeRequest;
import recipes.data.response.RecipeResponse;
import recipes.data.service.RecipeService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RecipesController {

    private final RecipeService recipeService;

    @Autowired
    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<Object> addNewRecipe(@Valid @RequestBody RecipeRequest request) {
        return ResponseEntity.ok(Map.of("id", recipeService.addNewRecipe(request)));
    }

    @GetMapping("/api/recipe/{id}")
    public RecipeResponse getRecipe(@PathVariable long id) {
        return recipeService.getRecipe(id);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity updateRecipe(@PathVariable long id, @Valid @RequestBody RecipeRequest request) {
        recipeService.updateRecipe(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity deleteRecipe(@PathVariable long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/recipe/search")
    public List<RecipeResponse> search(
        @RequestParam(name = "category", required = false) String category,
        @RequestParam(name = "name", required = false) String name
    ) {
        return recipeService.search(category, name);
    }
}
