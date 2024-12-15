package com.rush.logistic.client.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDto implements Comparable<EdgeDto> {
    private int to;
    private int distance;
    private int milliseconds;

    @Override
    public int compareTo(EdgeDto o) {
        int comp = Integer.compare(this.milliseconds, o.milliseconds);
        if (comp == 0) {
            comp = Integer.compare(this.distance, o.distance);
        }
        return comp;
    }
}
