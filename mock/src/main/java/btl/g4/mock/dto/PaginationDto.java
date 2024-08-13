package btl.g4.mock.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationDto<T> {

    private List<T> data;

    private Integer page;

    private Long totalItem;
}
