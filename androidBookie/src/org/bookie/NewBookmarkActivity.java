package org.bookie;

import org.bookie.R.id;
import org.bookie.model.BookMark;
import org.bookie.service.BookieService;
import org.bookie.service.NewBookmarkRequest.RequestSuccessListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewBookmarkActivity extends Activity {
	private static final int RESULTS_MESSAGE_DURATION = Toast.LENGTH_SHORT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("NewBookemarkActivity", "A new NewBookmarkActivity is created");
		setContentView(R.layout.new_bookmark);

	    Intent intent = getIntent();
	    String action = intent.getAction();
	    String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
	        if ("text/plain".equals(type)) {
	            handleSendText(intent); // Handle text being sent
	        }
		}

		setUpSaveButton();
	}

	private void setUpSaveButton() {
		Button save = (Button) findViewById(id.newBookmarkSaveButton);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("NewBookmarkActiviy","Save Button Pressed");
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NewBookmarkActivity.this);
				String user = prefs.getString("username", ""); // TODO default
				String apiKey = prefs.getString("apikey", ""); // TODO constants
				BookMark bmark = new BookMark();
				bmark.url = ((EditText) findViewById(id.newBookmarkUrlField)).getText().toString();
				bmark.description = ((EditText) findViewById(id.newBookmarkTitleField)).getText().toString();
				BookieService.getService().saveBookmark(user,apiKey,bmark,NewBookmarkActivity.this.produceListenerForRequest());
			}


		});

	}

	private RequestSuccessListener produceListenerForRequest() {
		return new RequestSuccessListener() {

			@Override
			public void notify(Boolean requestWasSuccessful) {
				if(requestWasSuccessful) {
					NewBookmarkActivity.this.requestFinishedWithSuccess();
				} else {
					NewBookmarkActivity.this.requestFinishedWithFailure();
				}
			}
		};
	}

	protected void requestFinishedWithFailure() {
		showMessageAboutResultsOfSave("Failed to update!");
	}

	protected void requestFinishedWithSuccess() {
		showMessageAboutResultsOfSave("Saved OK");
		dismissThisActivity(RESULTS_MESSAGE_DURATION);
	}

	private void showMessageAboutResultsOfSave(CharSequence message) {
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, message, RESULTS_MESSAGE_DURATION);
		toast.show();
	}

	private void handleSendText(Intent intent) {
		String url = intent.getStringExtra(Intent.EXTRA_TEXT);
		EditText uriField = (EditText) findViewById(id.newBookmarkUrlField);
		uriField.setText(url);
	}

	private void dismissThisActivity(int millisecDelay) {
		if(millisecDelay>0) {
			Handler handler = new Handler();
			final Runnable dismissLater = new Runnable() {
				public void run() {
					final NewBookmarkActivity thisActivity = NewBookmarkActivity.this;
					thisActivity.finish();
				}
			};
			handler.postDelayed(dismissLater, millisecDelay);
		} else {
			finish();
		}
	}
}