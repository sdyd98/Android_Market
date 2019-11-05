package com.example.myapplication;

import com.kakao.message.template.LinkObject;
import com.kakao.message.template.MessageTemplateProtocol;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing clickable buttons in message template v2.
 *
 * @author kevin.kang. Created on 2017. 3. 10..
 */

public class ButtonObject {
    private final String title;
    private final LinkObject link;

    public ButtonObject(final String title, final LinkObject link) {
        this.title = title;
        this.link = link;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageTemplateProtocol.TITLE, title);
        jsonObject.put(MessageTemplateProtocol.LINK, link.toJSONObject());
        return jsonObject;
    }

    public String getTitle() {
        return title;
    }

    public LinkObject getLink() {
        return link;
    }
}