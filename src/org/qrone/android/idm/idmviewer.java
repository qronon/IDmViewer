package org.qrone.android.idm;

import org.json.JSONException;
import org.json.JSONObject;

import net.kokozo.android.nfc.idmviewer.R;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class idmviewer extends Activity {
    private final int MENU_ID1 = Menu.FIRST;
    private static final String SIGN = "5753534944-71726F6E6F6E-136e30ec44b-3fe46d7775dda0f9-C5E5C3838400C50AF1F6BF443A69E90D";

    private WebView wv;
    private MediaPlayer ring;
    private MediaPlayer newuser;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	Intent intent = getIntent();
    	Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    	NfcF techF = NfcF.get(tag);
    	byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
    	byte[] pmm = techF.getManufacturer();
    	byte[] systemCode = techF.getSystemCode();

        StringBuffer idmstr = new StringBuffer();
        for(int i = 0; i < idm.length; i++) {
        	idmstr.append(String.format("%02X",idm[i]));
        }
        
        
        StringBuffer pmmstr = new StringBuffer();
        for(int i = 0; i < pmm.length; i++) {
        	pmmstr.append(String.format("%02X",pmm[i]));
        }
        
        StringBuffer sysstr = new StringBuffer();
        for(int i = 0; i < systemCode.length; i++) {
        	sysstr.append(String.format("%02X",systemCode[i]));
        }
        
    	ring = MediaPlayer.create(this, R.raw.ring);
    	newuser = MediaPlayer.create(this, R.raw.newuser);

        ring.start();

        wv = (WebView) this.findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient() {});
        

    	JSONDatastoreClient.Add add = new JSONDatastoreClient.Add(){
    		@Override
    		public void onPostExecute(JSONObject obj){
    			String html;
				try {
					JSONObject user = (JSONObject)obj.get("item");
					html = "<h2>Successfully registered.</h2>" +
    				"<h2>IDm:" + user.getString("idm") + "</h2>" +
    				"<h2>pmm:" + user.getString("pmm") + "</h2>";
					wv.loadData(html, "text/html", "utf8");
				} catch (JSONException e) {
					e.printStackTrace();
				}
    			
    		}
		};
		try {
			JSONObject obj = new JSONObject();
			obj.put("idm", idmstr);
			obj.put("pmm", pmmstr);
			obj.put("sys", sysstr);
			add.execute("idmlog", null, obj, SIGN);
			
		} catch (JSONException e) {}
		

    	/*
        Button button = (Button) findViewById(R.id.button01);
        button.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
				System.exit(RESULT_OK);
       		}
        });
        */

    	//tvRewrite(idm,pmm,systemCode);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);

		menu.add(0 , MENU_ID1 , Menu.NONE , "QUIT");
		return ret;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case MENU_ID1:
				System.exit(RESULT_OK);
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
