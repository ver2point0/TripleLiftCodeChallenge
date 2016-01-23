## TripleLiftCodeChallenge

Our server has a URL endpoint that will return the performance (number of clicks and impressions) of a given advertiser over the past week (see JSON example below). The problem is that this server is on a machine that we lost access to and is running old code that we can't access. All we know is that it will sometimes take a long time to return the result.

Your challenge is to write a method that will take a list of advertiser_ids and aggregate the number of clicks and impressions they received by date.

Since this data is used for our dashboards and due to the random timeouts of our server, you need to make sure that the entire series of HTTP requests completes within 200ms. If you're not able to get the data for a particular advertiser within this time, you should flag note that the aggregated result does not include data for advertiser. If you're comfortable with Java, we prefer you use it. Otherwise, use a language you're familiar with.

Sample JSON returned when calling http://dan.triplelift.net/code_test.php?advertiser_id=123:

```
[
  {"advertiser_id" : 123, "ymd" : "2014-09-20", "num_clicks" : 24, "num_impressions" : 1000},
  {"advertiser_id" : 123, "ymd" : "2014-09-21", "num_clicks" : 20, "num_impressions" : 1010},
  {"advertiser_id" : 123, "ymd" : "2014-09-22", "num_clicks" : 10, "num_impressions" : 1210},
  {"advertiser_id" : 123, "ymd" : "2014-09-23", "num_clicks" : 22, "num_impressions" : 1110},
  {"advertiser_id" : 123, "ymd" : "2014-09-24", "num_clicks" : 25, "num_impressions" : 1710},
  {"advertiser_id" : 123, "ymd" : "2014-09-25", "num_clicks" : 31, "num_impressions" : 1020},
  {"advertiser_id" : 123, "ymd" : "2014-09-26", "num_clicks" : 50, "num_impressions" : 2000}
]
```

A sample call to your method:

```RetrievedData data = yourMethod(new long[]{123, 456, 789});```


