package dev.abelab.crms.api.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.flywaydb.core.Flyway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import dev.abelab.crms.annotation.IntegrationTest;
import dev.abelab.crms.util.ConvertUtil;


/**
 * Abstract Rest Controller Integration Test
 */
@SpringBootTest
@IntegrationTest
public abstract class AbstractRestController_IT {

	static final int SAMPLE_INT = 1;
	static final String SAMPLE_STR = "SAMPLE";

	/**
	 * The Mock MVC
	 */
	MockMvc mockMvc;

	/**
	 * The Web Application Context.
	 */
	@Autowired
	WebApplicationContext webApplicationContext;

	/**
	 * DB Migration
	 */
	@Autowired
	Flyway flyway;

	@BeforeEach
	void setup() {
		// init DB
		this.flyway.clean();
		this.flyway.migrate();

		// set Mock MVC
		this.mockMvc = MockMvcBuilders //
			.webAppContextSetup(this.webApplicationContext) //
			.build();
	}

	/**
	 * GET request
	 *
	 * @param path path
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder getRequest(final String path) {
		return MockMvcRequestBuilders.get(path);
	}

	/**
	 * POST request
	 *
	 * @param path path
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder postRequest(final String path) {
		return MockMvcRequestBuilders.post(path);
	}

	/**
	 * POST request (Form)
	 *
	 * @param path   path
	 *
	 * @param params query params
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder postRequest(final String path, final MultiValueMap<String, String> params) {
		return MockMvcRequestBuilders.post(path) //
			.contentType(MediaType.APPLICATION_FORM_URLENCODED) //
			.params(params);
	}

	/**
	 * POST request (JSON)
	 *
	 * @param path    path
	 *
	 * @param content request body
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder postRequest(final String path, final Object content) {
		return MockMvcRequestBuilders.post(path) //
			.contentType(MediaType.APPLICATION_JSON) //
			.content(ConvertUtil.convertObjectToJson(content));
	}

	/**
	 * PUT request (JSON)
	 *
	 * @param path    path
	 *
	 * @param content request body
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder putRequest(final String path, final Object content) {
		return MockMvcRequestBuilders.put(path) //
			.contentType(MediaType.APPLICATION_JSON) //
			.content(ConvertUtil.convertObjectToJson(content));
	}

	/**
	 * DELETE request
	 *
	 * @param path path
	 *
	 * @return HTTP request builder
	 */
	public MockHttpServletRequestBuilder deleteRequest(final String path) {
		return MockMvcRequestBuilders.delete(path);
	}

	/**
	 * Execute request
	 *
	 * @param request HTTP request builder
	 *
	 * @param status  expected HTTP status
	 *
	 * @return MVC result
	 *
	 * @throws Exception exception
	 */
	public MvcResult execute(final MockHttpServletRequestBuilder request, final HttpStatus status) throws Exception {
		final var result = mockMvc.perform(request).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(status.value());

		return result;
	}

	/**
	 * Execute request / return response
	 *
	 * @param request HTTP request builder
	 *
	 * @param status  expected HTTP status
	 *
	 * @param clazz   response class
	 *
	 * @return response
	 *
	 * @throws Exception exception
	 */
	public <T> T execute(final MockHttpServletRequestBuilder request, final HttpStatus status, final Class<T> clazz) throws Exception {
		final var result = mockMvc.perform(request).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(status.value());

		return ConvertUtil.convertJsonToObject(result.getResponse().getContentAsString(), clazz);
	}

}
