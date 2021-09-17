package recipes.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
