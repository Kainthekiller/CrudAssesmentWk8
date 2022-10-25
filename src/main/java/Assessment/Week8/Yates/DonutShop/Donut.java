package Assessment.Week8.Yates.DonutShop;

//<editor-fold desc="TODO_LIST">
//TODO    Create	POST	/donuts	"create" route	Creates a donut
//TODO    Read	GET	/donuts/{id}	"show" route	Responds with a single donut
//TODO    Update	PATCH	/donuts/{id}	"update" route	Updates attributes of the donut
//TODO    Delete	DELETE	/donuts/{id}	"delete" route	Deletes the donut
//TODO   List	GET	/donuts	"index" or "list" route	Responds with a list of donuts

//TODO        Long	id
//TODO       String	name
//TODO       String	topping
//TODO       Date	expiration


//</editor-fold>


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Donut
{
    //<editor-fold desc="Variables">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto Generate
    Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Name")
    String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Topping")
    String topping;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("Expiration")
    LocalDate expiration;

    //</editor-fold>


    //<editor-fold desc="Getters & Setters">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }


    //</editor-fold>

}
