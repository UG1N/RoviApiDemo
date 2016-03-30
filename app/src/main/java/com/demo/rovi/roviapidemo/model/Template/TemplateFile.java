package com.demo.rovi.roviapidemo.model.Template;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TemplateFile implements Serializable {
    @SerializedName("templates")
    private Template mTemplate;

    public Template getTemplate() {
        return mTemplate;
    }

    @Override
    public String toString() {
        return "TemplateFile{" +
                "mTemplate=" + mTemplate +
                '}';
    }

}
