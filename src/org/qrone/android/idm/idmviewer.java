package org.qrone.android.idm;

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

    	ring = MediaPlayer.create(this, R.raw.ring);
    	newuser = MediaPlayer.create(this, R.raw.newuser);
        
    	tvRewrite(idm,pmm,systemCode);

    	/*
        Button button = (Button) findViewById(R.id.button01);
        button.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
				System.exit(RESULT_OK);
       		}
        });
        */
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
	
	public void tvRewrite(byte[] idm, byte[] pmm, byte[] systemCode) {

        wv = (WebView) this.findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient() {});
        
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

        wv.loadUrl("http://www.qrone.org/?idm=" + idmstr + "&pmm=" + pmmstr + "sys=" + sysstr);
        
        ring.start();
		
	}
}
