package tsvetkoff.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * @author SweetSupremum
 */
public class GraphsSerializer extends JsonSerializer<GraphNameDtoCollectionWrapper> {
    @Override
    public void serialize(GraphNameDtoCollectionWrapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (Pair<String, GraphNameDto> entry : value.getGraphs()) {
            gen.writeFieldName(entry.getFirst());
            gen.writeObject(entry.getSecond());
        }
        gen.writeEndObject();

    }
}

