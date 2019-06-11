package de.uniba.dsg.jaxrs.resources;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;

import de.uniba.dsg.CustomSpotifyApi;
import de.uniba.dsg.interfaces.SearchApi;
import de.uniba.dsg.jaxrs.exceptions.ClientRequestException;
import de.uniba.dsg.jaxrs.exceptions.RemoteApiException;
import de.uniba.dsg.jaxrs.exceptions.ResourceNotFoundException;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;

@Path("search")
public class SearchResource implements SearchApi {

	@Override
	@GET
	public Interpret searchArtist(@QueryParam("artist") String artistName) {
		if (artistName == null) {
			throw new ClientRequestException(new ErrorMessage("Required query parameter is missing: artist"));
		}
		
		SearchArtistsRequest artistRequest = CustomSpotifyApi.getInstance().searchArtists(artistName).limit(1).build();

		try {
			// get search results
			Paging<Artist> artistSearchResult = artistRequest.execute();
			Artist[] artists = artistSearchResult.getItems();

			// no artist found
			if (artists != null && artists.length == 0) {
				throw new ResourceNotFoundException(new ErrorMessage(String.format("No matching artist found for query: %s", artistName)));
			}

			Artist artist = artists[0];
			Interpret result = new Interpret();
			result.setId(artist.getId());
			result.setName(artist.getName());
			result.setPopularity(artist.getPopularity());
			result.setGenres(Arrays.asList(artist.getGenres()));

			return result;
		} catch (SpotifyWebApiException | IOException e) {
			throw new RemoteApiException(new ErrorMessage(e.getMessage()));
		}
	}
}
