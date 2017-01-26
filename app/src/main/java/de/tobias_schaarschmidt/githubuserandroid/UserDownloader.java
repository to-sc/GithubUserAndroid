package de.tobias_schaarschmidt.githubuserandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by to-sc
 * https://github.com/to-sc
 */

public class UserDownloader extends AsyncTask<String, Void, JSONObject> {

    private UserDownloadedDelegate delegate;
    private Context context;

    public UserDownloader(String username, UserDownloadedDelegate delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
        execute(String.format("https://api.github.com/users/%s", username));
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        return getUserData(params[0]);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (jsonObject != null) {
            User user = parseJson(jsonObject);
            delegate.userDownloaded(user);
        } else {
            Toast.makeText(context, "User does not exist", Toast.LENGTH_LONG).show();
        }
    }

    private StringBuilder getJsonObject(HttpURLConnection httpURLConnection, StringBuilder buffer) {
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int charRead;
            char[] inputBuffer = new char[500];
            while (true) {
                charRead = inputStreamReader.read(inputBuffer);
                if (charRead <= 0) {
                    break;
                }
                buffer.append(String.copyValueOf(inputBuffer, 0, charRead));
            }
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getUserData(String httpUrl) {
        StringBuilder buffer = new StringBuilder();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            switch (httpURLConnection.getResponseCode()) {
                case 200:
                    getJsonObject(httpURLConnection, buffer);
                    return new JSONObject(buffer.toString());
                case 404:
                    return null;
                default:
                    return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User parseJson(JSONObject jsonObject) {
        User user = new User();

        try {
            user.setId(jsonObject.getInt("id"));
            user.setLogin(jsonObject.getString("login"));
            user.setAvatarUrl(jsonObject.getString("avatar_url"));
            user.setHtmlUrl(jsonObject.getString("html_url"));
            user.setType(jsonObject.getString("type"));
            user.setName(jsonObject.getString("name"));
            user.setCompany(jsonObject.getString("company"));
            user.setBlog(jsonObject.getString("blog"));
            user.setLocation(jsonObject.getString("location"));
            user.setEmail(jsonObject.getString("email"));
            user.setBio(jsonObject.getString("bio"));
            user.setFollowers(jsonObject.getInt("followers"));
            user.setFollowing(jsonObject.getInt("following"));
            user.setCreatedAt(jsonObject.getString("created_at"));
            user.setUpdatedAt(jsonObject.getString("updated_at"));
            user.setPublicRepos(jsonObject.getInt("public_repos"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}