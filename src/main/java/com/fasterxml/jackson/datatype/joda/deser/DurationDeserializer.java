package com.fasterxml.jackson.datatype.joda.deser;

import java.io.IOException;

import org.joda.time.Duration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Deserializes a Duration from either an int number of millis or using the {@link Duration#Duration(Object)}
 * constructor on a JSON string. By default the only supported string format is that used by {@link
 * Duration#toString()}. (That format for a 3,248 millisecond duration is "PT3.248S".)
 */
public class DurationDeserializer extends JodaDeserializerBase<Duration>
{
    private static final long serialVersionUID = 1L;

    public DurationDeserializer() { super(Duration.class); }

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        switch (p.getCurrentTokenId()) {
        case JsonTokenId.ID_NUMBER_INT: // assume it's millisecond count
            return new Duration(p.getLongValue());
        case JsonTokenId.ID_STRING:
            return new Duration(p.getText().trim());
        default:
        }
        return _handleNotNumberOrString(p, ctxt);
    }

    protected Duration _deserialize(DeserializationContext ctxt, String str)
            throws IOException
    {
        if (str.length() == 0) {
            if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
                return null;
            }
        }
        return Duration.parse(str);
    }
}
