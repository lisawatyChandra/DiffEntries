<<<<<<< HEAD
=======
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        for (Map<String, Map<String, Set<String>>> stringSetMap : Arrays.asList(calculateGroupDiffs(currentDiffEntry,
            previousDiffEntry), calculateGroupDiffs(previousDiffEntry,
            currentDiffEntry))) {

            stringSetMap.get("operation").forEach((key, value) -> {
                if (key.equals("ADD_GROUP") && !stringSetMap.get("diff").isEmpty()) {
                    System.out.println("Gained group access via " + stringSetMap.get("diff").keySet() +
                        " managed by \"team\" with " +   stringSetMap.get("diff").values() +
                        " role(s) at " + Instant.now().toString());
                }

                if (key.equals("REMOVE_GROUP") && !stringSetMap.get("diff").isEmpty()) {
                    System.out.println("Lost group access via " + stringSetMap.get("diff").keySet() +
                        " managed by \"team\" with " + stringSetMap.get("diff").values() +
                        " role(s) at " + Instant.now().toString());
                }
            });
        }

        for (Map<String, Map<String, Set<String>>> stringSetMap : Arrays.asList(calculateRoleDiffs(currentDiffEntry,
            previousDiffEntry), calculateRoleDiffs(previousDiffEntry,
            currentDiffEntry))) {

            if ((Objects.nonNull(stringSetMap.get("diffs")))) {
                stringSetMap.get("operation").forEach((key, value) -> {
                    switch (key) {
                        case "ADD_ROLE" -> stringSetMap.get("diffs").forEach((k, v) ->
                            System.out.println("Gained role access to " +
                            v + " via " + k + " (POSIX) managed by team at " +
                            Instant.now().toString()));
                        case "REMOVE_ROLE" -> stringSetMap.get("diffs").forEach((k, v) ->
                            System.out.println("Lost role access to " +
                            v + " via " + k + " (POSIX) managed by team at " +
                            Instant.now().toString()));
                    }
                });
            }
        }
    }

    private static Map<String, Map<String, Set<String>>> calculateRoleDiffs(DiffEntry diffEntryA, DiffEntry diffEntryB) {
        Map<String, Map<String, Set<String>>> map = new HashMap<>();
        Map<String, Set<String>> sameKeys = new HashMap<>();
        sameKeys.put("SAME_GROUP", calculateGroupDiffs(diffEntryA, diffEntryB).get("same").keySet());

        Map<String,Set<String>> rolesDiffs = new HashMap<>();
        for (String posix : sameKeys.get("SAME_GROUP")) {
            determineDiffRoles(diffEntryA, diffEntryB, map, rolesDiffs, posix);
        }

        Map<String, Set<String>> operation = new HashMap<>();
        if (diffEntryA.getTimestamp() > diffEntryB.getTimestamp()) {
            operation.put("ADD_ROLE", Collections.singleton("add_role"));
            map.put("operation", operation);
        } else {
            operation.put("REMOVE_ROLE", Collections.singleton("remove_role"));
            map.put("operation", operation);
        }

        return map;
    }

    private static void determineDiffRoles(DiffEntry diffEntryA, DiffEntry diffEntryB,
                                           Map<String, Map<String, Set<String>>> map,
                                           Map<String, Set<String>> rolesDiffs, String posix) {
        Set<String> diffRoles = new HashSet<>();
        List<List<List<String>>> posixRolesList_A = convertMapToList(diffEntryA);
        List<List<List<String>>> posixRolesList_B = convertMapToList(diffEntryB);

        int bound = Math.min(posixRolesList_B.size(), posixRolesList_A.size());
        String relevantPosix = null;
        for (int i = 0; i < bound; i++) {
            if (posixRolesList_B.get(i).get(0).get(0).equals(posixRolesList_A.get(i).get(0).get(0))
            && posix.equals(posixRolesList_B.get(i).get(0).get(0))) {
                for (String role : posixRolesList_A.get(i).get(1)) {
                    if (!posixRolesList_B.get(i).get(1).contains(role)) {
                        relevantPosix = posixRolesList_A.get(i).get(0).get(0);
                        diffRoles.add(role);
                    }
                }
            }

            if (posix.equals(relevantPosix)) {
                rolesDiffs.put(relevantPosix, diffRoles);
//                map.put(posix, rolesDiffs);
                map.put("diffs", rolesDiffs);
            }
        }
    }

    private static List<List<List<String>>> convertMapToList(DiffEntry diffEntryA) {
        Map<String, Set<String>> posixRolesMap_A = diffEntryA.getPosixRoles();
        List<List<List<String>>> posixGroupAndRolesList_A = new ArrayList<>();

        for (Map.Entry<String, Set<String>> posixRole : posixRolesMap_A.entrySet()) {
            List<List<String>> posixToRolesList = new ArrayList<>();
            List<String> posixGroup = new ArrayList<>();
            posixGroup.add(posixRole.getKey());
            List<String> posixRoles = new ArrayList<>(posixRole.getValue());
            posixToRolesList.add(posixGroup);
            posixToRolesList.add(posixRoles);
            posixGroupAndRolesList_A.add(posixToRolesList);
        }

        return posixGroupAndRolesList_A;
    }

    private static Map<String, Map<String, Set<String>>> calculateGroupDiffs(DiffEntry entryA, DiffEntry entryB) {
        Map<String,Map<String, Set<String>>> changeInfo = new HashMap<>();
        Map<String, Set<String>> groupDifferences = new HashMap<>();
        Map<String, Set<String>> sameGroups = new HashMap<>();

        for (Map.Entry<String, Set<String>> posixRolesA : entryA.getPosixRoles().entrySet()) {
            if (!entryB.getPosixRoles().containsKey(posixRolesA.getKey())) {
                groupDifferences.put(posixRolesA.getKey(), posixRolesA.getValue());
            } else {
                sameGroups.put(posixRolesA.getKey(), posixRolesA.getValue());
            }
        }

        changeInfo.put("diff", groupDifferences);
        changeInfo.put("same", sameGroups);

        if (entryA.getTimestamp() > entryB.getTimestamp()) {
            Map<String, Set<String>> operationMap = new HashMap<>();
            Set<String> operation = new HashSet<>();
            operation.add("add_group");
            operationMap.put("ADD_GROUP", operation);
            changeInfo.put("operation", operationMap);
        } else {
            Map<String, Set<String>> operationMap = new HashMap<>();
            Set<String> operation = new HashSet<>();
            operation.add("remove_group");
            operationMap.put("REMOVE_GROUP", operation);
            changeInfo.put("operation", operationMap);
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
>>>>>>> 1eeea04 (Add logic for calculating differences in roles, still faulty but getting closer)
