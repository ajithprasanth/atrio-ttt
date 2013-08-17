package com.trio.triotictactoe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.trio.triotictactoe.R;

public class FacebookActivity extends FragmentActivity {
	private static final int FB_ACTION_INVALID = -1;
	private static final int FB_ACTION_GET_USERNAME = 1;
	private static final int FB_ACTION_FETCH_FRIENDS = 2;
	private static final int FB_ACTION_PUBLISH_FEED = 3;

	private int requiredUserAction = FB_ACTION_INVALID;
	private Handler responseHandler;
	private Bundle publishData;
	private ProgressDialog spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Opens an active session in the background to check whether user is logged in
	 * @return
	 */
	protected boolean isUserLoggedIn() {
		Session session = Session.openActiveSession(FacebookActivity.this, false, null);
		return session != null;
	}

	/**
	 * Have to update some facebook class on activity result
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	/**
	 * Gateway for the subclass activity to get the username
	 * The obj field in the returned message would have the username as String
	 * @param handler
	 */
	protected void getFacebookUsername(Handler handler) {
		requiredUserAction = FB_ACTION_GET_USERNAME;
		responseHandler = handler;
		handleUserAction();
	}

	/**
	 * Gateway for the subclass activity to get the friends for this user.
	 * The obj field in the returned message would have the friends as JSONObject
	 * @param handler
	 */
	protected void getFacebookFriends(Handler handler) {
		requiredUserAction = FB_ACTION_FETCH_FRIENDS;
		responseHandler = handler;
		handleUserAction();
	}

	/**
	 * Gateway for the subclass activity to get the friends for this user.
	 * The obj field in the returned message would have the friends as JSONObject
	 * @param handler
	 */
	protected void publishNewsFeed(Bundle data, Handler handler) {
		requiredUserAction = FB_ACTION_PUBLISH_FEED;
		publishData = data;
		responseHandler = handler;
		handleUserAction();
	}

	private void handleUserAction() {
		if (!isUserLoggedIn()) {
			loginAndPerformRequiredAction();
		} else {
			performRequiredAction();
		}
	}

	/**
	 * Calls facebook activity internally to login first and then performs the required action.
	 * The facebook activity calls the facebook app to login, otherwise a webview is opened.
	 */
	private void loginAndPerformRequiredAction() {
		Session.openActiveSession(FacebookActivity.this, true, new Session.StatusCallback() {
			@Override
			public void call(final Session session, final SessionState state, final Exception exception) {
				// callback when session changes state
				if (session.isOpened()) {
					performRequiredAction();
				}
			}
		});
	}

	/**
	 * Perform the action requested by subclass
	 * in UI thread. The spinner should be closed by the appropriate method.
	 */
	private void performRequiredAction() {
		FacebookActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				switch (requiredUserAction) {
					case FB_ACTION_GET_USERNAME :
						fetchUsername();
						break;

					case FB_ACTION_FETCH_FRIENDS :
						fetchFriends();
						break;

					case FB_ACTION_PUBLISH_FEED :
						publishFeedDialog();
						break;
				}
			}
		});
	}

	/**
	 * The core method for fetching username using the request
	 */
	private void fetchUsername() {
		startSpinner();

		Request.executeMeRequestAsync(Session.getActiveSession(), new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				// callback after Graph API response with user object
				if (user != null) {
					Message msg = new Message();
					msg.obj = user.getName();
					if (responseHandler != null) {
						responseHandler.sendMessage(msg);
					}
					onUserActionComplete();
				}
			}
		});
	}

	/**
	 * The core method for fetching friends using the request
	 */
	private void fetchFriends() {
		startSpinner();

		Bundle bundle = new Bundle();
		bundle.putString("fields", "name,username");
		new Request(Session.getActiveSession(), "me/friends", bundle, HttpMethod.GET, new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				Message msg = new Message();
				msg.obj = response.getGraphObject().asMap().get("data");
				if (responseHandler != null) {
					responseHandler.sendMessage(msg);
				}
				onUserActionComplete();
			}
		}).executeAsync();
	}

	/**
	 * The core method for publish data obtained from a bundle
	 */
	private void publishFeedDialog() {
		if (publishData != null) {
			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), publishData)).build();
			feedDialog.show();
		}
		if (responseHandler != null) {
			responseHandler.sendEmptyMessage(0);
		}
	}

	private void startSpinner() {
		spinner = new ProgressDialog(this);
		spinner.setMessage(getResources().getString(R.string.just_a_second));
		spinner.setIndeterminate(true);
		spinner.setCancelable(false);
		spinner.show();
	}

	private void stopSpinner() {
		if (spinner != null && spinner.isShowing()) {
			spinner.dismiss();
		}
	}

	/**
	 * Called whenever FB call has been completed 
	 */
	private void onUserActionComplete() {
		requiredUserAction = FB_ACTION_INVALID;
		responseHandler = null;
		stopSpinner();
	}
}
