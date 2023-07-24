package ru.practicum.shareit.booking;

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
            return State.APPROVED;
        } else {
            return State.REJECTED;
        }
    }

    public static void exeptionState() {
        throw new ExeptionState("Unknown state: UNSUPPORTED_STATUS");
    }

    public static class ExeptionState extends RuntimeException {

        public ExeptionState(String message) {
            super(message);
        }

    }

}
