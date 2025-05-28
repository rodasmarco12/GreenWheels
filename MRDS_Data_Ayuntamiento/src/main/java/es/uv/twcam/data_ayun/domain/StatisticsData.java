package es.uv.twcam.data_ayun.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "statistics_data")
public class StatisticsData {

    @Id
    private String id;
    private Instant timeStamp;
    private List<AggregatedData> aggregatedData;

    public StatisticsData() {}

    public StatisticsData(String id, Instant timeStamp, List<AggregatedData> aggregatedData) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.aggregatedData = aggregatedData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<AggregatedData> getAggregatedData() {
        return aggregatedData;
    }

    public void setAggregatedData(List<AggregatedData> aggregatedData) {
        this.aggregatedData = aggregatedData;
    }
}

