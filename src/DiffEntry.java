import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a collection of posix-group(s), where each posix-group is
 * mapped to its associated role(s).
 */
public class DiffEntry {

    private final Map<String, Set<String>> posixRoles;
    private long timestamp;

    /**
     * Constructs a DiffEntry instance from an array of strings.
     * @param snapshot sample array argument:
     *                 [ "aws-firefly | bcp-testing", "admin % read-only % write-only | admin" ]
     */
    public DiffEntry(String[] snapshot) {
        posixRoles = mapGroupToRoles(extractGroups(snapshot[0]), extractRoles(snapshot[1]));
        timestamp = Instant.now().toEpochMilli();
    }

    /**
     * Takes a string and turns in into an array of strings by splitting the string
     * whenever there is a "|" symbol.
     * @param groupsString a string of group(s), if there is more than one group in the string,
     *                     they are separated by the "|" symbol
     * @return sample array returned: [ "aws-firefly", "bcp-testing" ]
     */
    private String[] extractGroups(String groupsString) {
        return groupsString.replace(" ", "").split("\\|");
    }

    public Map<String, Set<String>> getPosixRoles() {
        return posixRoles;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Takes a string and turns it into an array of strings by separating the string
     * whenever there is a "|" symbol.
     * @param rolesString a string of roles, role(s) belonging to separate groups are
     *                    separated by a "|" symbol
     * @return sample array returned: [ "admin%read-only", "admin" ]
     */
    private String[] extractRoles(String rolesString) {
        return rolesString.replace(" ", "").split("\\|");
    }

    /**
     * @param groupsArray each string in the array is a posix-group
     * @param rolesArray each string in the array describe role(s) that is/are associated
     *                   with its corresponding posix-group; if there is more than one role
     *                   in a string, they are separated by "%" symbol
     * @return a map that maps a posix-group to its associated role(s)
     */
    private Map<String, Set<String>> mapGroupToRoles(String[] groupsArray, String[] rolesArray) {
        Map<String, Set<String>> posixToRoles = new HashMap<>();
        for (int i = 0; i < groupsArray.length; i++) {
            Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesArray[i].split("%")));
            posixToRoles.put(groupsArray[i], rolesSet);
        }

        return posixToRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiffEntry that = (DiffEntry) o;

        return Objects.equals(getPosixRoles(), that.getPosixRoles())
            && Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosixRoles(), getTimestamp());
    }

    @Override
    public String toString() {
        return "timestamp=" + getTimestamp() + "\n" +
            "posixGroupToRoles=" + getPosixRoles();
    }
}
