package com.raye7.reddit.Listener;

public interface AsyncTaskListener
{
	public void onSuccessRemoteCallComplete(int operation, String result);

	public void onFailRemoteCallComplete(int operation, String result);


}
