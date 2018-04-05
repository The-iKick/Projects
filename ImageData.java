
public class ImageData {
	private String url;
	private String artist;
	private String album;
	private String genre;
	private String trackLength;
	
	ImageData (String url, String artist, String album, String genre, String trackLength) {
		this.url = url;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.trackLength = trackLength;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum () {
		return album;
	}
	
	public String getGenre () {
		return genre;
	}
	
	public String getTrackLength() {
		return trackLength;
	}
}
