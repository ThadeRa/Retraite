package retraite.simulateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class ControleurRetraite {

    @Autowired
    private ServiceRetraite serviceRetraite;

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/simuler")
    public double simulerRetraite(@RequestBody Adherent adherent) {
        return serviceRetraite.calculerEpargneRetraite(adherent);
    }

    @PostMapping("/example")
    public ResponseEntity<String> handlePost() {
        return ResponseEntity.ok("POST accepted");
    }

    @RequestMapping(value = "/example", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> handleRequest() {
        return ResponseEntity.ok("Request accepted");
    }

    
}
