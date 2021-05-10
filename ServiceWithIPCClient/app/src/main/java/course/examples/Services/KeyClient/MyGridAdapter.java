package course.examples.Services.KeyClient;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;


public class MyGridAdapter extends RecyclerView.Adapter<MyGridAdapter.ViewHolder> {
    protected Context context;
    protected String[] songTitleList;
    String[] artistList;
    Bitmap[] pictureList;
    String[] URLList;
    protected int PADDING = 40;
    private RVClickListener RVlistener;
    MediaPlayer mediaPlayer;
    boolean first;

    public MyGridAdapter(Context c, String[] songTitleList, String[] artistList, Bitmap[] pictureList, String[] URLList, RVClickListener listener){
        context = c;
        this.songTitleList = songTitleList;
        this.artistList = artistList;
        this.pictureList = pictureList;
        this.URLList = URLList;
        this.RVlistener = listener;
        Log.i("Grid constructor",songTitleList[0]);
        first = true;
    }
    public void clear(){
        songTitleList = new String[0];
        artistList= new String[0];
        pictureList = new Bitmap[0];
        URLList= new String[0];
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = inflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listView, RVlistener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText(songTitleList[i]);
        viewHolder.artist.setText(artistList[i]);
        viewHolder.image.setImageBitmap(pictureList[i]);
    }

    @Override
    public int getItemCount() {
        return songTitleList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        public TextView title;
        public ImageView image;
        public TextView artist;
        private RVClickListener listener;
        private View itemView;


        public ViewHolder(@NonNull View itemView, RVClickListener passedListener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.gridTitle);
            artist = (TextView) itemView.findViewById(R.id.gridArtist);
            image = (ImageView) itemView.findViewById(R.id.gridImageView);
            this.itemView = itemView;
            itemView.setOnCreateContextMenuListener(this); //set context menu for each list item (long click)
            this.listener = passedListener;
            /*
                don't forget to set the listener defined here to the view (list item) that was
                passed in to the constructor.
             */
            itemView.setOnClickListener(this); //set short click listener
        }

        @Override
        public void onClick(View v) {
            Log.i("ON_CLICK", "Here1");

            //Check if the media player needs to be stopped and reset. Otherwise
            // the music will overlap
            int pos = getAdapterPosition();
            if(first){
                first = false;
            }else{
                mediaPlayer.reset();
            }
            //Creating a new mediaPlayer object that fetches an mp3 from he given url
            String url = URLList[pos]; // your URL here
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}