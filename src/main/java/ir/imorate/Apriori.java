package ir.imorate;

import lombok.Data;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Apriori {
    private List<List<String>> dataSet;
    private int minSupport;
    private int minConfidence;

    public Apriori(List<List<String>> dataSet, int minSupport) {
        this.dataSet = dataSet;
        this.minSupport = minSupport;
    }

    public Set<String> createInitialSet() {
        Set<String> itemSet = new LinkedHashSet<>();
        for (List<String> recordList : dataSet) {
            itemSet.addAll(recordList);
        }
        return itemSet;
    }

    public Map<List<String>, Integer> generateKItemSet() {
        Map<List<String>, Integer> itemSetMap = new LinkedHashMap<>();
        Set<String> itemSet = createInitialSet();
        for (int i = 1; i < 4; i++) {
            Generator.combination(itemSet).simple(i).forEach((combination) -> {
                int count = getCount(combination);
                if (count >= minSupport) {
                    itemSetMap.put(combination, count);
                }
            });
        }
        return itemSetMap;
    }

    public Map<List<String>, Integer> extractThreeItemSet() {
        Map<List<String>, Integer> kItemSet = generateKItemSet();
        Map<List<String>, Integer> threeItemSetMap = new LinkedHashMap<>();
        for (Map.Entry<List<String>, Integer> entry : kItemSet.entrySet()) {
            if (entry.getKey().size() == 3) {
                threeItemSetMap.put(entry.getKey(), entry.getValue());
            }
        }
        return threeItemSetMap;
    }

    public int getCount(List<String> list) {
        int count = 0;
        for (List<String> recordList : dataSet) {
            if (recordList.containsAll(list)) {
                count++;
            }
        }
        return count;
    }

    public List<List<List<String>>> makeCandidateRules() {
        Map<List<String>, Integer> threeItemSetMap = extractThreeItemSet();
        List<List<List<String>>> rulesList = new ArrayList<>();
        for (Map.Entry<List<String>, Integer> entry : threeItemSetMap.entrySet()) {
            List<String> ruleList = entry.getKey();
            for (int i = 0; i < 3; i++) {
                String selectedRule = ruleList.get(i);
                List<String> selectedRuleList = List.of(selectedRule);
                rulesList.add(List.of(selectedRuleList, getRemainList(ruleList, selectedRule)));
                rulesList.add(List.of(getRemainList(ruleList, selectedRule), selectedRuleList));
            }
        }
        return rulesList;
    }

    public Map<List<List<String>>, Double> calculate() {
        Map<List<List<String>>, Double> resultMap = new LinkedHashMap<>();
        List<List<List<String>>> candidateRules = makeCandidateRules();
        for (List<List<String>> lists : candidateRules) {
            List<String> newList = Stream.concat(lists.get(0).stream(), lists.get(1).stream())
                    .collect(Collectors.toList());
            double percentage = getCount(newList) * 100 / getCount(lists.get(0));
            resultMap.put(lists, percentage);
        }
        return resultMap;
    }

    public List<String> getRemainList(List<String> inputList, String item) {
        List<String> list = new ArrayList<>();
        for (String itemList : inputList) {
            if (!itemList.equals(item)) {
                list.add(itemList);
            }
        }
        return list;
    }

}
