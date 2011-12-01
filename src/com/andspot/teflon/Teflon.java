package com.andspot.teflon;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import org.w3c.tidy.Tidy;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.auth.PropertiesCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.PutObjectRequest;

import com.andspot.jsonk.JSONArray;
import com.andspot.jsonk.JSONException;
import com.andspot.jsonk.JSONObject;

import java.awt.List;

public class Teflon {
        int GLOBAL__COUNTER = 0;
        public List listVisited = new List();
        public List listToVisit = new List(); 
     
        // takes package URL and returns JSON object containing ratings & values from package market page
        public JSONObject getRatingsByUrl(String url) throws JSONException{
            
            String packagename = url.replace("https://market.android.com/details?id=", "").split("&")[0];
            return propogate(packagename);	
            
	}
        
        // takes package name and returns entire HTML document for this particular package (incl related links & ratings)
        public Document getDocument(String packageName){ // Retrieve document structure by package name
            
            URL u;
            Document document = null;
            System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
            
            try{
                u = new URL("https://market.android.com/details?id=" + packageName);
                document = Jsoup.parse(u,90000);
                
            }
            catch(Exception e){}

            return document; 
            
        }
        
        // takes package name and returns list of elements containing all package links on market page
        public String[] getPackageLinks(String packageName){ // Find all related links on page
            
            String[] returnStrArray = new String[12];
            Elements el = getDocument(packageName).getElementsByClass("common-snippet-title");
            String attr;
            
            
            // Return list of links as package name
            for(int i = 0; i < el.size(); i++){
                attr = el.get(i).attr("href").replace("/details?id=", "").split("&")[0];; 
                returnStrArray[i] = attr;
            }
            
            return returnStrArray;
        
        }
        
        // takes package name and returns JSON object containing ratings & values from package market page
        public JSONObject getPackageRatings(String packageName){

            String rating;
            String votes;
            URL u;

            // Get document
            Document document = getDocument(packageName);

            System.setProperty("http.agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");

            try{
                u = new URL("https://market.android.com/details?id="+packageName);
                document = Jsoup.parse(u,90000);


                // Get ratings/other info
                try{                
                    rating = document.getElementsByClass("average-rating-value").get(0).html();                
                    votes = document.getElementsByClass("votes").get(0).html();
                }catch(Exception e){
                    rating = "0";
                    votes = "0";
                }


                String appName = document.getElementsByClass("doc-banner-title").html();
                String desc = document.getElementById("doc-original-text").html();
                String downloadtext = "";
                String entireDoc = document.toString();

                if(entireDoc.contains("5,000 - 10,000")){
                        downloadtext = "5,000 - 10,000";
                }else if(entireDoc.contains("50,000 - 100,000")){
                        downloadtext = "50,000 - 100,000";
                }else if(entireDoc.contains("100,000 - 500,000")){
                        downloadtext = "100,000 - 500,000";
                }else if(entireDoc.contains("500,000 - 1,00,000")){
                        downloadtext = "500,000 - 1,00,000";
                }else if(entireDoc.contains("1,000,000 - 5,000,000")){
                        downloadtext = "1,000,000 - 5,000,000";
                }else if(entireDoc.contains("1,000 - 5,000")){
                        downloadtext = "1,000 - 5,000";
                }else if(entireDoc.contains("500 - 1,000")){
                        downloadtext = "500 - 1,000";
                }else{
                        downloadtext = "0 - 500";
                }

                    Document sc = Jsoup.parse(document.getElementsByClass("doc-overview-screenshots").html());
                    Elements imgs = sc.select("img");
                    ArrayList<String> screenshots = new ArrayList<String>();

                for (Element img : imgs) {
                    screenshots.add(img.attr("src"));
                }

                    screenshots.remove(0);
                    screenshots.remove(screenshots.size()-1);

                    Document ic = Jsoup.parse(document.getElementsByClass("doc-banner-icon").html());
                    Elements icons  = ic.select("img");
                    String icon = "";

                for (Element img : icons) {
                    icon = img.attr("src");
                }

                    Document pr = Jsoup.parse(document.getElementsByClass("doc-banner-image-container").html());
                    Elements promo = pr.select("img");
                    String promoURL = "";

                for (Element img : promo){
                    promoURL = img.attr("src");
                }

                Document cr = Jsoup.parse(u,9000);
                downloadtext = cr.getElementsByAttributeValue("itemprop", "numDownloads").html();


                Elements a = cr.getElementsByClass("doc-metadata-list");
                Elements b = a.select("a");

                String contentRating = 
                        cr.getElementsByAttributeValue("itemprop", "contentRating").html();
                String version = 
                        cr.getElementsByAttributeValue("itemprop","softwareVersion").html();
                String lastUpdate = 
                        cr.getElementsByAttributeValue("itemprop", "datePublished").html();
                String filesize = 
                        cr.getElementsByAttributeValue("itemprop", "fileSize").html();
                String price = 
                        cr.getElementsByAttributeValue("itemprop", "price").html();
                String category = 
                        b.get(0).text();

                JSONObject jb = new JSONObject();
                
                jb.put("RATING", rating);
                jb.put("VOTES", votes);
                jb.put("APPNAME", appName);
                jb.put("DESC", desc);
                jb.put("SCREENSHOTS", screenshots);
                jb.put("ICON", icon);
                jb.put("PROMOURL", promoURL);
                jb.put("CATEGORY", category);
                jb.put("PRICE",price);      
                jb.put("DOWNLOADTEXT",  downloadtext);
                jb.put("LASTUPDATE", lastUpdate);
                jb.put("CONTENTRATING", contentRating);
                jb.put("VERSION", version);
                jb.put("FILESIZE", filesize);
                
                return jb;

            } catch(Exception e){}

            return null;
        
        }
             
        public JSONObject propogate(String packageName){
            
            JSONObject JSONReturn;
            
            // Get current package JSON output (getPkgRatings)
            parseMe(getPackageRatings(packageName));
            
            // Get current package related links (getPkgLinks)
            String[] packageLinks = getPackageLinks(packageName);
             
            GLOBAL__COUNTER++;
            
            if(packageLinks[GLOBAL__COUNTER] == null){
            
                GLOBAL__COUNTER= 0;
                JSONReturn = propogate(packageLinks[GLOBAL__COUNTER]);

            }
            else{
            
                JSONReturn = propogate(packageLinks[GLOBAL__COUNTER]);
            
            }
            
            return JSONReturn;
        }        
        
        // Method to send to database
	public String parseMe(JSONObject jsonObject){
            String returnStr = jsonObject.toString();
            debug("Found record: " + returnStr);
            return returnStr;
        }
     
        
	public String getVersionNumber(String aaptOutput){

            int x, y;
            String sdkVersion;
            
            x = aaptOutput.indexOf("sdkVersion:'")+12;
            y = aaptOutput.indexOf("'", x);
            
            sdkVersion = aaptOutput.substring(x, y);

            return sdkVersion;
	}
        
        /* Helper classes */
        public String debug(String toOutput){
            String message = null;
            try{
                message = toOutput + "\r\n";
                System.out.print(toOutput + "\r\n");
                return message;
            }   
            catch(Exception e) {
                message = "Failure to find variable \r\n" + "\r\n";
                System.out.print("Failure to find variable \r\n");
                return message;
            }
        }
        
        public String debug(String[] toOutput){
            String message = null;
            for(int i = 0; i < toOutput.length; i++){
                
                message += toOutput[i] + "\r";
            
            }
            return message;
        }
}