package com.rush.logistic.client.hub.model;

import com.rush.logistic.client.hub.dto.HubInfoRequestDto;
import com.rush.logistic.client.hub.dto.LatLonDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = "p_hub")
public class Hub extends BaseEntity{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "hub_id", updatable = false, nullable = false)
    private UUID hubId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "latitude", nullable = false)
    private double latitude;
    @Column(name = "longitude", nullable = false)
    private double longitude;

    public static Hub from(HubInfoRequestDto requestDto, LatLonDto latLonDto) {
        Hub hub = Hub.builder()
            .name(requestDto.getName())
            .address(requestDto.getAddress())
            .latitude(Double.parseDouble(latLonDto.getLatitude()))
            .longitude(Double.parseDouble(latLonDto.getLongitude()))
            .build();

        hub.setDelete(false);

        return hub;
    }

    public static Hub to(HubItem hubItem) {
        return Hub.builder()
            .hubId(UUID.fromString(hubItem.getHubId()))
            .name(hubItem.getName())
            .address(hubItem.getAddress())
            .latitude(hubItem.getLatitude())
            .longitude(hubItem.getLongitude())
            .build();
    }

    public void update(HubInfoRequestDto requestDto, LatLonDto latLonDto) {
        this.name = requestDto.getName();
        this.address = requestDto.getAddress();
        this.latitude = Double.parseDouble(latLonDto.getLatitude());
        this.longitude = Double.parseDouble(latLonDto.getLongitude());
    }

    public void delete() {
        LocalDateTime now = LocalDateTime.now();
        this.setDelete(true);
        this.setDeletedAt(now);
    }
}
