package com.ecotech.plantae.alert.interfaces.rest.resources;

import java.time.Instant;

public record AlertResource(
        String id,
        String plantId,
        String plantName,
        String plantSpecies,
        String sensorId,
        String sensorType,
        String type,
        String status,
        String message,
        Instant occurredAt,
        Instant resolvedAt,
        Double value,
        String metric) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String plantId;
        private String plantName;
        private String plantSpecies;
        private String sensorId;
        private String sensorType;
        private String type;
        private String status;
        private String message;
        private Instant occurredAt;
        private Instant resolvedAt;
        private Double value;
        private String metric;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder plantId(String plantId) {
            this.plantId = plantId;
            return this;
        }

        public Builder plantName(String plantName) {
            this.plantName = plantName;
            return this;
        }

        public Builder plantSpecies(String plantSpecies) {
            this.plantSpecies = plantSpecies;
            return this;
        }

        public Builder sensorId(String sensorId) {
            this.sensorId = sensorId;
            return this;
        }

        public Builder sensorType(String sensorType) {
            this.sensorType = sensorType;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder occurredAt(Instant occurredAt) {
            this.occurredAt = occurredAt;
            return this;
        }

        public Builder resolvedAt(Instant resolvedAt) {
            this.resolvedAt = resolvedAt;
            return this;
        }

        public Builder value(Double value) {
            this.value = value;
            return this;
        }

        public Builder metric(String metric) {
            this.metric = metric;
            return this;
        }

        public AlertResource build() {
            return new AlertResource(
                    id, plantId, plantName, plantSpecies, sensorId, sensorType,
                    type, status, message, occurredAt, resolvedAt, value, metric);
        }
    }
}
