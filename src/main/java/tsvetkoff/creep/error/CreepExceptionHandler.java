package tsvetkoff.creep.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tsvetkoff.domain.error.ExceptionDetailsDto;

import java.time.LocalDateTime;

/**
 * @author SweetSupremum
 */
@RestControllerAdvice
@Slf4j(topic = "CREEP_EXCEPTION_HANDLER")
public class CreepExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ExceptionDetailsDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(
                ExceptionDetailsDto.builder().caused(e.getMessage())
                        .currentDate(LocalDateTime.now()).build()
        );
    }
}
