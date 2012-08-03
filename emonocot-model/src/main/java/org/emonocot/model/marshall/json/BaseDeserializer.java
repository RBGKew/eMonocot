package org.emonocot.model.marshall.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.emonocot.api.Service;
import org.emonocot.model.common.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class BaseDeserializer<T extends Base> extends
        JsonDeserializer<T> {
	
	private static Logger logger = LoggerFactory.getLogger(BaseDeserializer.class);

    /**
     *
     */
    protected Service<T> service;

    /**
     *
     */
    protected Class<T> type;

    /**
     *
     * @param newType Set the type
     */
    public BaseDeserializer(final Class<T> newType) {
        type = newType;
    }

    /**
     *
     * @param newService
     *            Set the service
     */
    public final void setService(final Service<T> newService) {
        this.service = newService;
    }

    @Override
    public T deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException {
        String identifier = jsonParser.getText();
        /**
         * Hack for now should allow client side to
         * set "return lazy initialized proxy objs"
         */
        if (service != null) {
        	logger.debug("service is not null, returning object");
            return service.load(identifier);
        } else {
            try {
            	logger.debug("service is null, returning new object");
                T t = type.newInstance();
                t.setIdentifier(identifier);
                return t;
            } catch (InstantiationException ie) {
                throw new JsonParseException(ie.getMessage(),
                        jsonParser.getCurrentLocation());
            } catch (IllegalAccessException iae) {
                throw new JsonParseException(iae.getMessage(),
                        jsonParser.getCurrentLocation());
            }
        }
    }
}
