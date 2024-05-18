package tsvetkoff.domain.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author SweetSupremum
 */
@Data
@Builder
@AllArgsConstructor
public class ExceptionDetailsDto {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime currentDate;

    public String caused;
}
