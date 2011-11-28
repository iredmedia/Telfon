package com.andspot.teflon;

import java.io.IOException;

import com.andspot.jsonk.JSONException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, JSONException, InterruptedException {
		Teflon t = new Teflon();
		t.getRatingsByUrl("https://market.android.com/details?id=com.galapagossoft.trialx2_gl2&feature=search_result");

//		for(int i = 0;i<800;i++){
//			System.out.println(i);
//			mh.GetPaid("info@andspot.com", "QWERllandspot", i, "PRODUCTIVITY");
//			Thread.sleep(5000);
//		}
//		
	}

	
}
