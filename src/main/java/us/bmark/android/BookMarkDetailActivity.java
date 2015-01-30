package main.java.us.bmark.android;


import main.java.us.bmark.android.R;
import main.java.us.bmark.android.prefs.SharedPrefsBackedUserSettings;
import main.java.us.bmark.android.utils.IntentConstants;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import bookieclient.Bookmark;
import bookieclient.Tag;

import static us.bmark.android.utils.Utils.equalButNotBlank;

public class BookMarkDetailActivity extends AbstractActivity {

    private static final String TAG = BookMarkDetailActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_detail);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);
        dealWithIntents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_book_mark_detail, menu);
        return true;
    }

    private void dealWithIntents() {
        Intent intent = getIntent();
        try {
            final String json = intent.getStringExtra(IntentConstants.EXTRAS_KEY_BMARK);
            Bookmark bmark = (new Gson()).fromJson(json, Bookmark.class);
            populateFields(bmark);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Error getting bookmark detail", e);
	    Toast.makeText(getActivity(),"Some error occured while fetching bookmark details" , 
                Toast.LENGTH_SHORT).show();
        }
    }

    private void populateFields(Bookmark bmark) {
        ((TextView) findViewById(R.id.bookmarkDetailTextviewDescription)).setText(bmark.description);
        ((TextView) findViewById(R.id.bookmarkDetailTextviewUrl)).setText(bmark.url);
        ((TextView) findViewById(R.id.bookmarkDetailTextviewUsername)).setText(bmark.username);
        ((TextView) findViewById(R.id.bookmarkDetailTextviewStored)).setText(bmark.stored);
        ((TextView) findViewById(R.id.bookmarkDetailTextviewTotalClicks)).setText(Integer.toString(bmark.total_clicks));
        final TextView myClicksTextView = (TextView) findViewById(R.id.bookmarkDetailTextviewClicks);
        if (isMyBookmark(bmark)) {
            myClicksTextView.setText(Integer.toString(bmark.clicks));
        } else {
            myClicksTextView.setVisibility(View.INVISIBLE);
            findViewById(R.id.bookMarkDetailTextviewCountLabel).setVisibility(View.INVISIBLE);
        }
        refreshTagsTable(bmark.tags);
    }

    private boolean isMyBookmark(Bookmark bmark) {
        return equalButNotBlank(bmark.username, userSettings().getUsername());
    }

    private void refreshTagsTable(Iterable<Tag> tags) {
        final TableLayout table = (TableLayout) findViewById(R.id.bookmarkDetailTagTable);
        table.removeAllViews();

        if(tags==null) return;

        for (Tag tag : tags) {
            final TableRow tagRow = createTagRow(tag.name);
            table.addView(tagRow);
        }
    }

    private TableRow createTagRow(CharSequence tagText) {
        TableRow rowView = new TableRow(this);
        TextView tagTextView = new TextView(this);
        tagTextView.setText(tagText);
        rowView.addView(tagTextView);
        return rowView;
    }

    private UserSettings userSettings() {
        return (UserSettings) new SharedPrefsBackedUserSettings(this);
    }

}
