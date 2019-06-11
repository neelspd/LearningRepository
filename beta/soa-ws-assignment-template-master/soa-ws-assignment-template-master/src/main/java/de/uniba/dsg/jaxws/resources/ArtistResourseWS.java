package de.uniba.dsg.jaxws.resources;

import com.google.gson.Gson;
import de.uniba.dsg.interfaces.ArtistApi;
import de.uniba.dsg.jaxws.JaxWsServer;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Interpret;
import de.uniba.dsg.models.Song;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static de.uniba.dsg.jaxws.MusicApiImpl.restServerUri;



public class ArtistResourseWS implements ArtistApi {
    private static final Logger LOGGER = Logger.getLogger(ArtistResourseWS.class.getName());

    @Override
    public Interpret getArtist(String artistId) {
        //check if REST Server is available
        boolean status = JaxWsServer.isPortInUse(restServerUri);
        if (status == false) {
            throw new MusicRecommenderFault("REST Server unavailable", "Server unavailable");
        }

        Client client = ClientBuilder.newClient();
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/artists").path(artistId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        try {
            if (artistId == null || artistId.isEmpty()) {
                String cause = response.readEntity(ErrorMessage.class).getMessage();
            }
        } catch (Exception e) {
            throw new MusicRecommenderFault("Artist name required", "Empty or invalid paramter");

        }
        System.out.println("Response code : " + response.getStatus());

        if (response.getStatus() == 200) {
            Interpret artist = response.readEntity(Interpret.class);
            return artist;
        } else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("A client side error occurred ! Check artist id!", cause);
        } else if (response.getStatus() == 404) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("The requested resource was not found ", cause);
        } else if (response.getStatus() == 500) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An internal server error occurred", cause);
        } else {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
        }

    }

    @Override
    public List<Song> getTopTracks(String artistId) {
        //checking if REST Server is available
        boolean status = JaxWsServer.isPortInUse(restServerUri);
        if (status == false) {
            throw new MusicRecommenderFault("REST Server unavailable", "Server unavailable");
        }

        Client client = ClientBuilder.newClient();
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/artists").path(artistId).path("/top-tracks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (artistId == null || artistId.isEmpty()) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("Artist id missing!", cause);

        }
        if (response.getStatus() == 200) {
            String jsonString = (response.readEntity(String.class));
            Gson gson = new Gson();
            Song[] songsArray = gson.fromJson(jsonString, Song[].class);
            int i;
            int size = songsArray.length;
            List<Song> songList = new ArrayList<>();

            for (i = 0; i < size; i++) {
                Song song = new Song();
                song = songsArray[i];
                songList.add(song);

            }
            return songList;

        } else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("A client side error occurred, Please check the artist id!", cause);
        } else if (response.getStatus() == 404) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("The requested resource was not found", cause);
        } else if (response.getStatus() == 500) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An internal server error occurred", cause);
        } else {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
        }

    }

    @Override
    public Interpret getSimilarArtist(String artistId) {
        //checking if REST Server is available
        boolean status = JaxWsServer.isPortInUse(restServerUri);
        if (status == false) {
            throw new MusicRecommenderFault("REST Server unavailable", "Server unavailable");
        }

        Client client = ClientBuilder.newClient();
        if (artistId == null || artistId.isEmpty()) {

            throw new MusicRecommenderFault("Artist id missing!", "Missing parameter");
        }
        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/artists/").path(artistId).path("/similar-artist")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        System.out.println("Response code:" + response.getStatus());

        if (response.getStatus() == 200) {
            Interpret artist = response.readEntity(Interpret.class);
            System.out.println(artist);
            return artist;

        } else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("A client side error occurred,please check the artist id!", cause);
        } else if (response.getStatus() == 404) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("The requested resource was not found", cause);
        } else if (response.getStatus() == 500) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An internal server error occurred", cause);
        } else {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("An unknown remote server error occurred", cause);
        }
    }

}
