
package ca.usherbrooke.koopa.udesvolumecontrol;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import message.ExistsUserReply;
import message.ExistsUserRequest;
import message.PostNewUserReply;
import message.PostNewUserRequest;
import utils.ClientTCP;
import utils.Serializer;

public class LoginActivity extends AppCompatActivity
{
    private UserLoginTask mAuthTask = null;
    private CreateUserTask mCreateUserTask = null;
    private final int m_port = 9005;

    private AutoCompleteTextView mUsernameView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mUsernameView.setError(null);

        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(username);
            mAuthTask.execute((Void) null);
        }
    }

    private void createNewUser()
    {
        if (mCreateUserTask != null)
        {
            return;
        }


        mUsernameView.setError(null);

        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mCreateUserTask = new CreateUserTask(username);
            mCreateUserTask.execute((Void) null);
        }


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;

        UserLoginTask(String username) {
            mUsername = username;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ClientTCP cl = null;
            try {
                cl = new ClientTCP();
                cl.connect(getResources().getString(R.string.ipAddr), m_port);

                cl.send(Serializer.serialize(new ExistsUserRequest(mUsername)));

                ExistsUserReply mess = (ExistsUserReply) cl.receive();

                if (mess.isExisting())
                {
                    Intent myIntent = new Intent(LoginActivity.this, AllLocationsActivity.class);
                    myIntent.putExtra("userName",mUsername);
                    startActivity(myIntent);
                    return true;
                }

            }  catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (cl != null) {
                        cl.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (!success) {
                Toast.makeText(getApplicationContext(), "Invalid user!", Toast.LENGTH_LONG).show();
            }
            else
            {

                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class CreateUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;

        CreateUserTask(String username) {
            mUsername = username;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ClientTCP cl = new ClientTCP();
                cl.connect(getResources().getString(R.string.ipAddr), m_port);

                cl.send(Serializer.serialize(new PostNewUserRequest(mUsername)));

                PostNewUserReply mess = (PostNewUserReply) cl.receive();

                return mess.isSuccess();

            }  catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCreateUserTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(getApplication(), "User create, Sign up now!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplication(), "Can't create user!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mCreateUserTask = null;
            showProgress(false);
        }
    }
}
