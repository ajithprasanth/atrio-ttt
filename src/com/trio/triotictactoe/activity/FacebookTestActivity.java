package com.trio.triotictactoe.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.trio.triotictactoe.R;

@SuppressLint("HandlerLeak")
public class FacebookTestActivity extends FacebookActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_test_activity);

		findViewById(R.id.facebook_fetch_username_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FacebookTestActivity.this.getFacebookUsername(usernameHandler);
			}
		});

		findViewById(R.id.facebook_fetch_friends).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FacebookTestActivity.this.getFacebookFriends(friendsDataHandler);
			}
		});

		findViewById(R.id.facebook_publish).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			    Bundle bundleData = new Bundle();
			    bundleData.putString("name", "Name");
			    bundleData.putString("caption", "First post");
				bundleData.putString("description", "First post description");
				bundleData.putString("link", "http://www.amazon.com/Patch-675-Tic-Tac-Toe/dp/B003ATT68A");
				bundleData.putString("picture", "http://ecx.images-amazon.com/images/I/61roGmJcZwL._SX150_.jpg");
				
			    FacebookTestActivity.this.publishNewsFeed(bundleData, null);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isUserLoggedIn()) {
			findViewById(R.id.com_facebook_usersettingsfragment_login_button).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.com_facebook_usersettingsfragment_login_button).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Username Data would be given back in msg.obj as @String
	 */
	private final Handler usernameHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String username = (String) msg.obj;
			if (username != null) {
				((TextView) findViewById(R.id.facebook_data_holder)).setText(username);
			}
		}
	};

	/**
	 * Friends Data would be given back in msg.obj as @JSONArray
	 */
	private final Handler friendsDataHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			JSONArray friends = (JSONArray) msg.obj;
			StringBuffer names = new StringBuffer();

			if (friends != null) {
				for (int i = 0; i < friends.length() && i < 10; i++) {
					try {
						names.append(((JSONObject) friends.get(i)).get("name"));
						names.append(" - ");
						names.append(((JSONObject) friends.get(i)).get("username"));
						names.append("\n");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				((TextView) findViewById(R.id.facebook_data_holder)).setText(names.toString());
			}
		}
	};

}
