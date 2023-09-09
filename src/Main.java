import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String[] snapshot1 = new String[] {"aws-firefly", "admin % read-only % write-only"};
        DiffEntry diffEntry1 = new DiffEntry(snapshot1);
        String[] snapshot2 = new String[] {"aws-firefly | bcp-testing", "admin % read-only % write-only | admin"};
        DiffEntry diffEntry2 = new DiffEntry(snapshot2);
        diffEntry2.setTimestamp(diffEntry1.getTimestamp() + 1);
        String[] snapshot3 = new String[] {"aws-firefly | bcp-testing", "admin % read-only | admin"};
        DiffEntry diffEntry3 = new DiffEntry(snapshot3);
        diffEntry3.setTimestamp(diffEntry2.getTimestamp() + 1);
        String[] snapshot4 = new String[] {"aws-imperium | aws-billingcentral-support", "general-read-only | admin"};
        DiffEntry diffEntry4 = new DiffEntry(snapshot4);
        diffEntry4.setTimestamp(diffEntry3.getTimestamp() + 1);
        String[] snapshot5 = new String[] {"aws-imperium | aws-billingcentral-support", "general-read-only % general-write-only | admin"};
        DiffEntry diffEntry5 = new DiffEntry(snapshot5);
        diffEntry5.setTimestamp(diffEntry4.getTimestamp() + 1);

        System.out.println("diffEntry1=" + diffEntry1.getPosixRoles() + "\n");
        System.out.println("diffEntry2=" + diffEntry2.getPosixRoles() + "\n");
        System.out.println("diffEntry3=" + diffEntry3.getPosixRoles() + "\n");
        System.out.println("diffEntry4=" + diffEntry4.getPosixRoles() + "\n");
        System.out.println("diffEntry5=" + diffEntry5.getPosixRoles() + "\n");

        System.out.println("FIRST AUDIT: ");
        DiffEntry currentDiffEntry = diffEntry2;
        DiffEntry previousDiffEntry = diffEntry1;
        calculate(currentDiffEntry, previousDiffEntry);
        System.out.println();System.out.println();System.out.println();

        System.out.println("SECOND AUDIT: ");
        currentDiffEntry = diffEntry3;
        previousDiffEntry = diffEntry2;
        calculate(currentDiffEntry, previousDiffEntry);
        System.out.println();System.out.println();System.out.println();

        System.out.println("THIRD AUDIT: ");
        currentDiffEntry = diffEntry4;
        previousDiffEntry = diffEntry3;
        calculate(currentDiffEntry, previousDiffEntry);
        System.out.println();System.out.println();System.out.println();


        System.out.println("FOURTH AUDIT: ");
        currentDiffEntry = diffEntry5;
        previousDiffEntry = diffEntry4;
        calculate(currentDiffEntry, previousDiffEntry);
        System.out.println();System.out.println();System.out.println();
    }

    private static void calculate(DiffEntry currentDiffEntry, DiffEntry previousDiffEntry) {
        for (Map<String, Set<String>> stringSetMap : Arrays.asList(calculateGroupDiffs(currentDiffEntry,
            previousDiffEntry), calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry))) {
            stringSetMap.get("operation").forEach(System.out::print);
            System.out.println("=" +
                stringSetMap.get("diff"));
        }

        for (Map<String, Set<String>> stringSetMap : Arrays.asList(calculateRoleDiffs(currentDiffEntry,
            previousDiffEntry), calculateRoleDiffs(previousDiffEntry,
            currentDiffEntry))) {
            stringSetMap.get("operation").forEach(System.out::print);
            System.out.println("=" +
                stringSetMap.get("roles"));
        }
    }

    private static Map<String, Set<String>> calculateRoleDiffs(DiffEntry diffEntryA, DiffEntry diffEntryB) {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> sameKeys = new HashSet<>();
        sameKeys.addAll(calculateGroupDiffs(diffEntryA, diffEntryB).get("same"));
        sameKeys.addAll(calculateGroupDiffs(diffEntryB, diffEntryA).get("same"));

        for (String posix : sameKeys) {
            Set<String> rolesDiffs = new HashSet<>();
            for (String currentRole : diffEntryA.getPosixRoles().get(posix)) {
                if (!diffEntryB.getPosixRoles().get(posix).contains(currentRole)) {
                    rolesDiffs.add(currentRole);
                }
            }
            if (!rolesDiffs.isEmpty()) {
                map.put("roles", rolesDiffs);
            }
        }

        if (diffEntryA.getTimestamp() > diffEntryB.getTimestamp()) {
            map.put("operation", Collections.singleton("add_role"));
        } else {
            map.put("operation", Collections.singleton("remove_role"));
        }

        return map;
    }

    private static Map<String, Set<String>> calculateGroupDiffs(DiffEntry entryA, DiffEntry entryB) {
        Map<String,Set<String>> changeInfo = new HashMap<>();
        Set<String> groupDifferences = new HashSet<>();
        Set<String> sameGroups = new HashSet<>();
        Map<String, Set<String>> posixRolesA = entryA.getPosixRoles();
        Map<String, Set<String>> posixRolesB = entryB.getPosixRoles();
        for (String key : posixRolesA.keySet()) {
            if (!posixRolesB.containsKey(key)) {
                groupDifferences.add(key);
            } else {
                sameGroups.add(key);
            }
        }
        changeInfo.put("diff", groupDifferences);
        changeInfo.put("same", sameGroups);

        if (entryA.getTimestamp() > entryB.getTimestamp()) {
            changeInfo.put("operation", new HashSet<>(Collections.singleton("add_group")));
        } else {
            changeInfo.put("operation", new HashSet<>(Collections.singleton("remove_group")));
        }

        return changeInfo;
    }

    private static boolean equalGroups(DiffEntry currentDiffEntry, DiffEntry previousDiffEntry) {
        List<String> currentGroups = new ArrayList<>();
        for (Map.Entry<String, Set<String>> posixGroupRoles : currentDiffEntry.getPosixRoles().entrySet()) {
            currentGroups.add(posixGroupRoles.getKey());
        }

        List<String> previousGroups = new ArrayList<>();
        for (Map.Entry<String, Set<String>> posixGroupRoles : previousDiffEntry.getPosixRoles().entrySet()) {
            previousGroups.add(posixGroupRoles.getKey());
        }

        return currentGroups.equals(previousGroups);
    }

    private static boolean equalRoles(List<String> currentRoles, List<String> previousRoles) {
        return currentRoles.equals(previousRoles);
    }

    private static boolean areEqual(Map<String, Set<String>> first, Map<String, Set<String>> second) {
        if (first.size() != second.size()) {
            return false;
        }

        return first.entrySet().stream()
            .allMatch(e -> e.getValue().equals(second.get(e.getKey())));
    }
}
