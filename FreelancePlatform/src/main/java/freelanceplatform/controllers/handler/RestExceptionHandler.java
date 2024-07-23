package freelanceplatform.controllers.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import freelanceplatform.model.User;
import freelanceplatform.model.security.UserDetails;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class RestExceptionHandler {
    private static ObjectMapper objectMapper;

    /**
     * Gets a Jackson object mapper for mapping JSON to Java and vice versa.
     *
     * @return Object mapper
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return objectMapper;
    }

    /**
     * Creates a default HTTP message converter using Jackson for JSON conversion.
     *
     * @return a {@link MappingJackson2HttpMessageConverter} instance
     */
    public static HttpMessageConverter<?> createDefaultMessageConverter() {
        return new MappingJackson2HttpMessageConverter(getObjectMapper());
    }

    /**
     * Creates an HTTP message converter for encoding strings with UTF-8.
     *
     * @return a {@link StringHttpMessageConverter} instance with UTF-8 encoding
     */
    public static HttpMessageConverter<?> createStringEncodingMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    /**
     * Initializes security context with the specified user.
     *
     * @param user User to set as currently authenticated
     */
    public static SecurityContext setCurrentUser(User user) {
        final UserDetails userDetails = new UserDetails(user, new HashSet<>());
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(context);
        return context;
    }

    /**
     * Clears the current security context.
     */
    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

}
