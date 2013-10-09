package json;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.Answers;
import service.Questions;

public class JSONFactory {
	private static JSONFactory factory = new JSONFactory();
	
	private JSONFactory(){}
	
	public static JSONFactory getInstance(){return factory;}
	
	public String createJSONQuestion(LinkedList<Questions> questions,int startIndex){
		try {
			JSONObject obj = new JSONObject();
			obj.put("searchcount", 0);
			obj.put("startindex", startIndex);
			JSONArray ques = new JSONArray();
			obj.put("ques", ques);
			for(int i=0;i<questions.size();i++){
				JSONObject jObj = new JSONObject();
				jObj.put("id", questions.get(i).key);
				jObj.put("question", questions.get(i).ques);
				ques.put(jObj);
				if(i==0){
					obj.put("searchcount", questions.get(i).count);
				}
			}
			return obj.toString();
		} catch (JSONException e) {
			return "JSON Error creating questions";
		}
	}

	public String createJSONAnswer(LinkedList<Answers> answers, int startIndex) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("searchcount", 0);
			obj.put("startindex", startIndex);
			JSONArray ans = new JSONArray();
			obj.put("answers", ans);
			for(int i=0;i<answers.size();i++){
				JSONObject jObj = new JSONObject();
				jObj.put("id", answers.get(i).id);
				jObj.put("answer", answers.get(i).content);
				jObj.put("lat", answers.get(i).lat);
				jObj.put("lng", answers.get(i).lng);
				ans.put(jObj);
				if(i==0){
					obj.put("searchcount", answers.get(i).count);
				}
			}
			return obj.toString();
		} catch (JSONException e) {
			return "JSON Error creating answers";
		}
	}
}
