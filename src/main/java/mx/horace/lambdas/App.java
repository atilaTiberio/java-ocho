package mx.horace.lambdas;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class App {

    public static void main(String[] args) throws Exception{

        // Filtering with lambdas
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(120, "red"));

        inventory.stream().filter((apple) -> apple.getWeight()==80).forEach(System.out::println);

        //TODO exercises with Predicate, Consumer, Function, Supplier, UnaryOperator, BinaryOperator, BiPredicate<L,R>
        //TODO BiConsumer<T,U>, BiFunction<T,U,R>

        List<String> cadenas=new ArrayList<>();
        cadenas.add(null);
        cadenas.add("--");

        cadenas.stream().filter( s -> s.length()>0);


        List<String> list= new ArrayList<>();

        Consumer<String> c= s -> list.add(s); // list::add; //s -> {list.add(s);}; //(String s) -> list.add(s); //s -> list.add(s);

        final int testNumber=8443;
        Runnable r = ()-> System.out.println(testNumber);


        Function<Apple,Integer> getWeight= Apple::getWeight;

        UnaryOperator<String> p = cadena -> cadena+"- Algo- ";
        BinaryOperator<Integer> x= (a1,a2) -> a1*a2;
        BiPredicate<String,String> testBi = (s1,s2) -> s1.contains(s2);

        BinaryOperator<Integer> opMax = BinaryOperator.maxBy(Integer::compareTo);

        System.out.println(opMax.apply(20,30));


        Map<String,List<Apple>> appleByColor= inventory.stream().collect(Collectors.groupingBy(Apple::getColor));

        Optional<Apple> biggest=inventory.stream().collect(Collectors.maxBy(comparing(Apple::getWeight)));


        System.out.println(appleByColor);

        System.out.println(biggest.get());

    }

    interface TriFunction<T,U,V> {

        T apply (U u,V v);

    }
}
