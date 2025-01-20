package com.enolic.noviflix.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Response;

public class ErrorHandler {

    // log errors to a file
    private static void logErrorToFile(Context context, String message) {
        try {
            File logFile = new File(context.getFilesDir(), "api_errors.log");
            FileWriter writer = new FileWriter(logFile, true); // Append mode
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            Log.e("ErrorHandler", "Failed to log error to file: " + e.getMessage());
        }
    }

    public static void handleError(Response<?> response, Context context, String activityName) {
        try {
            int statusCode = response.code();
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            String logMessage = activityName + " - Status Code: " + statusCode + ", Error: " + errorBody;

            // send log to the file
            logErrorToFile(context, logMessage);

            // manage status codes
            switch (statusCode) {
                case 401:
                    Toast.makeText(context, "Unauthorized! Please login again.", Toast.LENGTH_SHORT).show();
                    break;
                case 403:
                    Toast.makeText(context, "Forbidden! You don't have permission to access this resource.", Toast.LENGTH_SHORT).show();
                    break;
                case 404:
                    Toast.makeText(context, "Not Found! The requested resource doesn't exist.", Toast.LENGTH_SHORT).show();
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
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void handleFailure(Throwable t, Context context, String activityName) {
        String errorMessage = t != null ? t.getMessage() : "Unknown error";
        String logMessage = activityName + " - Network Error: " + errorMessage;

        logErrorToFile(context, logMessage);

        Toast.makeText(context, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
        Log.e("API_FAILURE", logMessage, t);
    }
}
