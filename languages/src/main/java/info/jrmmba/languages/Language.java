package info.jrmmba.languages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Language
{
    private @Id @GeneratedValue Long id;
    private String language;
    private Long population;

    public Language()
    {
        // default constructor
    }

    public Language (String language, Long population)
    {
        this.language = language;
        this.population = population;
    }
}
