package shashank.grimreaper.movierating;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Shashank on 20-06-2016.
 */
public class Details extends Activity {
    String name,title,rating,posterUrl,response;
    TextView tvname,tvrating;
    ImageView ivposter;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        name = name.replaceAll(" ","+");
        new FetchMovieRating(this).execute();
        tvname = (TextView)findViewById(R.id.tvname);
        tvrating = (TextView)findViewById(R.id.tvrating);
        ivposter = (ImageView)findViewById(R.id.ivposter);
    }
    private class FetchMovieRating extends AsyncTask<Void, Void, String> {
        Context context;
        public FetchMovieRating(Context mContext) {
            this.context = mContext;
        }
        private ProgressDialog progressDialog = new ProgressDialog(Details.this);
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.getProgress();
            progressDialog.setIcon(R.mipmap.ic_launcher);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    FetchMovieRating.this.cancel(true);
                }
            });
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String MovieInfo = null;
            try {
                URL url = new URL("http://www.omdbapi.com/?t="+name+"&y=&plot=short&r=json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONObject obj = new JSONObject(String.valueOf(buffer));
                response = obj.getString("Response");
                if(response.equalsIgnoreCase("False")){
                    return "no such movie";
                }
                title = obj.getString("Title");
                rating = obj.getString("imdbRating");
                posterUrl  = obj.getString("Poster");
                //posterUrl = "http://cdn.mos.cms.futurecdn.net/b4f95fb08b0173b8f6b8469b22a9da20.jpg";
                System.out.println(posterUrl);
                try{
                    url = new URL(posterUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream2 = urlConnection.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(inputStream2);
                    bitmap = BitmapFactory.decodeStream(inputStream2);
                    //Toast.makeText(Details.this,"No error in fetching poster",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    //Toast.makeText(Details.this,"Error in fetching poster",Toast.LENGTH_LONG).show();
                }
                MovieInfo = buffer.toString();
                return MovieInfo;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                      //  Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return MovieInfo;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            this.progressDialog.dismiss();
            if(s.equalsIgnoreCase("no such movie")){
                Toast.makeText(Details.this,"Sorry, No such movie is present in Database.",Toast.LENGTH_SHORT).show();
                finish();
                return ;
            }
            tvname.setText(title);
            tvrating.setText(rating);
            ivposter.setImageBitmap(bitmap);
           // Log.i("json", s);
        }
        
        //All log statements removed jff
    }
}
/*

Cannot see the poster in college as the link is blocked by cyberoam

 */
