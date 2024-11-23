package retraite.simulateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class ControleurRetraite {

    @Autowired
    private ServiceRetraiteGeneral serviceRetraite;

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/simuler")
    @ResponseBody
    public ResultatRetraite simulerRetraite(@RequestBody Adherent adherent) {
        return serviceRetraite.calculerEpargneRetraite(adherent);
    }

}
