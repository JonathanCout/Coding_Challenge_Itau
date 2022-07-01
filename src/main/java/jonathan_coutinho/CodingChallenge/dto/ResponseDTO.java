package jonathan_coutinho.CodingChallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private Status status;
    private Map data;
    private String message;


    public ResponseDTO(Status failed, String message) {
        this.status = failed;
        this.data = Map.of();
        this.message = message;
    }

    public enum Status{
        FAILED,
        SUCCESS
    }
}
