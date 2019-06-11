package de.uniba.dsg.jaxws.resources;
// Using google GSON for java object conversion to JSON
import com.google.gson.Gson;
import de.uniba.dsg.interfaces.AlbumApi;
import de.uniba.dsg.jaxws.JaxWsServer;
import de.uniba.dsg.jaxws.MusicApiImpl;
import de.uniba.dsg.jaxws.exceptions.MusicRecommenderFault;
import de.uniba.dsg.models.ErrorMessage;
import de.uniba.dsg.models.Release;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static de.uniba.dsg.jaxws.MusicApiImpl.restServerUri;


public class AlbumResourceWS implements AlbumApi {
    private static final Logger LOGGER = Logger.getLogger(AlbumResourceWS.class.getName());
    @Override
    public List<Release> getNewReleases(String country, int size) {
        try {
            if (country.length() == 0 || country == null)
                //default country is set to Germany (DE)
                country = "DE";
        } catch (Exception e) {
            country = "DE";
        }
        if (size < 0) {
            throw new MusicRecommenderFault("Invalid size.Please provide a non negative number!", "Invalid size");
        }
        if (size > 50) {
            throw new MusicRecommenderFault("Invalid size .Please provide a limit less than 50!", "Invalid size");

        }
        //setting default size as described in the specifications
        if (size == 0)
            size = 10;
        //checking if REST Server is available
        boolean status = JaxWsServer.isPortInUse(restServerUri);
        if (status == false) {
            throw new MusicRecommenderFault("REST Server unavailable", "Server unavailable");
        }


        Client client = ClientBuilder.newClient();

        Response response = client.target(MusicApiImpl.restServerUri)
                .path("/albums").path("/new-releases").queryParam("country", country).queryParam("size", String.valueOf(size))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        System.out.println("Response : " + response.getStatus());

        if (response.getStatus() == 200) {
            String jsonString = (response.readEntity(String.class));
            Gson gson = new Gson();
            Release[] releaseArray = gson.fromJson(jsonString, Release[].class);
            int i;
            int arraySize = releaseArray.length;
            List<Release> releaseList = new ArrayList<>();

            for (i = 0; i < arraySize; i++) {
                Release release = new Release();
                release = releaseArray[i];
                releaseList.add(release);

            }
            return releaseList;

        } else if (response.getStatus() == 400) {
            String cause = response.readEntity(ErrorMessage.class).getMessage();
            throw new MusicRecommenderFault("Invalid country code! Please provide valid countries like DE,AT,IN...", cause);
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
