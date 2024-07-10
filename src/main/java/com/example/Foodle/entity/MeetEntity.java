package com.example.Foodle.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Transient;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String mid;
    private String uid;
    
    @Column(length = 100, nullable = false)
    private String name;
    private String date;

    @ElementCollection
    @CollectionTable(name = "member", joinColumns = @JoinColumn(name = "mid"))
    @Column(name = "member_id") // or any appropriate column name
    private List<String> member;

    // @ElementCollection
    // @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "mid"))
    // @Column(name = "place_id") // or any appropriate column name
    // private List<Integer> placeList;

    // Firestore에서 가져온 데이터를 매핑할 리스트
    @ElementCollection
    @CollectionTable(name = "placeList", joinColumns = @JoinColumn(name = "pid"))
    @MapKeyColumn(name = "place_id")
    @Column(name = "time")
    private List<Map<String, Object>> lists = new ArrayList<>();

    // placeList를 lists로 변환
    public void updatePlaceList(Map<String, Object> meetplace) {
        Map<String, Object> newPlace = (Map<String, Object>) meetplace.get("place");
        Object newPid = newPlace.get("pid");

        boolean updated = false;

        for (Map<String, Object> existingEntry : lists) {            
            Map<String, Object> existingPlace = new HashMap<>(existingEntry);
            String existingPlaceId = (String) existingEntry.get("place");

            if (newPid.equals(existingPlace)) {
                // 기존 엔트리를 업데이트
                existingPlace.put("place", existingPlaceId);
                existingPlace.put("time", meetplace.get("time"));
                updated = true;
                break;
            }
        }

        if (!updated) {
            // 동일한 pid를 가진 엔트리가 없으면 새로 추가
            lists.add(meetplace);
        }
    }

    // private Map<Integer, Date> placeList = new HashMap<>();
    
    // // Convert placeList keys to Integer type
    // public void convertPlaceListKeysToIntegers() {
    //     Map<Integer, Date> convertedPlaceList = new HashMap<>();
    //     for (Map.Entry<String, Date> entry : lists.entrySet()) {
    //         Integer key = Integer.parseInt(entry.getKey());
    //         convertedPlaceList.put(key, entry.getValue());
    //     }
    //     placeList = convertedPlaceList;
    // }
    
}
