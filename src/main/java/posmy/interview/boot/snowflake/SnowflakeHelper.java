package posmy.interview.boot.snowflake;

import java.util.List;

public class SnowflakeHelper {
    private SnowflakeHelper() {

    }

    /**
     * Assign id as long value to the assigner who implement SnowflakeId<Long>
     *
     * @param assigner assigner
     */
    public static void assignLongIds(List<? extends SnowflakeId<Long>> assigner) {
        for (SnowflakeId<Long> assign : assigner) {
            //only assign when the id is null, so will not replace the existing value
            //todo: can add forceAssign parameter, when it is true reset the value regardless it got value or not in future
            if (assign.getId() == null) {
                assign.setId(Snowflake.getSnowflake().nextId());
            }
        }
    }
}
