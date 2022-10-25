package Assessment.Week8.Yates;

import Assessment.Week8.Yates.DonutShop.Donut;
import Assessment.Week8.Yates.DonutShop.DonutRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DonutTest {

	//<editor-fold desc="Variables">
	@Autowired
	DonutRepo donutRepo;

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

	@Autowired
	MockMvc mvc;

	Donut donutOne;
	Donut donutTwo;
	Donut donutThree;
	//</editor-fold>

	//<editor-fold desc="Before Each">
	@BeforeEach
	void init()
	{
		donutOne = new Donut();
		donutTwo = new Donut();
		donutThree = new Donut();

		//ONE
		donutOne.setName("Nick");
		donutOne.setTopping("Sprinkles");
		donutOne.setExpiration(LocalDate.of(2022,8,26));
		//TWO
		donutTwo.setName("Anthony");
		donutTwo.setTopping("Icing");
		donutTwo.setExpiration(LocalDate.of(2022,9,27));
		//THREE
		donutThree.setName("Kenny");
		donutThree.setTopping("Oreo");
		donutThree.setExpiration(LocalDate.of(2022,10,28));
		donutRepo.save(donutOne);
		donutRepo.save(donutTwo);
		donutRepo.save(donutThree);
	}


	//</editor-fold>



	//<editor-fold desc="All Test">


	//<editor-fold desc="Post Test">
	@Test
	@Transactional
	@Rollback
	void postADonutTest() throws Exception {
		donutOne.setName("NewPostOfNicky");
		donutTwo.setName("NewPostOfAnthony");
		String json = mapper.writeValueAsString(donutOne);
		String jsonTwo = mapper.writeValueAsString(donutTwo);
		MockHttpServletRequestBuilder request = post("/Donut")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		MockHttpServletRequestBuilder requestTwo = post("/Donut")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonTwo);
		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.Name").value("NewPostOfNicky"))
				.andExpect(jsonPath("$.Topping").value("Sprinkles"))
				.andExpect(jsonPath("$.Expiration").value("2022-08-26"));
		this.mvc.perform(requestTwo)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.Name").value("NewPostOfAnthony"))
				.andExpect(jsonPath("$.Topping").value("Icing"))
				.andExpect(jsonPath("$.Expiration").value("2022-09-27"));
	}
	//</editor-fold>

	//<editor-fold desc="Get A Single Donut">
	@Test
	@Transactional
	@Rollback
	void getSingleDonut() throws Exception
	{

		MockHttpServletRequestBuilder request = get(String.format("/Donut/%d",donutOne.getId()));
		MockHttpServletRequestBuilder requestNotFound = get(String.format("/Donut/%d", this.donutRepo.count() + 1));
		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.Name").value("Nick"))
				.andExpect(jsonPath("$.Topping").value("Sprinkles"))
				.andExpect(jsonPath("$.Expiration").value("2022-08-26"));
		this.mvc.perform(requestNotFound)
				.andExpect(status().isNotFound());
	}


	//</editor-fold>

	//<editor-fold desc="Patch Donut">
	@Test
	@Transactional
	@Rollback
	void patchADonut() throws Exception
	{
		donutOne.setName("UpdatedNickName");
		donutTwo.setTopping("UpdatedToppingCandyCane");
		donutThree.setExpiration(LocalDate.of(1988,12,6));
		String jsonUpdateName = mapper.writeValueAsString(donutOne);
		String jsonUpdateTopping = mapper.writeValueAsString(donutTwo);
		String jsonUpdateExperation = mapper.writeValueAsString(donutThree);
		MockHttpServletRequestBuilder requestUpdateName = patch(String.format("/Donut/%d",donutOne.getId()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUpdateName);
		MockHttpServletRequestBuilder requestUpdateTopping = patch(String.format("/Donut/%d",donutTwo.getId()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUpdateTopping);
		MockHttpServletRequestBuilder requestUpdateExpirationDate = patch(String.format("/Donut/%d",donutThree.getId()))
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUpdateExperation);


		this.mvc.perform(requestUpdateName)
				.andExpect(status().isIAmATeapot())
				.andExpect(jsonPath("$.Name").value("UpdatedNickName"))
				.andExpect(jsonPath("$.Topping").value("Sprinkles"));
		this.mvc.perform(requestUpdateTopping)
				.andExpect(status().isIAmATeapot())
				.andExpect(jsonPath("$.Topping").value("UpdatedToppingCandyCane"))
				.andExpect(jsonPath("$.Name").value("Anthony"));
		this.mvc.perform(requestUpdateExpirationDate)
				.andExpect(status().isIAmATeapot())
				.andExpect(jsonPath("$.Expiration").value("1988-12-06"))
				.andExpect(jsonPath("$.Name").value("Kenny"));

	}

	//</editor-fold>

	//<editor-fold desc="Delete A Donut">
	@Test
	@Transactional
	@Rollback
	void deleteADonut() throws Exception
	{
		MockHttpServletRequestBuilder requestNotFound = delete(String.format("/Donut/%d",this.donutRepo.count() + 1));
		MockHttpServletRequestBuilder request = delete(String.format("/Donut/%d",donutOne.getId()));

		this.mvc.perform(requestNotFound)
				.andExpect(status().isNotFound());

		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.Name").doesNotExist())
				.andExpect(jsonPath("$.Topping").doesNotExist())
				.andExpect(jsonPath("$.Expiration").doesNotExist());
	}




	//</editor-fold>

	//<editor-fold desc="Get All Donut">
	@Test
	@Transactional
	@Rollback
	void getAllDounts() throws Exception
	{
		MockHttpServletRequestBuilder request = get("/Donut");

		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].Name").value("Nick"))
				.andExpect(jsonPath("$[0].Topping").value("Sprinkles"))
				.andExpect(jsonPath("$[0].Expiration").value("2022-08-26"))
				.andExpect(jsonPath("$[1].Name").value("Anthony"))
				.andExpect(jsonPath("$[1].Topping").value("Icing"))
				.andExpect(jsonPath("$[1].Expiration").value("2022-09-27"))
				.andExpect(jsonPath("$[2].Name").value("Kenny"))
				.andExpect(jsonPath("$[2].Topping").value("Oreo"))
				.andExpect(jsonPath("$[2].Expiration").value("2022-10-28"));

	}

	//</editor-fold>


	//</editor-fold>
}
