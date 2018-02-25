package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is the model for a message, which
 * stores the basic information such as the speaker,
 * the content, and the time, with all sorts of helpers
 *
 * Created by ray on 2018/2/25.
 */
public class Message {

    private StringBuilder content;
    private Member speaker;
    private String createdTime;

    public Message(String content, Member speaker) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        createdTime = sdf.format(cal.getTime());
        this.content = new StringBuilder(content);
        this.speaker = speaker;
    }

    public String toJSONSting() throws JSONException {
        JSONObject result = new JSONObject()
                .put("content", this.content.toString())
                .put("speaker", this.speaker.toString())
                .put("createdTime", this.createdTime);

        return result.toString();
    }

    public void updateContent(String newContent) {
        content.replace(0, content.length(),newContent);
    }

    public static Message parseJSONToMessage(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        Member speaker = Member.getMember(obj.getString("speaker"));
        Message newMessage = new Message(obj.getString("content"), speaker);

        return newMessage;
    }
}
