package com.demo.rovi.roviapidemo.model.templatefile;

import com.google.gson.annotations.SerializedName;

public class TemplateFile {

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
