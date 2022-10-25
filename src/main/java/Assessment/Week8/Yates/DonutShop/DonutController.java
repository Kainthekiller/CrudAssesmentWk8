package Assessment.Week8.Yates.DonutShop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Donut")
public class DonutController
{

    //<editor-fold desc="Variables & Constructor">


    DonutRepo donutRepo;

    public DonutController(DonutRepo donutRepo) {
        this.donutRepo = donutRepo;
    }
//</editor-fold>


   //<editor-fold desc="Post New Donut">

    @PostMapping("")
    ResponseEntity<Donut> postADonut(@RequestBody Donut body)
    {
       return new ResponseEntity<>(this.donutRepo.save(body), HttpStatus.OK);
    }
    //</editor-fold>

    //<editor-fold desc="Get Single Donut">
    @GetMapping("{id}")
    ResponseEntity<Donut> getSingleDonut(@PathVariable Long id)
    {
        //Donut Exist
        if (this.donutRepo.findById(id).isPresent())
        {
            return new ResponseEntity<>(this.donutRepo.findById(id).get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //</editor-fold>

    //<editor-fold desc="Patch a Dount">
    @PatchMapping("{id}")
    ResponseEntity<Donut> patchADonut(@PathVariable Long id, @RequestBody Map<String, String> body)
    {
        //Is user present to be patched
        if (this.donutRepo.findById(id).isPresent())
        {
            Donut holder = this.donutRepo.findById(id).get();
            for(Map.Entry<String, String> entry : body.entrySet())
            {
               switch (entry.getKey())
               {
                   case "Name" -> holder.setName(entry.getValue());
                   case "Expiration" -> holder.setExpiration(LocalDate.parse(entry.getValue()));
                   case "Topping" -> holder.setTopping(entry.getValue());
               }
            }
            this.donutRepo.save(holder);
            return new ResponseEntity<>(this.donutRepo.findById(id).get(), HttpStatus.I_AM_A_TEAPOT);
        }
        //User Was Not Found
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //</editor-fold>

    //<editor-fold desc="Delete A Donut">
        @DeleteMapping("{id}")
        ResponseEntity<Donut> deleteADonut(@PathVariable Long id)
        {
            //Exist
            if (this.donutRepo.findById(id).isPresent())
            {
                this.donutRepo.deleteById(id);
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            //Dose Not Exist
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    //</editor-fold>

    //<editor-fold desc="Get List Of Donuts">
    @GetMapping("")
    List<Donut> getAllDonuts()
    {
       return this.donutRepo.findAll();
    }

    //</editor-fold>

}
