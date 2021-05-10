package course.examples.Services.KeyCommon;
import android.graphics.Bitmap;
    interface KeyGenerator {
       String getSong(int n);
       String getURL(int n);
       String getArtist(int n);
       Bitmap getPicture(int n);
       String[] getAllTitles();
       String[] getAllArtists();
       String[] getAllURL();
       Bitmap[] getAllPictures();
    }