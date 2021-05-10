package course.examples.Services.KeyService;

import android.os.Parcel;
import android.os.Parcelable;

/*
*  the title of the song,
* (2) the name of artist or band who playsthe song,
* (3) a picture (bitmap) associated with the song, and
* (4) a string denoting the URL of a web sitecontaining an audio file for the song.*/
public class SongInfo implements Parcelable {
    int n;
    public String title;
    public String artist;
    public int pictureID;
    int URL;
    public SongInfo(int n, String t, String a, int p, int u){
        this.n = n;
        title = t;
        artist = a;
        pictureID = p;
        URL = u;
    }

    protected SongInfo(Parcel in) {
        n = in.readInt();
        title = in.readString();
        artist = in.readString();
        pictureID = in.readInt();
        URL = in.readInt();
    }

    public static final Creator<SongInfo> CREATOR = new Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(n);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeInt(pictureID);
        dest.writeInt(URL);
    }
}
