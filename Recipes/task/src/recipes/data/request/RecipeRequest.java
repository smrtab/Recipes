package recipes.data.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.persistence.ElementCollection;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class RecipeRequest {

    @NotBlank(message = "Name mustn't be blank")
    private final String name;

    @NotBlank(message = "Category mustn't be blank")
    private final String category;

    @NotBlank(message = "Description mustn't be blank")
    private final String description;

    @NotNull(message = "Ingredients mustn't be presented")
    @Size(min = 1, message = "Min 1 ingredient must be presented")
    private final List<String> ingredients;

    @NotNull(message = "Directions mustn't be blank")
    @Size(min = 1, message = "Min 1 direction must be presented")
    private final List<String> directions;
}
