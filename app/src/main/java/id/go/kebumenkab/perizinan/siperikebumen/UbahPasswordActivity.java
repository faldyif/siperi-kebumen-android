package id.go.kebumenkab.perizinan.siperikebumen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UbahPasswordActivity extends AppCompatActivity {

    private static final String TAG = UbahPasswordActivity.class.getSimpleName();
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private String urlApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        final HashMap<String, String> user = db.getUserDetails();
        final EditText editCurrentPassword = (EditText) findViewById(R.id.password);
        final EditText editNewPassword = (EditText) findViewById(R.id.password_baru);
        Button buttonSubmit = (Button) findViewById(R.id.button_submit);

        if(user.get("user_type").equals("seksitimteknis")) {
            urlApi = "http://" + user.get("url_api") + "/" + AppConfig.URL_RESET_PASS_TERKAIT;
        } else if(user.get("user_type").equals("kepalabidang")) {
            urlApi = "http://" + user.get("url_api") + "/" + AppConfig.URL_RESET_PASS_BIDANG;
        } else if(user.get("user_type").equals("sekertarisdinas")) {
            urlApi = "http://" + user.get("url_api") + "/" + AppConfig.URL_RESET_PASS_SEKERTARIS;
        } else if(user.get("user_type").equals("kepaladinas")) {
            urlApi = "http://" + user.get("url_api") + "/" + AppConfig.URL_RESET_PASS_KEPALA;
        }

        Log.d(TAG, "onCreate: " + urlApi);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.get("username");
                String password = editCurrentPassword.getText().toString().trim();
                String newPassword = editNewPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!password.isEmpty() && !newPassword.isEmpty()) {
                    // login user
                    changePassword(username, password, newPassword);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void changePassword(final String username, final String password, final String new_password) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

        Log.d(TAG, "changePassword: "+username+" "+password+" "+new_password);

        pDialog.setMessage("Applying changes ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                urlApi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(UbahPasswordActivity.this, "Password berhasil diubah!", Toast.LENGTH_SHORT).show();
                        // Launch main activity
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
                Log.e(TAG, "Error: " + error.getMessage());
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
                params.put("new_password", new_password);

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

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(UbahPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
