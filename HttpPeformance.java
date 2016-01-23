package requestperformance;

/*
* OVERVIEW:
*
* Our server has a URL endpoint that will return the performance (number of clicks and impressions) of a given advertiser over the past week (see JSON example below).
* The problem is that this server is on a machine that we lost access to and is running old code that we can't access.
* All we know is that it will sometimes take a long time to return the result.
*
* CHALLENGE:
*
* Your challenge is to write a method that will take a list of advertiser_ids and aggregate the number of clicks and impressions they received by date.
*
* Since this data is used for our dashboards and due to the random timeouts of our server,
* you need to make sure that the entire series of HTTP requests completes within 200ms.
* If you're not able to get the data for a particular advertiser within this time,
* you should flag note that the aggregated result does not include data for advertiser.
* If you're comfortable with Java, we prefer you use it. Otherwise, use a language you're familiar with.

Sample JSON returned when calling http://dan.triplelift.net/code_test.php?advertiser_id=123:

[
  {"advertiser_id" : 123, "ymd" : "2014-09-20", "num_clicks" : 24, "num_impressions" : 1000},
  {"advertiser_id" : 123, "ymd" : "2014-09-21", "num_clicks" : 20, "num_impressions" : 1010},
  {"advertiser_id" : 123, "ymd" : "2014-09-22", "num_clicks" : 10, "num_impressions" : 1210},
  {"advertiser_id" : 123, "ymd" : "2014-09-23", "num_clicks" : 22, "num_impressions" : 1110},
  {"advertiser_id" : 123, "ymd" : "2014-09-24", "num_clicks" : 25, "num_impressions" : 1710},
  {"advertiser_id" : 123, "ymd" : "2014-09-25", "num_clicks" : 31, "num_impressions" : 1020},
  {"advertiser_id" : 123, "ymd" : "2014-09-26", "num_clicks" : 50, "num_impressions" : 2000}
]
A sample call to your method:

RetrievedData data = yourMethod(new long[]{123, 456, 789});
*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpPeformance {

  public static void main(String[] args) throws JSONException {

    JSONObject performanceData = adData(new long[]{123, 456, 789});
    System.out.println(performanceData);
  }

  public static JSONObject adData(long[] id) throws JSONException {

    StringBuilder stringBuilder = new StringBuilder();
    InputStreamReader inputStreamReader = null;

    for (long urlId : id) {
      URLConnection urlConnection;
      String webUrl = "http://dan.triplelift.net/code_test.php?advertiser_id=";
      webUrl = webUrl + urlId;

      try {
        // get url, make connection
        URL newUrl = new URL(webUrl);
        urlConnection = newUrl.openConnection();

        if (urlConnection != null) {
          urlConnection.setReadTimeout(200);
        }
        if (urlConnection != null && urlConnection.getInputStream() != null) {
          inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), Charset.defaultCharset());
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

          int dataInput;
          while ((dataInput = bufferedReader.read()) != -1) {
            stringBuilder.append((char) dataInput);
          }
          bufferedReader.close();
        }
        if (inputStreamReader != null) {
          inputStreamReader.close();
        }
      } catch (Exception e) {
        System.out.println("advertiser " + urlId + " was excluded");
      }
    }

    // put stringbuilder into a string
    String sbResult = stringBuilder.toString();
    sbResult = sbResult.replaceAll("\\]", ",");
    sbResult = sbResult.replaceAll("\\[", "");
    sbResult = "[" + sbResult + "]";

    // convert string to json array
    JSONArray array = new JSONArray(sbResult);
    JSONObject object;
    int numberClicks;
    int numberImpressions;

    HashMap<String, List<Integer>> clicksImpressions = new HashMap<String, List<Integer>>();

    // compute clicks and impressions according to date value
    for (int i = 0; i < array.length(); i++) {
      object = array.getJSONObject(i);
      String date = object.getString("ymd");
      List<Integer> aggregateCount = new ArrayList<Integer>();

      if (!clicksImpressions.containsKey(date)) {
        numberClicks = object.getInt("numberClicks");
        numberImpressions = object.getInt("numberImpressions");
      } else {
        aggregateCount = clicksImpressions.get(date);
        numberClicks = aggregateCount.get(0) + object.getInt("numberClicks");
        numberImpressions = aggregateCount.get(1) + object.getInt("numberImpressions");
      }

      aggregateCount.clear();
      aggregateCount.add(numberClicks);
      aggregateCount.add(numberImpressions);
      clicksImpressions.put(date, aggregateCount);
    }

    // save to JSON object
    return new JSONObject(clicksImpressions);
  }
}
