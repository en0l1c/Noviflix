package com.enolic.noviflix.tools;

import android.content.Context;
import android.widget.Toast;

import com.enolic.noviflix.R;

public class SuccessHandler {

    public static void handleSuccess(int code, String action, Context context) {
        switch (code) {
            case 200: // general success
                handleActionSpecificSuccess(action, context);
                break;
            case 201: // resources created
                if ("add".equals(action)) {
                    Toast.makeText(context, context.getString(R.string.success_movie_added), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.success_action_completed), Toast.LENGTH_SHORT).show();
                }
                break;
            case 204: // no content
                if ("update".equals(action)) {
                    Toast.makeText(context, context.getString(R.string.success_movie_updated), Toast.LENGTH_SHORT).show();
                } else if ("delete".equals(action)) {
                    Toast.makeText(context, context.getString(R.string.success_movie_deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.success_action_completed), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(context, context.getString(R.string.success_default, code), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static void handleActionSpecificSuccess(String action, Context context) {
        if ("update".equals(action)) {
            Toast.makeText(context, context.getString(R.string.success_movie_updated), Toast.LENGTH_SHORT).show();
        } else if ("delete".equals(action)) {
            Toast.makeText(context, context.getString(R.string.success_movie_deleted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.success_action_completed), Toast.LENGTH_SHORT).show();
        }
    }
}

