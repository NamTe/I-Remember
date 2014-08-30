package fu.agile.iremember;


public class Card {
	private int id;
	private String title;
	private String body;
	private String audio;
	private String image;
	private String video;
	private String storyTime;
	private String latitute;
	private String longitude;
	public Card(String mTitle, String mBody, String audioPath, String imagePath, String videoPath, String storyTime,String latitute,String longitude) {
		title = mTitle == null? "Unkown" : mTitle;
		body = mBody == null? "Unkown" : mBody;
		audio = audioPath == null? "Unkown" : audioPath;
		image = imagePath == null? "Unkown" : imagePath;
		video = videoPath == null? "Unkown" : videoPath;
		this.storyTime = storyTime == null? "Unkown" : storyTime;
		this.latitute = latitute;
		this.longitude = longitude;
	}
	
	
	public Card(String title, String body, String storyTime) {
		this.title = title;
		this.body = body;
		this.storyTime = storyTime;
	}
	
	public Card() {
		title = "Unknow";
		body = "Unknow";
		audio = null;
		image = null;
		video = null;
		this.storyTime = "0/0/0";
	}
	
	public String getLongitude() { return longitude;}	
	public String getLatitute() { return latitute;}
	public String getTitle() { return title;}
	public String getBody() { return body;}
	public String getTime() { return storyTime;}
	public String getAudioFile() { return audio;}
	public String getImageFile() { return image;}
	public String getVideoFile() { return video;}
	public int getID() { return id;}
	
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public void setLatitute(String latitute) {
		this.latitute = latitute;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void setStoryTime(String storyTime){
		this.storyTime = storyTime;
	}
	
	public void setAudioFile(String audioPath) {
			audio = audioPath;
	}
	
	public void setImageFile(String imagePath) {
			image = imagePath;
	}
	
	public void setVideoFile(String videoPath) {
		video = videoPath;
	}
	
	public void setID(int id) {
		this.id =id;
	}
	
	
	@Override
	public boolean equals(Object o) {
		Card obj = (Card)o;
		return obj.getID() == this.getID();
	}
	
	
}
