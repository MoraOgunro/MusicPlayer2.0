package course.examples.Services.KeyClient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import course.examples.Services.KeyCommon.KeyGenerator;


public class KeyServiceUser extends Activity {

	protected static final String TAG = "KeyServiceUser";
	protected static final int PERMISSION_REQUEST = 0;
	private static KeyGenerator mKeyGeneratorService;
	private boolean mIsBound = false;
	private static String[] songTitleList;
	private static String[] artistList;
	static Bitmap[] pictureList;
	private static String[] URLList;
	RecyclerView albumView;
	MyGridAdapter gridAdapter;
	RVClickListener listener;
	static TextView numberTextView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		albumView = (RecyclerView) findViewById(R.id.recyclerView);
		listener = (view,position)->{

		};

		final TextView bindStatus = findViewById(R.id.bindingStatusView);
		numberTextView= findViewById(R.id.editTextNumber);
		TextView mainInfo = findViewById(R.id.mainInfoTextView);
		mainInfo.setVisibility(View.INVISIBLE);
		Button specificSongButton = (Button) findViewById(R.id.specificSongButton);
		specificSongButton.setEnabled(false);
		/*
		This button calls the bind function and updates the appropriate GUI elements
		 */
		Button bindButton = findViewById(R.id.bindButton);
		bindButton.setOnClickListener(v -> {
			if(!mIsBound) {
				try {
					checkBindingAndBind();

				} catch (RemoteException e) {
					e.printStackTrace();
				}
				String connected = "Service Connected.";
				if(mIsBound) {
					bindStatus.setText(connected);
					bindStatus.setTextColor(Color.rgb(48, 166, 66));
					mainInfo.setVisibility(View.VISIBLE);
					specificSongButton.setEnabled(true);
					albumView.setVisibility(View.VISIBLE);
				}
			}
		});
		/*
		This button calls the unbind function and updates the appropriate GUI elements
		 */
		Button unbindButton = findViewById(R.id.unbindButton);
		unbindButton.setOnClickListener(v -> {
				checkBindandUnbind();
				String disconnected = "Service Disconnected. Commands disabled.";
				if(!mIsBound) {
					bindStatus.setText(disconnected);
					bindStatus.setTextColor(Color.RED);
					mainInfo.setVisibility(View.INVISIBLE);
					specificSongButton.setEnabled(false);
					albumView.setVisibility(View.INVISIBLE);
				}

		});
		/*
		This button launches the second activity with extras containing the title, artist, and ID
		If the input is out of bounds, a default value of 1 is inserted
		 */
		specificSongButton.setOnClickListener(v->{
			Intent intent = new Intent(this, songInfoActivity.class);
			int num = Integer.parseInt(String.valueOf(numberTextView.getText()));
			if(num > 5 || num < 1){
				num = 1;
			}
			try {
				intent.putExtra("title",mKeyGeneratorService.getSong(num));
				intent.putExtra("artist",mKeyGeneratorService.getArtist(num));
				intent.putExtra("number",num);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			startActivity(intent);
		});


	}

	//A helper function needed for the second activity,
	//it retrieves the appropriate image of the song
	public static Bitmap getBitmap(int n) throws RemoteException {
		return mKeyGeneratorService.getPicture(n);
	}
	// Bind to KeyGenerator Service
	@Override
	protected void onStart() {
		super.onStart();

		if (checkSelfPermission("course.examples.Services.KeyService.GEN_ID")
			!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{"course.examples.Services.KeyService.GEN_ID"},
					PERMISSION_REQUEST);
		}
		else {
		}
	}

	protected void checkBindingAndBind() throws RemoteException {
		if (!mIsBound) {
			boolean b = false;
			Intent i = new Intent(KeyGenerator.class.getName());
			// UB:  Stoooopid Android API-21 no longer supports implicit intents
			// to bind to a service #@%^!@..&**!@
			// Must make intent explicit or lower target API level to 20.
			ResolveInfo info = getPackageManager().resolveService(i, 0);
			i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
			b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);

			if (b) {
				mIsBound = true;
				Log.i(TAG, "Ugo says bindService() succeeded!");
			} else {
				Log.i(TAG, "Ugo says bindService() failed!");
			}
		}
	}

	//Only unbind if the service is currently bound
	protected void checkBindandUnbind(){
		if(mIsBound){
			unbindService(this.mConnection);
			mIsBound = false;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST: {

				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				}
				else {
					Toast.makeText(this, "BUMMER: No Permission :-(", Toast.LENGTH_LONG).show() ;
				}
			}
			default: {
				// do nothing
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	//Disconnect when app is destroyed
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mIsBound) {
			unbindService(this.mConnection);
		}
	}

	/*
	When the client connects to the service, we want to fetch all of the available data
	 */
	private final ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder iservice) {

			mKeyGeneratorService = KeyGenerator.Stub.asInterface(iservice);

			mIsBound = true;
			try {
				songTitleList = mKeyGeneratorService.getAllTitles();
				artistList = mKeyGeneratorService.getAllArtists();
				pictureList = mKeyGeneratorService.getAllPictures();
				URLList = mKeyGeneratorService.getAllURL();
				//We can now initialize out grid adapter with this data
				gridAdapter = new MyGridAdapter(KeyServiceUser.this,songTitleList,artistList,pictureList,URLList,listener);
				albumView.setHasFixedSize(true);
				albumView.setAdapter(gridAdapter);
				albumView.setLayoutManager(new GridLayoutManager(KeyServiceUser.this,2));
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		}

		public void onServiceDisconnected(ComponentName className) {

			mKeyGeneratorService = null;
			mIsBound = false;

		}
	};

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
