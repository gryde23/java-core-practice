package task2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyAnalysis<T> {
    public Map<T, Integer> analysis(List<? extends T> list) {
        Map<T, Integer> res = new HashMap<>(list.size());
        for(T elem: list) {
            if (res.containsKey(elem)) {
                Integer count = res.get(elem);
                res.put(elem, ++count);
            } else {
                res.put(elem, 1);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        List<Integer> integerList = List.of(1,2,3,4,5,6,7,8,9,9,9,9,10,1,2,1);
        List<String> stringList = List.of("hello", "world", "world", "123");
        List<Character> charList = List.of('h', 'e', 'l', 'l', 'o', 'w');

        System.out.println(new FrequencyAnalysis<Integer>().analysis(integerList));
        System.out.println(new FrequencyAnalysis<String>().analysis(stringList));
        System.out.println(new FrequencyAnalysis<Character>().analysis(charList));
    }
}
