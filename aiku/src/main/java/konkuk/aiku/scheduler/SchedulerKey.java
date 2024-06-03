package konkuk.aiku.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class SchedulerKey {
    private SchedulerType type;
    private Long scheduleId;

    @Override
    public boolean equals(Object o) {
        SchedulerKey object = (SchedulerKey) o;
        return this.type.equals(((SchedulerKey) o).getType()) && this.scheduleId == object.getScheduleId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, scheduleId);
    }
}
