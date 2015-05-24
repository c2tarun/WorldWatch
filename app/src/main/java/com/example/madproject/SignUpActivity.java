package com.example.madproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	
	EditText usernameEditText;
	EditText emailEditText;
	EditText passwordEditText;
	EditText confirmPassEditText;
	Button signupButton;
	Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		usernameEditText = (EditText) findViewById(R.id.editTextUserName);
		emailEditText = (EditText) findViewById(R.id.editTextEmail);
		passwordEditText = (EditText) findViewById(R.id.editTextPassword);
		confirmPassEditText = (EditText) findViewById(R.id.editTextPasswordConfirm);
		signupButton = (Button) findViewById(R.id.buttonSignup);
		cancelButton = (Button) findViewById(R.id.buttonCancel);
		
		signupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = usernameEditText.getText().toString();
				String email = emailEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				String confirmPass = confirmPassEditText.getText().toString();
				boolean login = true;
				
				if(isEmpty(username)) {
					Toast.makeText(SignUpActivity.this, "Username is missing", Toast.LENGTH_LONG).show();
					login = false;
				} else 
				if(isEmpty(email)) {
					Toast.makeText(SignUpActivity.this, "Email is missing", Toast.LENGTH_LONG).show();
					login = false;
				} else 
				if(isEmpty(password)) {
					Toast.makeText(SignUpActivity.this, "Password is empty", Toast.LENGTH_LONG).show();
					login = false;
				} else 
				if(!password.equals(confirmPass)) {
					Toast.makeText(SignUpActivity.this, "Password doesn't match with confirm password", Toast.LENGTH_LONG).show();
					login = false;
				}
				
				if(login) {
					ParseUser user = new ParseUser();
					user.setEmail(email);
					user.setUsername(email);
					user.setPassword(password);
					
					user.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {
							// TODO Auto-generated method stub
							if(e == null) {
								Toast.makeText(SignUpActivity.this, "Signup Successfull", Toast.LENGTH_LONG).show();
								ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
								currentInstallation.put("username", ParseUser.getCurrentUser().getObjectId());
								currentInstallation.put("useremail", ParseUser.getCurrentUser().getEmail());
								currentInstallation.saveInBackground();
								Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
							}
						}
					});
				}
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	public boolean isEmpty(String str) {
		if(str == null)
			return true;
		if(str.length() <= 0)
			return true;
		
		return false;
	}
}
