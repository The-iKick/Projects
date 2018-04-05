import com.google.gson.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;

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
		query = "https://itunes.apple.com/search?limit="+numberOf+"&entity=album&term=" + URLEncoder.encode(query);
		
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
	static ImageData[] generateURLS (String query, int numberOf) {
		ImageData[] ImageData = new ImageData[numberOf];
		try {
			JsonElement json = parseQuery(query, numberOf);

			
			JsonObject root = json.getAsJsonObject();                      // root of response
			JsonArray results = root.getAsJsonArray("results");          // "results" array
			int numResults = results.size();                             // "results" array size
			
			for (int i = 0; i < numResults; i++) {                       
			    JsonObject result = results.get(i).getAsJsonObject();    // object i in array
			    String artworkUrl = result.get("artworkUrl100").getAsString(); // artworkUrl100 member
			    String artist = result.get("artistName").getAsString(); // artist
			    String album = result.get("collectionName").getAsString(); // album
			    String genre = result.get("primaryGenreName").getAsString(); // genre
			    String trackLen = result.get("trackCount").getAsString(); // track count
			    
			    if (artworkUrl  != null) {                             // member might not exist
			    	ImageData[i] = new ImageData(artworkUrl , artist, album, genre, trackLen);                       // print the string
			    } // if
			} // for
			if (numResults < 20) { 
				return new ImageData[numResults];
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		ImageData[] unique = new ImageData[numberOf];
		
		// filters out all of the duplicate entries
		outer: for (int i = 0; i < numberOf; i++) {
			if (ImageData[i] == null) continue;
			
			String current = ImageData[i].getUrl();
			for (int j = 0; j < numberOf; j++) {
				if (unique[j] != null && current.equals(unique[j].getUrl())) {
					continue outer;
				}
			}
			unique[i] = ImageData[i];
		}
		// because we have null spots in the unique array, we need to filter those out
		return Arrays.stream(unique).filter(p -> p != null).toArray(size -> new ImageData[size]);
	}
}
