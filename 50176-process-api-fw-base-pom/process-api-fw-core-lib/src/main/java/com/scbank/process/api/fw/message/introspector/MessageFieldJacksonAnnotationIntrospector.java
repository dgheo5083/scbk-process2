package com.scbank.process.api.fw.message.introspector;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.scbank.process.api.fw.message.annotation.MessageField;

public class MessageFieldJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    @Override
    public PropertyName findNameForSerialization(Annotated a) {
        MessageField ann = this._findAnnotation(a, MessageField.class);
        if (ann != null && !ann.id().isEmpty()) {
            return PropertyName.construct(ann.id());
        }
        return super.findNameForSerialization(a);
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        MessageField ann = this._findAnnotation(a, MessageField.class);
        if (ann != null && !ann.id().isEmpty()) {
            return PropertyName.construct(ann.id());
        }
        return super.findNameForDeserialization(a);
    }
}
