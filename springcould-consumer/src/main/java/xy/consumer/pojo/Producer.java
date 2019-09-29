package xy.consumer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xy
 * @date 2019/8/20 13:16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producer implements Serializable {
    private Long id;
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
