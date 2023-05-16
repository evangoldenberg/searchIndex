package org.ezcampus.search.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ezcampus.search.core.SearchHandler;
import org.ezcampus.search.core.models.CourseDataResult;
import org.ezcampus.search.core.models.SearchQuery;
import org.ezcampus.search.hibernate.entity.Word;
import org.ezcampus.search.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/search")
public class Search
{
	private final ObjectMapper jsonMap = new ObjectMapper();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequestBody(String jsonPayload) {
		try {

			SearchQuery requestData = jsonMap.readValue(jsonPayload, SearchQuery.class);

			List<CourseDataResult> results = SearchHandler.searchFuzzy(
					requestData.getSearchTerm(), 
					requestData.getPage(), 
					requestData.getResultsPerPage());

			// Convert to JSON array string
			try {
				String jsonArray = jsonMap.writeValueAsString(results);

				return Response.status(Response.Status.OK).entity(jsonArray).build(); // Response with 200 OK and data
																						// (search results)

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			return Response.status(Response.Status.NOT_FOUND).entity("not found").build();
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON payload").build();
		}
	}
	


	@GET
	@Path("orm")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getRequestBody2(
			@QueryParam("search_term") String searchTerm, @QueryParam("page") int page,
			@QueryParam("results_per_page") int resultsPerPage
	)
	{

		Logger.debug("search function starting");

		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			Logger.debug("In session try statement");

			List<Word> existingWord = session.createQuery("FROM Word ", Word.class).getResultList();

			existingWord.forEach(x -> {
				Logger.info(x);
			});

			String a = String.join(", ", existingWord.stream().map(x -> x.getWordString()).toList());

			return Response.status(Response.Status.OK).entity(a).build();
		}

//		return Response.status(Response.Status.NOT_FOUND).entity("not found").build();

	}
}
