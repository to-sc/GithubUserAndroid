package de.tobias_schaarschmidt.githubuserandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements UserDownloadedDelegate {

    private Button btnSearch;
    private EditText userInput;

    private TextView tvId;
    private TextView tvLogin;
    private TextView tvCreatedAt;
    private TextView tvHtmlUrl;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        userInput = (EditText) findViewById(R.id.userInput);

        tvId = (TextView) findViewById(R.id.tvId);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        tvHtmlUrl = (TextView) findViewById(R.id.tvHtmlUrl);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInput.getText().length() > 0) {
                    getUserData(userInput.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Textfield \"username\" is empty.\nInsert a username.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getUserData(String username) {
        UserDownloader userDownloader = new UserDownloader(username, this, this);
    }

    @Override
    public void userDownloaded(User user) {
        tvId.setText(String.valueOf(user.getId()));
        tvLogin.setText(user.getLogin());
        tvCreatedAt.setText(user.getCreatedAt());
        tvHtmlUrl.setText(user.getHtmlUrl());
        tvLocation.setText(user.getLocation());
    }
}






