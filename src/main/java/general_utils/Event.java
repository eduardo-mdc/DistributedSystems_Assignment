package general_utils;

public class Event {
    public SocketIdentifier source;
    public String message;
    public Integer counterTimestamp = 0;
    public Event(SocketIdentifier source, String message, Integer counterTimestamp){
        this.message = message;
        this.source = source;
        this.counterTimestamp = counterTimestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "source=" + source +
                ", message='" + message + '\'' +
                ", counter_timestamp=" + counterTimestamp +
                '}';
    }

    public Integer getCounterTimestamp() {
        return this.counterTimestamp;
    }
}
