package mypack;

import java.lang.reflect.Array;

public class JSON {
    private static String $s = "\"";
    protected String json = "{";

    public JSON(JSON json) {
        this.json = json.json;
    }

    public JSON() {
        this.json = "{";
    }

    public String getJson() {
        return json;
    }

    //TODO: FIX A BUG WHEN YOU USE THE JSON CONSTRUCTOR.
    public void addAttribute(String attribute, Object value) {
        StringBuilder concat = new StringBuilder();

        if (isArray(value)) {
            int length = Array.getLength(value);
            Object[] list = new Object[length];
            for (int i = 0; i < length; i++) list[i] = Array.get(value, i);
            addAttribute(attribute, list);
        } else {
            concat.append(json);
            concat.append($s);
            concat.append(attribute);
            concat.append($s);
            concat.append(": ");
            json = concat.toString();

            if (value instanceof JSON) {
                concat.append(((JSON) value).json);
                concat.append(", ");
                json = concat.toString();
            } else if (value instanceof String) {
                concat.append($s);
                concat.append(value);
                concat.append($s);
                concat.append(", ");
                json = concat.toString();
            } else {
                concat.append(value);
                concat.append(", ");
                json = concat.toString();
            }
        }
    }

    public void addAttribute(String attribute, Object[] value) {
        StringBuilder concat = new StringBuilder();
        concat.append(json);
        concat.append($s);
        concat.append(attribute);
        concat.append($s);
        concat.append(":[");
        json = concat.toString();

        for (int i = 0; i < value.length; i++) {
            if (value[i] instanceof JSON) {
                concat.append(((JSON) value[i]).json);
                concat.append(", ");
                json = concat.toString();
            } else if (value[i] instanceof String) {
                concat.append($s);
                concat.append(value[i]);
                concat.append($s);
                concat.append(", ");
                json = concat.toString();
            } else {
                concat.append(value[i]);
                concat.append(", ");
                json = concat.toString();
            }
        }
        if (value.length > 0) {
            concat.delete(0, concat.length());
            concat.append(json.substring(0, json.length() - 2));
            concat.append("], ");
            json = concat.toString();
        } else {
            concat.append("], ");
            json = concat.toString();
        }

    }

    public void build() {
        StringBuilder concat = new StringBuilder();
        concat.append(json.substring(0, json.length() - 2));
        concat.append("}");
        json = concat.toString();
    }

    //To solve a obsolete, prehistoric and primitive problem.
    private boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }
}

