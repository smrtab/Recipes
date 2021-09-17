package recipes.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Direction> directions = new ArrayList<>();

    public List<String> getIngredientsDescriptions() {
        List<String> list = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            list.add(ingredient.getDescription());
        }
        return list;
    }

    public List<String> getDirectionsDescriptions() {
        List<String> list = new ArrayList<>();
        for (Direction direction : directions) {
            list.add(direction.getDescription());
        }
        return list;
    }

    @Override
    public String toString() {
        return "Recipe(id=" + getId() + ", user_id=" + getUser().getId() + ", date=" + getDate() + ", name=" + getName() + ", description=" + getDescription() + ", category=" + getCategory() + ")";
    }
}
