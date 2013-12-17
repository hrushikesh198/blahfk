package at.vcity.androidim;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import at.vcity.androidim.communication.SocketOperator;
import at.vcity.androidim.interfaces.ISocketOperator;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Login extends Activity {

	// Your Facebook APP ID
	private static String APP_ID = "716867304991179"; // Replace with your App ID

	// Instance of Facebook Class
//    String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;

	private static ISocketOperator socket_op = new SocketOperator();

	// Buttons
	Button btnFbLogin, btnFbLogout;

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//        btnFbLogin = (Button) findViewById(R.id.btn_fblogin);
//        btnFbLogout = (Button) findViewById(R.id.btn_fblogout);
//        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = createSession();
		if (!session.isOpened()) {
			session.openForRead(new Session.OpenRequest(this));
		}
		final String requestId = "me";
		Request fbreq = new Request(session, requestId, null, null, new Request.Callback() {
			public void onCompleted(Response response) {
				GraphObject graphObject = response.getGraphObject();
				FacebookRequestError error = response.getError();
				String s = "";
				if (graphObject != null) {
					if (graphObject.getProperty("id") != null) {
						s = s + String.format("%s: %s\n", graphObject.getProperty("id"), graphObject.getProperty(
								"name"));
						String fb_name = "", fb_id = "", sex = "F";
						int age = 0;
						double lat_x = 0, long_y = 0;
						try {
							String params = "fb_name=" + enc(fb_name) + "&fb_id=" + fb_id +
									"&sex=" + sex +
									"&age=" + age +
									"&lat_x=" + lat_x +
									"&long_y=" + long_y;
							String res = socket_op.sendHttpRequest(params);
							//TODO: from res build search_n_res_page_i
							Intent search_n_res_page_i = new Intent();
							/**
							 * 1.create user if not found on db
							 * 2.lsist of [age, gender, name, status]
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						s = s + String.format("%s: <no such id>\n", requestId);
					}
				} else if (error != null) {
					s = s + String.format("Error: %s", error.getErrorMessage());
				}
				final String s2 = s;
				Log.d("graph obj", graphObject.getInnerJSONObject().toString());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						toast(s2);
					}
				});
			}
		});
		fbreq.executeAsync();
	}

	public static String enc(String msg) throws UnsupportedEncodingException {
		return URLEncoder.encode(msg, "UTF-8");
	}

	private Session createSession() {
		Session activeSession = Session.getActiveSession();
		if (activeSession == null || activeSession.getState().isClosed()) {
			activeSession = new Session.Builder(this).setApplicationId(APP_ID).build();
			Session.setActiveSession(activeSession);
		}
		return activeSession;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}