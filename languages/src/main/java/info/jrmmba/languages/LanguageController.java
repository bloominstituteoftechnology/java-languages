package info.jrmmba.languages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
// @RestController
public class LanguageController
{
    private final LanguageRepository langrepos;
    private final RabbitTemplate rt;

    public LanguageController(LanguageRepository langrepos, RabbitTemplate rt)
    {
        this.langrepos = langrepos;
        this.rt = rt;
    }

    @GetMapping("/languages")
    public List<Language> all()
    {
        return langrepos.findAll();
    }

    @GetMapping("/languages/{id}")
    public Language findOne(@PathVariable Long id)
    {
        return langrepos.findById(id)
                .orElseThrow(() -> new LanguageNotFoundException(id));
    }

    @PostMapping("/languages")
    public List<Language> newLanguage(@RequestBody List<Language> newLangs)
    {
        return langrepos.saveAll(newLangs);
    }

    @GetMapping("/languages/population")
    public ObjectNode sumPops()
    {
        // high resources
        // population does not change often
        // cache
        List<Language> languages = langrepos.findAll();

        Long total = 0L;
        for (Language l : languages)
        {
            total = total + l.getPopulation();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode totalPops = mapper.createObjectNode();
        totalPops.put("id", 0);
        totalPops.put("language", "total");
        totalPops.put("population", total);
        totalPops.put("comment", "Lambda School");

        LanguageLog message = new LanguageLog("Checked Total Population");
        rt.convertAndSend(WorldlangsApplication.QUEUE_NAME, message.toString());
        log.info("Message sent");
        return totalPops;
    }
}
