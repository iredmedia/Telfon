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
            
            //t.getRatingsByUrl("https://market.android.com/details?id=com.galapagossoft.trialx2_gl2&feature=search_result");
            
            String[] paidfree = new String[]{"paid","free"};

            for(int a =0;a<paidfree.length;a++)
            {

                String[] cats  = new String[]{"ARCADE","BRAIN", "CARDS", "CASUAL", "GAME_WALLPAPER", "RACING", "SPORTS_GAMES", "GAME_WIDGETS"};

                for(int c = 0; c < cats.length; c++){    
                
                    System.out.print(cats[c]);
                    
                    for(int i= 0 ; i < 800; i+=24){
                       
                        String f =  t.getTopPackages("apps_topselling_" + paidfree[a], cats[c], i, 24)+"\n";
                       
                        if(f == null){
                           break;
                        }
                    }
                }
            }
//		for(int i = 0;i<800;i++){
//			System.out.println(i);
//			mh.GetPaid("info@andspot.com", "QWERllandspot", i, "PRODUCTIVITY");
//			Thread.sleep(5000);
//		}
//		
            System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");

	}

	
}
