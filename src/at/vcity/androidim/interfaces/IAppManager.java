package at.vcity.androidim.interfaces;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public interface IAppManager {
	
	public String getNickname();
	public String sendMessage(int receiver_id, String message) throws UnsupportedEncodingException;
	public JSONObject authenticateUser() throws UnsupportedEncodingException;
	public void messageReceived(int sender_id, String nick, String message);
	public boolean isNetworkConnected();
	public boolean isUserAuthenticated();
	public void exit();
	public String addFavourite(int id);
    public String removeFavourite(int id);
}
