package com.example.madproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	EditText emailEditText;
	EditText passwordEditText;
	Button loginButton;
	Button createAccountButton;
	ParseUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		emailEditText = (EditText) findViewById(R.id.editTextEmail);
		passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		loginButton = (Button) findViewById(R.id.buttonLogin);
		createAccountButton = (Button) findViewById(R.id.buttonCreateNewAccount);
		// ParseUser.logOut();
		if (!isOnline()) {
			Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
		} else {
			user = ParseUser.getCurrentUser();
			// ParseUser.logOut();
			if (user != null) {

				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

			loginButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String email = emailEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					boolean login = true;
					if (isEmpty(email)) {
						Toast.makeText(LoginActivity.this, "Email is missing", Toast.LENGTH_LONG).show();
						login = false;
					} else if (isEmpty(password)) {
						Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_LONG).show();
						login = false;
					}
					if (login) {
						ParseUser.logInInBackground(email, password, new LogInCallback() {

							@Override
							public void done(ParseUser loggedInUser, ParseException e) {
								// TODO Auto-generated method stub
								if (e == null) {
									user = loggedInUser;

									ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
									currentInstallation.put("username", user.getObjectId());
									currentInstallation.put("useremail", user.getEmail());
									currentInstallation.saveInBackground();
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(intent);
									finish();
								} else {
									Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				}
			});

			createAccountButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
	}

	public boolean isEmpty(String str) {
		if (str == null)
			return true;
		if (str.length() <= 0)
			return true;

		return false;
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
