package main.java.us.bmark.android;

import android.os.Bundle;
import android.widget.Toast;

public class AllBookmarkListFragment extends BookmarkListFragment {

    @Override
    void refresh() {
    	Toast.makeText(getActivity(),R.string.fetch_all_bm, 
                Toast.LENGTH_SHORT).show();
        int nextPage = pagesLoaded;
        refreshState.setStateInProgress();
        service.everyonesRecent(countPP, nextPage, new ServiceCallback());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }
}
