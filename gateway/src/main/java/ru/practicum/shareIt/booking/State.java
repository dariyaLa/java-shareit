package ru.practicum.shareIt.booking;

public enum State {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    CURRENT,
    PAST,
    FUTURE,
    ALL;

    public static State getStateUser(Boolean state) {
        if (state) {
            return APPROVED;
        } else {
            return REJECTED;
        }
    }

    public static void exeptionState(String state) {
        throw new ExeptionState("Unknown state: " + state);
    }

    public static class ExeptionState extends RuntimeException {

        public ExeptionState(String message) {
            super(message);
        }

    }

}
