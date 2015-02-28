package com.LyndonFawcett.MiniArchitect.UI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

//Contains window, table, text items, table with image and text - on click opens item
//reads news.json for information
public class CardWindow extends Window{
	ScrollPane scroll;
	Table content;
	public CardWindow(Skin skin){
		super("",skin, "chalk");
		content = new Table(skin);
		scroll = new ScrollPane(content, skin,"transparent");

		this.add(scroll);
		scroll.setWidth(800);
		try {

			//read in news.json to get text and images for list
			if(Gdx.files.isLocalStorageAvailable()){
				FileHandle file = Gdx.files.local("downloaded/news/news.json");
				JSONObject obj = new JSONObject(file.readString());
				//String pageName = obj.getJSONObject("pageInfo").getString("pageName");

				JSONArray arr;

				arr = obj.getJSONArray("textcards");

				for (int i = 0; i < arr.length(); i++)
				{
					String text = arr.getJSONObject(i).getString("text");
					

					//add card to window
					content.add(new Label(text, skin, "24")).row();

				}

				arr = obj.getJSONArray("imagecards");
				for (int i = 0; i < arr.length(); i++)
				{
					String text = arr.getJSONObject(i).getString("text");
					String imagepath = arr.getJSONObject(i).getString("image");
					String price =arr.getJSONObject(i).getString("price");
					//add card to window
					Table card = new Table(skin);
					file = Gdx.files.local("downloaded/models/"+imagepath);

					card.add(new ImageButton((Drawable) new TextureRegionDrawable(new TextureRegion(new Texture(file)))));
					card.add(new Label(text+" "+price,skin,"24"));
					content.add(card).align(Align.center).pad(10).row();
				}
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
