package com.freedom.fbvideo;
import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.view.*;import android.widget.*;import java.io.File;import com.yausername.youtubedl_android.YoutubeDL;import com.yausername.youtubedl_android.YoutubeDLRequest;
public class MainActivity extends Activity {
 EditText url; TextView status; ProgressBar bar; Button download;
 void message(String s){runOnUiThread(()->status.setText(s));}
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(10,15,30)); LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(35,60,35,25);root.setBackgroundColor(Color.rgb(13,19,35));setContentView(root);
 TextView title=new TextView(this);title.setText("Freedom FB Video");title.setTextColor(-1);title.setTextSize(28);root.addView(title);
 TextView sub=new TextView(this);sub.setText("Scarica video pubblici e Reel autorizzati");sub.setTextColor(0xffa9b8d0);sub.setPadding(0,10,0,40);root.addView(sub);
 url=new EditText(this);url.setSingleLine();url.setHint("Incolla link Facebook / Reel");url.setTextColor(-1);url.setHintTextColor(0xff94a3b8);root.addView(url);
 Button paste=new Button(this);paste.setText("INCOLLA LINK");root.addView(paste);paste.setOnClickListener(v->{android.content.ClipboardManager c=(android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);if(c.hasPrimaryClip())url.setText(c.getPrimaryClip().getItemAt(0).coerceToText(this));});
 download=new Button(this);download.setText("SCARICA VIDEO MP4");root.addView(download);
 bar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);bar.setIndeterminate(true);bar.setVisibility(View.GONE);root.addView(bar);
 status=new TextView(this);status.setTextColor(-1);status.setPadding(0,30,0,0);status.setText("Pronto");root.addView(status);
 if(getIntent()!=null&&Intent.ACTION_SEND.equals(getIntent().getAction())){String s=getIntent().getStringExtra(Intent.EXTRA_TEXT);if(s!=null)url.setText(s);}
 download.setOnClickListener(v->startDownload()); }
 void startDownload(){String link=url.getText().toString().trim();if(!link.matches("https?://.*")){message("Inserisci un URL valido.");return;}if(!(link.contains("facebook.com")||link.contains("fb.watch"))){message("Inserisci un link Facebook.");return;}download.setEnabled(false);bar.setVisibility(View.VISIBLE);message("Preparazione download...");new Thread(()->{try{YoutubeDL.getInstance().init(getApplicationContext());File folder=new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),"Video");folder.mkdirs();YoutubeDLRequest req=new YoutubeDLRequest(link);req.addOption("-o",new File(folder,"%(title).80s-%(id)s.%(ext)s").getAbsolutePath());req.addOption("-f","best[ext=mp4]/best");req.addOption("--no-playlist");YoutubeDL.getInstance().execute(req,(progress,eta,line)->runOnUiThread(()->{bar.setIndeterminate(false);bar.setProgress((int)progress);status.setText("Download: "+(int)progress+"% • "+line);}));message("Completato. Video in: "+folder.getAbsolutePath());}catch(Exception e){message("Download non riuscito: "+e.getMessage());}finally{runOnUiThread(()->{download.setEnabled(true);bar.setVisibility(View.GONE);});}}).start();}
}
