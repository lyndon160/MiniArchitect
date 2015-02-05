package com.LyndonFawcett.MiniArchitect.utils;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;

public class Updater {
	//Check what we've already downloaded and only get new models

	public Updater(){


		Document doc;
		try {

			// need http protocol
			doc = Jsoup.connect("http://lyndonfawcett.me/models").get();

			// get page title
			String title = doc.title();
			System.out.println("title : " + title);

			// get all links
			Elements links = doc.select("a[href]");
			for (final Element link : links) {
				//if contains g3db its a model!
				// get the value from href attribute
				if(link.text().contains("g3db")){
					Gdx.app.log("Link", link.text());

					//check if already have model FIXME
					HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
					httpGet.setUrl("http://lyndonfawcett.me/models/"+link.text().replace(" ", "%20"));

					Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
						public void handleHttpResponse(HttpResponse httpResponse) {
							//	Gdx.app.log("Model downloaded",httpResponse.getResult().length+"");
							//do stuff here based on response

							//can write to local
							if(Gdx.files.isLocalStorageAvailable()){
								Gdx.app.log("Writing new file",link.text());
								FileHandle file = Gdx.files.local("downloaded/models/"+link.text());
								//file.writeString(httpResponse.getResultAsString(), false);
								file.writeBytes(httpResponse.getResult(), false);
							}




						}

						public void failed(Throwable t) {
							System.out.println("failed to update");
							//do stuff here based on the failed attempt
						}

						@Override
						public void cancelled() {
							// TODO Auto-generated method stub

						}
					});



				}
				//System.out.println("\nlink : " + link.attr("href"));
				//System.out.println("text : " + link.text());

			}
			
			
			//download model images
			// need http protocol
			doc = Jsoup.connect("http://lyndonfawcett.me/miniarchitect/").get();

			// get page title
			//String title = doc.title();
			//System.out.println("title : " + title);

			// get all links
			links = doc.select("a[href]");
			for (final Element link : links) {
				//if contains g3db its a model!
				// get the value from href attribute
				if(link.text().contains("png")){
					Gdx.app.log("Link", link.text());

					//check if already have model FIXME
					HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
					httpGet.setUrl("http://lyndonfawcett.me/miniarchitect/"+link.text().replace(" ", "%20"));

					Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
						public void handleHttpResponse(HttpResponse httpResponse) {
							//	Gdx.app.log("Model downloaded",httpResponse.getResult().length+"");
							//do stuff here based on response

							//can write to local
							if(Gdx.files.isLocalStorageAvailable()){
								Gdx.app.log("Writing new file",link.text());
								FileHandle file = Gdx.files.local("downloaded/models/"+link.text());
								//file.writeString(httpResponse.getResultAsString(), false);
								file.writeBytes(httpResponse.getResult(), false);
							}




						}

						public void failed(Throwable t) {
							System.out.println("failed to update");
							//do stuff here based on the failed attempt
						}

						@Override
						public void cancelled() {
							// TODO Auto-generated method stub

						}
					});



				}
				//System.out.println("\nlink : " + link.attr("href"));
				//System.out.println("text : " + link.text());

			}

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			



			//get news

			doc = Jsoup.connect("http://lyndonfawcett.me/miniarchitect/news.txt").get();
			System.out.println(doc.text());

			if(Gdx.files.isLocalStorageAvailable()){
				Gdx.app.log("Creating news file",doc.text());
				FileHandle file = Gdx.files.local("downloaded/news/news.txt");
				file.writeBytes(doc.text().getBytes(), false);
			}


			doc = Jsoup.connect("http://lyndonfawcett.me/miniarchitect/news.json").ignoreContentType(true).get();
			System.out.println(doc.text());

			if(Gdx.files.isLocalStorageAvailable()){
				Gdx.app.log("Creating news file",doc.text());
				FileHandle file = Gdx.files.local("downloaded/news/news.json");
				file.writeBytes(doc.text().getBytes(), false);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
