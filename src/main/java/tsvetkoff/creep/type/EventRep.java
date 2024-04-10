package tsvetkoff.creep.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author SweetSupremum
 */
public interface EventRep extends JpaRepository<Event, Long> {
    List<Event> findByTitleOrderByEventDateTime(String title);
}
