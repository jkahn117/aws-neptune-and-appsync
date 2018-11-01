package neptunebridge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.SigV4WebSocketChannelizer;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<AppSyncRequest, Object> {
    // Neptune Cluster connection info, passed as Lambda environment variables
    private static String NEPTUNE_ENDPOINT = System.getenv("NEPTUNE_ENDPOINT");
    private static String NEPTUNE_PORT = System.getenv("NEPTUNE_PORT");

    // Connection to Neptune
    private static Cluster.Builder builder;
    private static Cluster cluster;
    private static Client client;

    static {
        builder = Cluster.build();
        builder.addContactPoint(NEPTUNE_ENDPOINT);
        builder.port(Integer.parseInt(NEPTUNE_PORT));
        // if Neptune uses IAM auth, use the following channelizer; else omit
        builder.channelizer(SigV4WebSocketChannelizer.class);
        
        cluster = builder.create();
        client = cluster.connect();
    }

    public Object handleRequest(final AppSyncRequest input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            System.out.println("before query");
            final ResultSet rs = client.submit(input.getQuery());
            System.out.println("after query");

            for (Result r : rs) {
                System.out.println(r);
            }
            
            
            return new GatewayResponse("{}", headers, 200);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return new GatewayResponse("{}", headers, 500);
        }
    }
}
