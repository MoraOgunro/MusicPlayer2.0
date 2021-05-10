package course.examples.Services.KeyService;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import course.examples.Services.KeyCommon.KeyGenerator;




public class KeyGeneratorImpl extends Service {
	private static String[] songTitleList;
	private static String[] artistList;
	static Bitmap[] pictureList;
	private static String[] URLList;

	/*
	This function is a helper function that fills the arrays with default data
	These arrays will be used in the AIDL to send to the client
	 */
	public void populateSongInfoArrayList() {
		songTitleList = new String[]{"Giorno","Melancholia","Ring of Iron","Coral", "City Nights"};
		artistList = new String[]{"Jojo's Bizarre Adventure","FSM Team","WombatNoisesAudio","LiQWYD","Roa Music"};
		URLList = new String[]{"https://www.soundboard.com/handler/DownLoadTrack.ashx?cliptitle=giorno+theme&filename=25/251239-bbec0847-fe56-46db-956c-3d5f9c525696.mp3",
								"https://www.free-stock-music.com/music/fsm-team-escp-yellowtree-melancholia-goth-emo-type-beat.mp3",
								"https://www.free-stock-music.com/music/wombat-noises-audio-ring-of-iron.mp3",
								"https://www.free-stock-music.com/music/liqwyd-coral.mp3",
								"https://www.free-stock-music.com/music/roa-music-city-nights.mp3"};
		pictureList = new Bitmap[]{
				BitmapFactory.decodeResource(getResources(), R.drawable.giorno),
				BitmapFactory.decodeResource(getResources(), R.drawable.mencholia),
				BitmapFactory.decodeResource(getResources(), R.drawable.ringofiron),
				BitmapFactory.decodeResource(getResources(), R.drawable.coral),
				BitmapFactory.decodeResource(getResources(), R.drawable.citynights)
				};
	}

	// Implement the Stub for this Object
	private final KeyGenerator.Stub mBinder = new KeyGenerator.Stub() {

		//These functions return information about a song a specified index
		public synchronized String getSong(int n){
			Log.i("getSong()","Sending song " + songTitleList[n-1]);
			return songTitleList[n-1];
		}
		public String getArtist(int n){
			return artistList[n-1];
		}
		public Bitmap getPicture(int n){
			return pictureList[n-1];
		}
		public synchronized String getURL(int n){
			Log.i("getURL()","Sending URL " + URLList[n-1]);
			return URLList[n-1];
		}
		//These functions return info for all songs according to the specified type
		public String[] getAllTitles(){
			return songTitleList;
		}
		public String[] getAllArtists(){
			return artistList;
		}
		public String[] getAllURL(){
			return URLList;
		}
		public Bitmap[] getAllPictures(){
			return pictureList;
		}

	};

	// Return the Stub defined above
	@Override
	public IBinder onBind(Intent intent) {
		populateSongInfoArrayList();
		return mBinder;
	}
}
