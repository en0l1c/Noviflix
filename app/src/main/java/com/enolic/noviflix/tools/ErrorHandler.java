package com.enolic.noviflix.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ErrorHandler {


    public static void handleError(Response<?> response, Context context, String activityName) {
        int statusCode = response.code();
        String errorBody = "Unknown error";

        // Use try-with-resources for ResponseBody
        try (ResponseBody body = response.errorBody()) {
            if (body != null) {
                errorBody = body.string();
            }
        } catch (IOException e) {
            Log.e("ErrorHandler", "Failed to read error body: " + e.getMessage(), e);
        }

        String logMessage = activityName + " - Status Code: " + statusCode + ", Error: " + errorBody;

        // Manage status codes
        switch (statusCode) {
            case 400:
                Toast.makeText(context, "All the fiels are required!", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Toast.makeText(context, "Unauthorized! Please login again.", Toast.LENGTH_SHORT).show();
                break;
            case 403:
                Toast.makeText(context, "Forbidden! You don't have permission to access this resource.", Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(context, "Not Found! The requested resource doesn't exist.", Toast.LENGTH_SHORT).show();
                break;
            case 409:
                Toast.makeText(context, "This title already exist. Try something else!", Toast.LENGTH_SHORT).show();
                break;
            case 429:
                Toast.makeText(context, "Too Many Requests! Slow down and try again later.", Toast.LENGTH_SHORT).show();
                break;
            case 500:
                Toast.makeText(context, "Server error. Try again later.", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Unexpected Error: " + statusCode + ". Please try again.", Toast.LENGTH_SHORT).show();
                break;
        }

        Log.e("API_ERROR", logMessage);
    }


    public static void handleFailure(Throwable t, Context context, String activityName) {
        String errorMessage = t != null ? t.getMessage() : "Unknown error";
        String logMessage = activityName + " - Network Error: " + errorMessage;

        Toast.makeText(context, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
        Log.e("API_FAILURE", logMessage, t);
    }
}
