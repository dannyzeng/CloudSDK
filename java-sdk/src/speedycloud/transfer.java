package speedycloud;

/*
 *         bucket: Ŀ��Ͱ
        host: Ŀ��Ͱ��host
        resolutions: �ֱ��ʣ�����á������ָ������磺480P1,480P2,720P
        callback_url: �ص�url
        source_id: �ϸ��ӿڷ��ص�source_id
 * */

public class transfer {
	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getResolutions() {
		return resolutions;
	}

	public void setResolutions(String resolutions) {
		this.resolutions = resolutions;
	}

	public String getCallback_url() {
		return callback_url;
	}

	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	private String bucket;
	private String host;
	private String resolutions;
	private String callback_url;
	private String source_id;

}