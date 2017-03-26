package id.go.kebumenkab.perizinan.siperikebumen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String urlApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NoDefaultSpinner sebagai = (NoDefaultSpinner) findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sebagai, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sebagai.setAdapter(adapter);
        sebagai.setPrompt(Html.fromHtml("<font color=\"#ffffff\">Status</font>"));

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        final EditText editUrlApi = (EditText) findViewById(R.id.url_api);
        final EditText editUsername = (EditText) findViewById(R.id.username);
        final EditText editPassword = (EditText) findViewById(R.id.password);
        final NoDefaultSpinner editStatus = (NoDefaultSpinner) findViewById(R.id.status);
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        editUrlApi.setText("202.91.8.60/api");

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editStatus.getSelectedItemPosition() == 0) {
                    urlApi = "http://" + editUrlApi.getText().toString().trim() + "/" + AppConfig.URL_LOGIN_TERKAIT;
                    String username = editUsername.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!username.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(username, password, editUrlApi.getText().toString().trim(), "seksitimteknis");
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else if(editStatus.getSelectedItemPosition() == 1) {
                    urlApi = "http://" + editUrlApi.getText().toString().trim() + "/" + AppConfig.URL_LOGIN_BIDANG;
                    String username = editUsername.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!username.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(username, password, editUrlApi.getText().toString().trim(), "kepalabidang");
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else if(editStatus.getSelectedItemPosition() == 2) {
                    urlApi = "http://" + editUrlApi.getText().toString().trim() + "/" + AppConfig.URL_LOGIN_SEKERTARIS;
                    String username = editUsername.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!username.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(username, password, editUrlApi.getText().toString().trim(), "sekertarisdinas");
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else if(editStatus.getSelectedItemPosition() == 3) {
                    urlApi = "http://" + editUrlApi.getText().toString().trim() + "/" + AppConfig.URL_LOGIN_KEPALA;
                    String username = editUsername.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!username.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(username, password, editUrlApi.getText().toString().trim(), "kepaladinas");
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });
    }

    private void checkLogin(final String username, final String password, final String url_api, final String user_type) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                urlApi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String nama = user.getString("nama");
                        String jabatan = user.getString("jabatan");
                        String golongan = user.getString("golongan");
                        String notelp = user.getString("notelp");
                        String nohp = user.getString("nohp");
                        String alamat = user.getString("alamat");
                        String username = user.getString("username");
                        Integer status = Integer.parseInt(user.getString("status"));

                        // Inserting row in users table
                        db.addUser(nama, uid, jabatan, golongan, notelp, nohp, alamat, username, url_api, user_type, status);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };

        // Adding request to request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
