package duckie.example.backend.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record  ApiError(
    String error,
    String message,
    List<FieldErrorDetail> detail,
    String path,
    Instant timestamp
) {
    public record FieldErrorDetail(String field,String message){
    }
}