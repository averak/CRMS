package dev.abelab.crms.db.entity;

import java.util.Date;

/**
 * Reservation Sample Builder
 */
public class ReservationSample extends AbstractSample {

    public static ReservationSampleBuilder builder() {
        return new ReservationSampleBuilder();
    }

    public static class ReservationSampleBuilder {

        private Integer id = SAMPLE_INT;
        private Integer userId = SAMPLE_INT;
        private Date startAt = SAMPLE_DATE;
        private Date finishAt = SAMPLE_DATE;
        private Date createdAt = SAMPLE_DATE;
        private Date updatedAt = SAMPLE_DATE;

        public ReservationSampleBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public ReservationSampleBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public ReservationSampleBuilder startAt(Date startAt) {
            this.startAt = startAt;
            return this;
        }

        public ReservationSampleBuilder finishAt(Date finishAt) {
            this.finishAt = finishAt;
            return this;
        }

        public ReservationSampleBuilder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReservationSampleBuilder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Reservation build() {
            return Reservation.builder() //
                .id(this.id) //
                .userId(this.userId) //
                .startAt(this.startAt) //
                .finishAt(this.finishAt) //
                .createdAt(this.createdAt) //
                .updatedAt(this.updatedAt) //
                .build();
        }

    }

}
