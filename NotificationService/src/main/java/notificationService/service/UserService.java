package notificationService.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a list of all user email addresses from the database.
     *
     * <p>This method executes a SQL query to select the email addresses from the
     * "users" table and returns them as a list of strings.</p>
     *
     * @return a list of email addresses for all users in the database
     */
    public List<String> getAllUserEmails() {
        String sql = "SELECT email FROM users";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
