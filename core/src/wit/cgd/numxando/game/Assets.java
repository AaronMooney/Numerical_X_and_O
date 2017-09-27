/**
 * @file        Assets.java
 * @author      Aaron Mooney 20072163
 * @assignment  Numerical X and O
 * @brief       controls all assets
 *
 * @notes       
 * 				
 */
package wit.cgd.numxando.game;

import wit.cgd.numxando.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = WorldRenderer.class.getName();

	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public AssetFonts fonts;

	public Asset board;
	public AssetSounds sounds;
	public AssetMusic music;

	public Array<Asset> numbers;

	public class Asset {
		public final AtlasRegion region;

		public Asset(TextureAtlas atlas, String imageName) {
			region = atlas.findRegion(imageName);
		}
	}

	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;

		public AssetFonts() {
			// create three fonts using Libgdx's built-in 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(4.0f);

			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	private Assets() {
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// start loading assets and wait until finished
		// load sounds
		assetManager.load("../android/assets/sounds/first.wav", Sound.class);
		assetManager.load("../android/assets/sounds/second.wav", Sound.class);
		assetManager.load("../android/assets/sounds/win.wav", Sound.class);
		assetManager.load("../android/assets/sounds/draw.wav", Sound.class);

		// load music
		assetManager.load("../android/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);

		assetManager.finishLoading();

		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		fonts = new AssetFonts();

		// build game resource objects
		board = new Asset(atlas, "board");
		numbers = new Array<Asset>();
		numbers.add(new Asset(atlas,"1"));
		numbers.add(new Asset(atlas,"2"));
		numbers.add(new Asset(atlas,"3"));
		numbers.add(new Asset(atlas,"4"));
		numbers.add(new Asset(atlas,"5"));
		numbers.add(new Asset(atlas,"6"));
		numbers.add(new Asset(atlas,"7"));
		numbers.add(new Asset(atlas,"8"));
		numbers.add(new Asset(atlas,"9"));

		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	public class AssetSounds {

		public final Sound first;
		public final Sound second;
		public final Sound win;
		public final Sound draw;

		public AssetSounds(AssetManager am) {
			first = am.get("../android/assets/sounds/first.wav", Sound.class);
			second = am.get("../android/assets/sounds/second.wav", Sound.class);
			win = am.get("../android/assets/sounds/win.wav", Sound.class);
			draw = am.get("../android/assets/sounds/draw.wav", Sound.class);
		}
	}

	public class AssetMusic {
		public final Music song01;

		public AssetMusic(AssetManager am) {
			song01 = am.get("../android/assets/music/keith303_-_brand_new_highscore.mp3", Music.class);
		}
	}
}