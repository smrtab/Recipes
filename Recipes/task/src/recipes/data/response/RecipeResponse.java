package recipes.data.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class RecipeResponse {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NotBlank
    private LocalDateTime date;

    @Size(min = 1)
    private List<String> ingredients;

    @Size(min = 1)
    private List<String> directions;
}
