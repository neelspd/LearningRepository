package de.uniba.dsg.jaxws;

import java.io.IOException;
import java.util.Properties;

import javax.xml.ws.Endpoint;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import de.uniba.dsg.Configuration;

public class JaxWsServer {
    private static Properties properties = Configuration.loadProperties();

    public static void main(String[] args) throws IOException, SpotifyWebApiException {
        String serverUri = properties.getProperty("soapServerUri");

        Endpoint endpoint = Endpoint.create(new MusicApiImpl());
        endpoint.publish(serverUri);
        System.out.println("Server ready to serve your JAX-WS requests...");
    }
}
