import com.google.gson.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Represents a class that parses the JSON of the iTunes Search API
 * 
 * @author Shawn Holman
 * @version 0.1.0
 */
public class JSONParser {
	/**
	 * Reads the iTunes Search API and parses its JSON
	 * 
	 * @param query search term
	 * @param numberOf the number of images to return
	 * @return parsed json
	 * @throws IOException
	 */
	static JsonElement parseQuery (String query, int numberOf) throws IOException {
		query = "https://itunes.apple.com/search?limit="+numberOf+"&term=" + URLEncoder.encode(query);
		
		URL url = new URL(query);
		
		InputStreamReader reader = new InputStreamReader(url.openStream());
		JsonParser jp = new JsonParser();

		return jp.parse(reader);
	}
	
	/**
	 * Generates images based on a search query 
	 * 
	 * @param query search term
	 * @param numberOf the number of images to return
	 * @return
	 */
	static String[] generateURLS (String query, int numberOf) {
		String[] URLS = new String[numberOf];
		try {
			JsonElement json = parseQuery(query, numberOf);

			
			JsonObject root = json.getAsJsonObject();                      // root of response
			JsonArray results = root.getAsJsonArray("results");          // "results" array
			int numResults = results.size();                             // "results" array size
			
			for (int i = 0; i < numResults; i++) {                       
			    JsonObject result = results.get(i).getAsJsonObject();    // object i in array
			    JsonElement artworkUrl100 = result.get("artworkUrl100"); // artworkUrl100 member
			    if (artworkUrl100 != null) {                             // member might not exist
			         String artUrl = artworkUrl100.getAsString();        // get member as string
			         URLS[i] = artUrl;                       // print the string
			    } // if
			} // for
			if (numResults < 20) { 
				return new String[numResults];
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return Arrays.stream(URLS).distinct().toArray(String[]::new);
	}
}
