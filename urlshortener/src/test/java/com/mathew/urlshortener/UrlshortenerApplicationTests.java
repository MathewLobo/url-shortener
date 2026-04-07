package com.mathew.urlshortener;

import com.mathew.urlshortener.dto.ShortenRequest;
import com.mathew.urlshortener.repository.UrlRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlshortenerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UrlRepository urlRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		urlRepository.deleteAll();
	}

	@Test
	void shortenUrl_validUrl_returnsShortCode() throws Exception {
		ShortenRequest req = new ShortenRequest();
		req.setUrl("https://www.google.com");
		req.setExpiryDays(7);

		mockMvc.perform(post("/api/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.shortCode").isNotEmpty())
				.andExpect(jsonPath("$.originalUrl").value("https://www.google.com"));
	}

	@Test
	void shortenUrl_invalidUrl_returns400() throws Exception {
		ShortenRequest req = new ShortenRequest();
		req.setUrl("not-a-valid-url");

		mockMvc.perform(post("/api/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getStats_validCode_returnsStats() throws Exception {
		ShortenRequest req = new ShortenRequest();
		req.setUrl("https://www.github.com");
		req.setExpiryDays(7);

		String response = mockMvc.perform(post("/api/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
				.andReturn().getResponse().getContentAsString();

		String shortCode = objectMapper.readTree(response).get("shortCode").asText();

		mockMvc.perform(get("/api/stats/" + shortCode))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.clickCount").value(0));
	}

	@Test
	void getStats_invalidCode_returns404() throws Exception {
		mockMvc.perform(get("/api/stats/invalidcode"))
				.andExpect(status().isNotFound());
	}
}