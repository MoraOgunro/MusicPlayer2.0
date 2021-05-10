package course.examples.Services.KeyClient;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.ImageView;
import android.widget.TextView;

public class songInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info);
        String title = getIntent().getStringExtra("title");
        String artist = getIntent().getStringExtra("artist");
        int number = getIntent().getIntExtra("number",1);
        try {
            ImageView imageView = findViewById(R.id.ImageView);
            Bitmap picture = KeyServiceUser.getBitmap(number);
            imageView.setImageBitmap(picture);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        TextView titleTextView = findViewById(R.id.Title);
        TextView artistTextView = findViewById(R.id.Artist);
        titleTextView.setText(title);
        artistTextView.setText(artist);


    }
}