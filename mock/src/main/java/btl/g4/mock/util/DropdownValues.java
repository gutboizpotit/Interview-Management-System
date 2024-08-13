package btl.g4.mock.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class DropdownValues {

    @Value("#{'${contractTypes}'.split(', ')}")
    private List<String> contractTypes;

    @Value("#{'${highestLevels}'.split(', ')}")
    private List<String> highestLevels;

    @Value("#{'${levels}'.split(', ')}")
    private List<String> levels;

    @Value("#{'${departments}'.split(', ')}")
    private List<String> departments;

    @Value("#{'${positions}'.split(', ')}")
    private List<String> positions;

    @Value("#{'${skills}'.split(', ')}")
    private List<String> skills;

    @Value("#{'${benefits}'.split(', ')}")
    private List<String> benefits;

    private final List<String> offerStatus = List.of("Waiting for Approval", "Approved", "Rejected", "Waiting for Response", "Accepted offer", "Declined offer", "Cancelled");
}
