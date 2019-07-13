package com.huang.apigateway.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ReactorTest
 * @date 2019年07月02日 10:37:45
 */
@Slf4j
public class ReactorTest {

    /**
     * 简单队列的生成
     */
    @Test
    public void SimpleFluxTest() {
        // just 可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束
        Flux.just("Hello", "World").subscribe(System.out::println);
        // 可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        // 创建一个不包含任何元素，只发布结束消息的序列
        Flux.empty().subscribe(System.out::println);
        // 创建包含从 start 起始的 count 个数量的 Integer 对象的序列
        Flux.range(1, 10).subscribe(System.out::println);
        // 创建一个包含了从 0 开始递增的 Long 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间
        Flux.interval(Duration.of(100, ChronoUnit.SECONDS)).subscribe(System.out::println);
//        Flux.intervalMillis(1000).subscribe(System.out::println);
    }

    /**
     * 复杂队列的生成
     * generate 同步消息产生
     * create 支持同步|异步消息的产生
     */
    @Test
    public void ComplexFluxTest() {
        /*
         * generate()方法通过同步和逐一的方式来产生 Flux 序列。序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，
         * complete()和 error(Throwable)方法来完成的。逐一生成的含义是在具体的生成逻辑中，next()方法只能最多被调用一次。
         * 在有些情况下，序列的生成可能是有状态的，需要用到某些状态对象。此时可以使用 generate()方法的另外一种形式
         * generate(Callable<S> stateSupplier, BiFunction<S,SynchronousSink<T>,S> generator)，其中 stateSupplier
         * 用来提供初始的状态对象。在进行序列生成时，状态对象会作为 generator 使用的第一个参数传入，
         * 可以在对应的逻辑中对该状态对象进行修改以供下一次生成时使用
         */
        Flux.generate(sink -> {
            // next方法产生一个简单的值
            sink.next("Hello");
            // 结束该序列
            sink.complete();
        }).subscribe(System.out::println);


        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);

        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }

    @Test
    public void MonoTest() {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
    }

    /**
     * buffer跟bufferTimeout测试
     * 这两个操作符的作用是把当前流中的元素收集到集合中，并把集合对象作为流中的新元素。
     * 在进行收集时可以指定不同的条件：所包含的元素的最大数量或收集的时间间隔。
     * 方法 buffer()仅使用一个条件，而 bufferTimeout()可以同时指定两个条件。
     * 指定时间间隔时可以使用 Duration 对象或毫秒数，即使用 bufferMillis()或 bufferTimeoutMillis()两个方法
     */
    @Test
    public void bufferAndbufferTimeoutTest() {
        // 输出5个包含20个元素的数组
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        System.out.println("--------------------------");
        /**
         * Flux.interval(Duration period)方法，产生一个从0开始递增的Long对象的序列,其中包含的元素按照指定的时间间隔发布
         * Flux<Long> interval(Duration delay, Duration period)，可以指定第一个元素的发布时间延迟
         * Flux<List<T>> bufferTimeout(int maxSize, Duration maxTime)把当前流中的元素收集到集合中，当超过maxSize/maxTime时，进行下一次收集
         * take系列操作符，表示从当前流中提取元素。Flux<T> take(long n)按照指定的数量来提取元素
         *
         * **首先通过 toStream()方法把 Flux 序列转换成 Java 8 中的 Stream 对象，再通过 forEach()方法来进行输出。这是因为序列的生成是异步的，
         *   而转换成 Stream 对象可以保证主线程在序列生成完成之前不会退出，从而可以正确地输出序列中的所有元素。
         */
        Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .bufferTimeout(2, Duration.of(3, ChronoUnit.SECONDS))
                .take(4).toStream().forEach(System.out::println);
        System.out.println("--------------------------");

        // bufferUntil会一直收集直到 Predicate 返回为 true。使得 Predicate 返回 true 的那个元素可以选择添加到当前集合或下一个集合中
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("--------------------------");

        // bufferWhile只有当 Predicate 返回 true 时才会收集，一旦值为 false，会立即开始下一次收集
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);

    }

    @Test
    public void filterTest() {
        // filter对元素进行过滤，只留下满足Predicate指定条件的元素
        Flux.range(1, 10).filter(integer -> integer % 2 == 0).subscribe(System.out::println);
    }

    /**
     * window操作符测试
     * window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>
     */
    @Test
    public void windowTest() {
        // 两行语句的输出结果分别是 5 个和 2 个 UnicastProcessor 字符。这是因为 window 操作符所产生的流中包含的是 UnicastProcessor 类的对象，
        // 而 UnicastProcessor 类的 toString 方法输出的就是 UnicastProcessor 字符
        Flux.range(1, 100).window(20).subscribe(System.out::println);
        Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
                .window(Duration.of(1, ChronoUnit.SECONDS)).take(2).toStream().forEach(System.out::println);
    }

    /**
     * zipWith操作符测试
     * zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。在合并时可以不做任何处理，
     * 由此得到的是一个元素类型为 Tuple2 的流；也可以通过一个 BiFunction 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。
     */
    @Test
    public void zipWithTest() {
        // 合并不做任何处理
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);
        // 使用合并函数
        Flux.just("a", "b", "p")
                .zipWith(Flux.just("c", "d", "p"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
    }

    /**
     * take操作符测试
     * take 系列操作符用来从当前流中提取元素。提取的方式可以有很多种。
     */
    @Test
    public void takeTest() {
        // 提取10个元素
        Flux.range(1, 1000).take(10).subscribe(System.out::println);
        // 提取最后10个元素
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        // 提取小于10的元素
        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        // 提取元素直到i=-10。
        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
    }

    /**
     * reduce操作符测试
     * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。
     * 累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
     */
    @Test
    public void reduceTest() {
        // 将序列的值相加
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        // 将序列的值相加，指定初始值为100。(即1+...+100+初始值100)
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
        // 将序列的值相乘
        Flux.range(1, 2).reduce((x, y) -> x * y).subscribe(System.out::println);
    }

    /**
     * merge操作符测试
     * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。
     * 不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并。
     */
    @Test
    public void mergeTest() {
        // 第一个序列每一秒产生一个元素。第二个序列，延迟500毫秒，每个1秒产生一个元素。
        // merge按实际元素产出的顺序进行合并
        Flux.merge(Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).take(5),
                Flux.interval(Duration.of(500, ChronoUnit.MILLIS), Duration.of(1, ChronoUnit.SECONDS)).take(5))
                .toStream()
                .forEach(System.out::println);

        // 产出顺序同理
        // 当是mergeSequential是按照所有流被订阅的顺序，以流为单位进行合并
        Flux.mergeSequential(Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).take(5),
                Flux.interval(Duration.of(500, ChronoUnit.MILLIS), Duration.of(1, ChronoUnit.SECONDS)).take(2))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * flatMap操作符测试
     * flatMap操作符将操作流中的每个元素转换成一个流，再把所有流进行合并。
     * flatMap跟flatMapSequential的区别同merge、mergeSequential的区别一样
     */
    @Test
    public void flatMapTest() {
        // 将5跟10转换成每隔100毫秒产出一个数据的流，取5跟10个元素，然后将两个流合并
        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.of(100, ChronoUnit.MILLIS))
                        .take(x))
                .toStream()
                .forEach(System.out::println);
        System.out.println("-----------------------");

        Flux.just(5, 10)
                .flatMapSequential(x -> Flux.interval(Duration.of(100, ChronoUnit.MILLIS))
                        .take(x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * concatMap操作符测试
     * concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。
     * 与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；
     * 与 flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流。
     */
    @Test
    public void concatMapTest() {
        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * combineLatest操作符测试
     * combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
     * 只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。
     * 在 代码清单 14 中，流中最新产生的元素会被收集到一个数组中，通过 Arrays.toString 方法来把数组转换成 String。
     */
    @Test
    public void combineLatestTest() {
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).take(5),
                Flux.interval(Duration.of(50, ChronoUnit.MILLIS), Duration.of(100, ChronoUnit.MILLIS)).take(5)
        ).toStream().forEach(System.out::println);
    }

    /**
     * 消息处理测试
     */
    @Test
    public void messageHandleTest() {
        // subscribe订阅正常消息跟异常消息
        Flux.just(1, 2)
                .concatWith(Mono.just(6))
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);

        // 遇到错误消息的时候，通过onErrorReturn方法返回一个默认值
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(9) // 必须每个error后面都加上onErrorReturn，不会全局调用的
                .concatWith(Mono.error(new RuntimeException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);

        // 出错重新订阅
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(1)
                .subscribe(System.out::println);

    }

    @Test
    public void schedulerTest() {
        Flux.range(1, 10000)
                .publishOn(Schedulers.parallel())
                .subscribe(System.out::println);
    }

    @Test
    public void testViaStepVerifier() {
//        StepVerifier.create(Flux.just(1, 2, 3, 4, 5, 6))
//                .expectNext(1, 2, 3, 4, 5, 1)
//                .expectComplete()
//                .verify();
//        StepVerifier.create(Mono.error(new Exception("some error")))
//                .expectErrorMessage("some error")
//                .verify();
//
//        StepVerifier.create(Flux.range(1, 6)
//                .map(i -> i * i))   // 2
//                .expectNext(1, 4, 9, 16, 25, 36)
//                .verifyComplete(); //verifyComplete() = expectComplete().verify()
//
//
//        StepVerifier.create(
//                Flux.just("flux", "mono")
//                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
//                                .delayElements(Duration.ofMillis(100))) // 2
//                        .doOnNext(System.out::print)) // 3
//                .expectNextCount(8) // 4
//                .verifyComplete();

//        StepVerifier.create(
//                Flux.just("flux", "mono")
//                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))
//                                .delayElements(Duration.ofMillis(100)))
//                        .doOnNext(System.out::println))
//                .expectNextCount(8) // 4
//                .verifyComplete();

        // generate提供一个初始值，然后对值做操作产生下一个元素（返回一个新的状态值供下一次调用）
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.subscribe(log::info);


        Flux<String> flux2 = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("state: " + state));
    }

    @Test
    public void errorTest() {
        Flux.<String>error(new IllegalArgumentException())
                .doOnError(System.out::println)
                .retryWhen(companion -> companion.take(3))
                .subscribe();
    }

    @Test
    public void testAppendBoomError() {
//        Flux<String> source = Flux.just("foo", "bar");
//
//        StepVerifier.create(
//                source.concatWith(Mono.error(new RuntimeException("boom"))))
//                .expectNext("foo")
//                .expectNext("bar")
//                .expectErrorMessage("boom")
//                .verify();
        Flux.range(1, 10)
                .log()
                .take(3)
                .subscribe();
    }

    @Test
    public void androidPkTest() {
        Flux.just(2, 43, 4, 14)
                .map(i -> i * 2 + 3)
                .subscribe(System.out::println);

        List<Integer> list = Arrays.asList(2, 43, 4, 14);
        list.stream().map(i -> i * 2 + 3).forEach(System.out::println);
    }

}
