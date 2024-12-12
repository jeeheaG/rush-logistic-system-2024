package com.rush.logistic.client.hub.model;

import com.rush.logistic.client.hub.dto.HubPointRequestDto;
import com.rush.logistic.client.hub.dto.TimeTakenAndDistDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_hub_route")
public class HubRoute extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "hub_route_id", updatable = false, nullable = false)
    private UUID hubRouteId;
    @Column(name = "start_hub_id", nullable = false)
    private UUID startHubId;
    @Column(name = "end_hub_id", nullable = false)
    private UUID endHubId;
    @Column(name = "time_taken", nullable = false)
    private String timeTaken;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "milliseconds", nullable = false)
    private String milliseconds;

    public static String formatDuration(Duration duration) {
        long totalMinutes = duration.toMinutes();
        long days = totalMinutes / (24 * 60);
        long hours = (totalMinutes % (24 * 60)) / 60;
        long minutes = totalMinutes % 60;

        return String.format("%dD %dH %dM", days, hours, minutes);
    }

    public static HubRoute from(HubPointRequestDto requestDto, Duration timeTaken, int distance, String milliseconds) {
        String timeTakenString = formatDuration(timeTaken);

        HubRoute hubRoute = HubRoute.builder()
                .startHubId(requestDto.getStartHubId())
                .endHubId(requestDto.getEndHubId())
                .timeTaken(timeTakenString)
                .distance(distance)
                .milliseconds(milliseconds)
                .build();

        hubRoute.setDelete(false);

        return hubRoute;
    }

    public void update(TimeTakenAndDistDto timeTakenAndDistDto, Duration timeTaken) {
        String timeTakenString = formatDuration(timeTaken);

        this.timeTaken = timeTakenString;
        this.distance = Integer.parseInt(timeTakenAndDistDto.getDistance());
        this.milliseconds = timeTakenAndDistDto.getTimeTaken();
    }
}
