package org.okhttpLearn;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

public class UploadExample {
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	OkHttpClient client = new OkHttpClient();

	String post(String url, String json) throws IOException {
		MultipartBody.Builder builder = new MultipartBody.Builder("inputstream");
		String str = "test";
		byte[] bytes = str.getBytes();
		RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), ByteString.of(bytes));
		RequestBody requestBody = builder.setType(MultipartBody.FORM).addFormDataPart("detail_image", "yyp", fileBody).build();
		Request request = new Request.Builder().url("http://www.roundsapp.com/post").post(requestBody).build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	String bowlingJson(String player1, String player2) {
		return "{'winCondition':'HIGH_SCORE'," + "'name':'Bowling'," + "'round':4," + "'lastSaved':1367702411696,"
				+ "'dateStarted':1367702378785," + "'players':[" + "{'name':'" + player1
				+ "','history':[10,8,6,7,8],'color':-13388315,'total':39}," + "{'name':'" + player2
				+ "','history':[6,10,5,10,10],'color':-48060,'total':41}" + "]}";
	}

	public static void main(String[] args) throws IOException {
		UploadExample example = new UploadExample();
		String json = example.bowlingJson("Jesse", "Jake");
		String response = example.post("http://www.roundsapp.com/post", json);
		System.out.println(response);
	}
}