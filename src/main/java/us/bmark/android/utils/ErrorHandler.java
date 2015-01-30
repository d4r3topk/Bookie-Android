package main.java.us.bmark.android.utils;

import retrofit.RetrofitError;

public interface ErrorHandler {
    public void handleError(RetrofitError error);
}
