package tsvetkoff.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author SweetSupremum
 */
public class GraphDtoSerializer extends JsonSerializer<GraphDto> {
    @Override
    public void serialize(GraphDto value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("abscissa", value.getAbscissa());
        value.getOrdinates().forEach(coordinate -> {
            try {
                gen.writeNumberField(coordinate.getFirst(), coordinate.getSecond());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        gen.writeEndObject();
    }
}
