package mx.horace.lambdas.streams;


import mx.horace.lambdas.App;
import mx.horace.lambdas.Apple;
import mx.horace.lambdas.Dish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;

/*
 * This class summarizes how to create streams and how to create collectors and how to pipeline
 * functions or sorters when sorting a stream
 *
 * what's a lambda?
 * A functional interface (only one abstract method), there're already a bunch of functional interface with target types
 * Predicate, IntPredicate (just for ints)
 * Function, IntFunction, IntToLongFunction (just for ints) andThen, compose (function inside compose is executed first) apply
 *  for Functions the last "Parameter is the one that will be returned Function<Integer,Apple> test= (value) -> new Apple(value) Apple:new
 * BiFunction
 * UnaryOperator
 * BinaryOperator
 * Supplier
 * Consumer
 *
 */
public class StreamsAndCollectors {


    public static void main(String[] args) {

        someExamples();
        bookExamples();

        dishesBookExamples();


    }

    public static void someExamples(){
        Supplier<Apple> newApple = Apple::new; // get Default constructor new Apple()
        newApple.get();
        Consumer<Apple> appleConsumer = (a) -> System.out.println(a.getWeight());        //accept

        appleConsumer.accept(new Apple(20, "yellow"));

        List<String> str = Arrays.asList("z", "Z", "y", "B");
        str.sort(String::compareToIgnoreCase); //Static method reference         str.sort((s1,s2)-> s1.compareToIgnoreCase(s2));

        System.out.println(str);
        BiFunction<Integer,String,Apple> appleBiFunction= Apple::new;

        Apple res= appleBiFunction.apply(36,"Orange");
        System.out.println(res);

        List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red")));

        /*Pipeline*/
        inventory.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));

        System.out.println(inventory);

    }

    public static void bookExamples(){

        // 1
        List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red")));

        // [Apple{color='green', weight=80}, Apple{color='red', weight=120}, Apple{color='green', weight=155}]
        inventory.sort(new AppleComparator());
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(30, "green"));

        // 2
        // [Apple{color='green', weight=30}, Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        inventory.sort(new Comparator<Apple>() {
            public int compare(Apple a1, Apple a2){
                return a1.getWeight().compareTo(a2.getWeight());
            }});
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(20, "red"));

        // 3
        // [Apple{color='red', weight=20}, Apple{color='green', weight=30}, Apple{color='green', weight=155}]
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(10, "red"));

        // 4
        // [Apple{color='red', weight=10}, Apple{color='red', weight=20}, Apple{color='green', weight=155}]
        inventory.sort(comparing(Apple::getWeight));
        System.out.println(inventory);
    }

    static class AppleComparator implements Comparator<Apple> {
        public int compare(Apple a1, Apple a2){
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }



    public static void dishesBookExamples(){
        getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);

    }
    public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes){
        System.out.println("Getting dishes below < 400");
        return dishes.stream()
                .filter(d -> d.getCalories() < 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());
    }
}
