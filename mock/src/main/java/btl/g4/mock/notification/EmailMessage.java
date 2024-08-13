package btl.g4.mock.notification;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailMessage {

    private String template;

    private String subject;

    private String sender;

    private String receiver;

    private Map<String, String> arguments;
}
