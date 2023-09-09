import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        String[] snapshot6 = new String[] {"aws-imperium | aws-billingcentral-support", "general-read-only % general-write-only | admin"};
//        DiffEntry diffEntry6 = new DiffEntry(snapshot6);


        System.out.println("diffEntry1=" + diffEntry1.getPosixRoles() + "\n");
        System.out.println("diffEntry2=" + diffEntry2.getPosixRoles() + "\n");
        System.out.println("diffEntry3=" + diffEntry3.getPosixRoles() + "\n");
        System.out.println("diffEntry4=" + diffEntry4.getPosixRoles() + "\n");
        System.out.println("diffEntry5=" + diffEntry5.getPosixRoles() + "\n");

        System.out.println("FIRST AUDIT: ");

        DiffEntry currentDiffEntry = diffEntry2;
        DiffEntry previousDiffEntry = diffEntry1;
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateGroupDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("same"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("same"));
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateRoleDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(currentDiffEntry,
                previousDiffEntry).get("roles"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateRoleDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(previousDiffEntry,
                currentDiffEntry).get("roles"));


        System.out.println();System.out.println();System.out.println();

        System.out.println("SECOND AUDIT: ");

        currentDiffEntry = diffEntry3;
        previousDiffEntry = diffEntry2;
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateGroupDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("same"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("same"));
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateRoleDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(currentDiffEntry,
                previousDiffEntry).get("roles"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateRoleDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(previousDiffEntry,
                currentDiffEntry).get("roles"));

        System.out.println();System.out.println();System.out.println();

        System.out.println("THIRD AUDIT: ");

        currentDiffEntry = diffEntry4;
        previousDiffEntry = diffEntry3;
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("same"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("same"));
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateRoleDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(currentDiffEntry,
                previousDiffEntry).get("roles"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateRoleDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(previousDiffEntry,
                currentDiffEntry).get("roles"));

        System.out.println();System.out.println();System.out.println();

        System.out.println("FOURTH AUDIT: ");

        currentDiffEntry = diffEntry5;
        previousDiffEntry = diffEntry4;
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateGroupDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(currentDiffEntry,
                previousDiffEntry).get("same"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("diff"));
        System.out.println("sameGroups=" +
            calculateGroupDiffs(previousDiffEntry,
                currentDiffEntry).get("same"));System.out.println();
        // if currentDiffEntry has more posixGroups than previousDiffEntry,
        // then collect addedGroups
        calculateRoleDiffs(currentDiffEntry,
            previousDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" + calculateRoleDiffs(currentDiffEntry,
                previousDiffEntry).get("roles"));
        System.out.println();
//        System.out.println("calculateRoleDiffs(currentDiffEntry,\n" +
//            "                previousDiffEntry).get(\"rolesDiffs\")" + calculateRoleDiffs(currentDiffEntry,
//            previousDiffEntry).get("rolesDiffs"));
        // if previousDiffEntry has more groups than currentDiffEntry,
        // then collect removedGroups
        calculateRoleDiffs(previousDiffEntry,
            currentDiffEntry).get("operation").forEach(System.out::print);
        System.out.println("=" +
            calculateRoleDiffs(previousDiffEntry,
                currentDiffEntry).get("roles"));

        System.out.println();System.out.println();System.out.println();

//        Map<String, Set<String>> map = new HashMap<>();
//        map.put("a", new HashSet<>(Collections.singleton("bella")));
//        System.out.println(map);
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
