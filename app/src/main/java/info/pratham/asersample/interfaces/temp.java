package info.pratham.asersample.interfaces;

import com.google.gson.annotations.SerializedName;

public class temp {
    @SerializedName("Scored Labels")
    private String ScoredLabels;

    private String Distance;

    @SerializedName("Scored Probabilities")
    private String ScoredProbabilities;

    public temp(String scoredLabels, String distance, String scoredProbabilities) {
        ScoredLabels = scoredLabels;
        Distance = distance;
        ScoredProbabilities = scoredProbabilities;
    }

    public String getScoredLabels() {
        return ScoredLabels;
    }

    public void setScoredLabels(String scoredLabels) {
        ScoredLabels = scoredLabels;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getScoredProbabilities() {
        return ScoredProbabilities;
    }

    public void setScoredProbabilities(String scoredProbabilities) {
        ScoredProbabilities = scoredProbabilities;
    }
}
