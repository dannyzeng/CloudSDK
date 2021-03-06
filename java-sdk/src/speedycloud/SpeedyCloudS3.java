package speedycloud;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
public class SpeedyCloudS3 extends AbstractS3API {
    private String host;
    private String access_key;
    private String secret_key;

    public SpeedyCloudS3(String host,String access_key, String secret_key) {
        super(host,access_key, secret_key);
    	this.host = host;
        this.access_key = access_key;
        this.secret_key = secret_key;
    }

    public String list(String bucket) {
        return this.request("GET", "/" + bucket);
    }

    public String createBucket(String bucket) {
        return this.request("PUT", "/" + bucket);
    }

    public String deleteBucket(String bucket) {
        return this.request("DELETE", "/" + bucket);
    }

    public String queryBucketAcl(String bucket) {
        return this.request("GET", String.format("/%s?acl", bucket));
    }

    public String queryObjectAcl(String bucket, String key) {
        return this.request("GET", String.format("/%s/%s?acl", bucket, key));
    }

    public String deleteKey(String bucket, String key) {
        return this.request("DELETE", String.format("/%s/%s", bucket, key));
    }

    public String deleteVersioningKey(String bucket, String key, String versionId) {
        return this.request("DELETE", String.format("/%s/%s?versionId=%s", bucket, key, versionId));
    }

    public String getKeyVersions(String bucket) {
        return this.request("GET", String.format("/%s?versions", bucket));
    }

    public String configureBucketVersioning(String bucket, String status) {
        String path = bucket + "?versioning";
        String versioningBody = String.format("<?xml version=\"1.0\"encoding=\"UTF-8\"?><VersioningConfiguration xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Status>%s</Status ></VersioningConfiguration>", status);
        return this.putString("PUT", path, versioningBody);
    }

    public String getBucketVersioningStatus(String bucket) {
        return this.request("GET", String.format("/%s?versioning", bucket));
    }

    public String putObjectFromFile(String bucket, String key, String path) {
        return this.putKeyFromFile("PUT", String.format("/%s/%s", bucket, key), path);
    }

    public String putObjectFromString(String bucket, String key, String s) {
        return this.putKeyFromString("PUT", String.format("/%s/%s", bucket, key), s);
    }

    public String updateBucketAcl(String bucket, String acl) {
        return this.requestUpdate("PUT", String.format("/%s?acl", bucket), acl);
    }

    public String updateKeyAcl(String bucket, String key, String acl) {
        return this.requestUpdate("PUT", String.format("/%s/%s?acl", bucket, key), acl);
    }

    public String updateVersioningKeyAcl(String bucket, String key, String versionId, String acl) {
        return this.requestUpdate("PUT", String.format("/%s/%s?acl&versionId=%s", bucket, key, versionId), acl);
    }
    /*wangjiyou*/
    public int IsExsit(String method,String bucket, String key) {
        return this.requestIsExsit(method, String.format("/%s/%s", bucket, key));
    }
    
/*
 *     参数：
        url: 已上传到对象存储的对象的ur了（必填）
        address: 房源的地址
        bucket: 目标桶
        host: 目标桶的host
    	返回值：
        200： {"source_id": "404dbfe2-d66a-11e7-a00b-000c29b58aad", "status": "Success"}

 * */
    
    public String InitMysql(String url,String address, String bucket,String host,String accesskey,String secretkey) throws JSONException {
        JSONObject jsonObject1 = new JSONObject();  
        jsonObject1.put("url", url);  
        jsonObject1.put("address", address);
        jsonObject1.put("bucket", bucket);
        jsonObject1.put("host", host);
        jsonObject1.put("access_key", accesskey);
        jsonObject1.put("secret_key", secretkey);
        System.out.println(jsonObject1.toString());  
        return this.requestInitMysql("POST", jsonObject1.toString());
    }
    /*
     * transcode
     * 
     * init: 初始化mysql api的返回值
        bucket: 目标桶
        host: 目标桶的host
        resolutions: 分辨率，多个用“，”分隔，例如：480P1,480P2,720P
        callback_url: 回调url
        //source_id: 上个接口返回的source_id
     * 
     * */
    public String Transcode(String initresult,String bucket,String host,String resolutions,String callback_url,String accesskey,String secretkey,String watermark_path,String watermark_position) throws JSONException {
    	JSONObject jsonObject = new JSONObject(initresult);
    	
        JSONObject jsonObject1 = new JSONObject();  
        jsonObject1.put("bucket", bucket);  
        jsonObject1.put("host", host);
        jsonObject1.put("resolutions", resolutions);
        jsonObject1.put("callback_url", callback_url);
        jsonObject1.put("source_id", jsonObject.getString("source_id"));
        jsonObject1.put("access_key", accesskey);
        jsonObject1.put("secret_key", secretkey);
        jsonObject1.put("watermark_path", watermark_path);
        jsonObject1.put("watermark_position", watermark_position);
        System.out.println(jsonObject1.toString());  
        return this.requestInitMysql("POST", jsonObject1.toString());
    }
    
    public String PutTranscode(String bucket, String key, String path,String houseaddress,String resolutions,String callback_url,String watermark_path,String watermark_position) throws Exception {
    	String code = putObjectFromFile(bucket,key,path);
        JSONObject jsonObject1 = new JSONObject();  
        jsonObject1.put("source_id", "");  
        jsonObject1.put("status", "error");
    	System.out.println("put file "+code);
    	if(code.equals("200")) {
    		String url= host+"/"+bucket+"/"+key;
    		String initmysqlhost = host.substring("http://".length());
    		//String msg = "url:"+url+" houseaddress:"+houseaddress+" bucket:"+bucket+" initmysqlhost:"+initmysqlhost;
    		//System.out.println(msg);
    		String init = InitMysqlApi(url,houseaddress,bucket,initmysqlhost,access_key,secret_key);
    		System.out.println(init);
    		JSONObject jsonObject = new JSONObject(init);
    		String status = jsonObject.getString("status");
    		if(status.equals("Success")) {
    			String ret = Transfer(init,bucket,initmysqlhost,resolutions,callback_url,watermark_path,watermark_position);
    			if(ret.equals("success")) {
    				return init;
    			}else {
    				return jsonObject1.toString();
    			}
    		}else {
    			return jsonObject1.toString();
    		}
    	}else {
    		return jsonObject1.toString();
    	}    	
    }
    
    public String InitMysqlApi(String url,String houseaddress,String bucket,String host,String accesskey,String secretkey) throws Exception {
        video_source sqlapi = new video_source(host, url,houseaddress,  bucket, accesskey,secretkey);
        String init = sqlapi.InitMysqlApi("http://106.2.24.17:8000/video_source");
		return init;
    }

    
    public String Transfer(String init,String bucket,String host,String resolutions,String callback_url,String watermark_path,String watermark_position ) throws Exception {
    	//String initresult,String bucket,String host,String resolutions,String callback_url,String accesskey,String secretkey
    	transfer trans = new transfer(init,bucket,host,resolutions,callback_url,access_key,secret_key);
    	String ret = trans.TransferApi("http://106.2.24.17:8000/transcode",watermark_path,watermark_position);
    	return ret;
    }
}
