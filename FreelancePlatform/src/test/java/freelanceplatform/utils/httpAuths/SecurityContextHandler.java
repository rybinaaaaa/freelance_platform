package freelanceplatform.utils.httpAuths;

import freelanceplatform.model.Role;
import freelanceplatform.model.User;
import freelanceplatform.services.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


//TODO
@Aspect
@Component
public class SecurityContextHandler {

    @Autowired
    private UserService userService;

    @Pointcut("within(freelanceplatform.controllers.*Test) && @annotation(freelanceplatform.utils.httpAuths.WithAuthenticatedAdmin)")
    public void isUserMockedAsAdmin() {
    }

    @Pointcut("within(freelanceplatform.controllers.*Test) && @annotation(freelanceplatform.utils.httpAuths.WithAuthenticatedUser)")
    public void isUserMockedAsUser() {
    }

    @Before("isUserMockedAsAdmin()")
    public void setAdminContext() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Optional<User> user = userService.findById(2);

        if (user.isPresent()) {
            TestingAuthenticationToken token = new TestingAuthenticationToken(user, user.get().getPassword(), user.get().getRole().name());
            context.setAuthentication(token);
            SecurityContextHolder.setContext(context);
        }
    }

    @Before("isUserMockedAsUser()")
    public void setUserContext() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Optional<User> user = userService.findById(1);

        if (user.isPresent()) {
            TestingAuthenticationToken token = new TestingAuthenticationToken(user, user.get().getPassword(), user.get().getRole().name());
            context.setAuthentication(token);
            SecurityContextHolder.setContext(context);
        }
    }
}
