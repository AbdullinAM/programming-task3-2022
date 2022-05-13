package com.example.russianCheckers.logic;

import lombok.Data;


@Data
public class Checker {

    private Checker() {}

    private CheckerStatus checkerStatus;
    private boolean isSuperChecker;
    private String position;


    public static Builder newBuilder() {
        return new Checker().new Builder();
    }

    public class Builder {

        private Builder() {
            // private constructor
        }

        public Builder setCheckerStatus(CheckerStatus checkerStatus) {
            Checker.this.checkerStatus = checkerStatus;
            return this;
        }

        public Builder setIsSuperChecker(boolean isSuperChecker) {
            Checker.this.isSuperChecker = isSuperChecker;
            return this;
        }

        public Builder setPosition(String position) {
            Checker.this.position = position;
            return  this;
        }

        public Checker build() {
            return Checker.this;
        }

    }
}
