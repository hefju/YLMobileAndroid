package YLWebService;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-01-27.
 */
public class YLWebDataabstract extends AsyncTask<String,Integer,String> {
    private JSONObject jsonObject;
    private String YLUrl;

    public YLWebDataabstract (JSONObject jsonObject,String YLUrl){
        this.jsonObject = jsonObject;
        this.YLUrl = YLUrl;
    }

    @Override
    public String doInBackground(String... strings) {
        HttpPost post = new HttpPost(YLUrl);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
        HttpConnectionParams.setSoTimeout(httpParams, 2000);
        try {
            post.setEntity(new StringEntity(jsonObject.toString(),"UTF-8"));
            post.setHeader(HTTP.CONTENT_TYPE,"text/json");
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200){
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
