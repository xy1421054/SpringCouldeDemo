package xy.producer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xy
 * @date 2019/8/20 13:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producer implements Serializable {
    private int id;
    private Long time;
    private String context;


    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", time=" + time +
                ", context='" + context + '\'' +
                '}';
    }
}
