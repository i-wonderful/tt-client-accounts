
package testtask.accounts.serializator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 27, 2018
 */
public class JsonBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private final NumberFormat format = NumberFormat.getNumberInstance();

    public JsonBigDecimalSerializer() {
         format.setGroupingUsed(false);
    }

    @Override
    public void serialize(BigDecimal val, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeNumber(format.format(val));
    }

}
